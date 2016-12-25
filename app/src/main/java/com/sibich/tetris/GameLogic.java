package com.sibich.tetris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sibich.tetris.database.RecordsBaseHelper;
import com.sibich.tetris.database.TetrisDbSchema.RecordsTable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Created by Slavon on 01.11.2016.
 */
public class GameLogic {

    private SQLiteDatabase mDatabase;

    /*private int [] mColors = new int[] {
            R.color.green, R.color.yellow,
            R.color.blue, R.color.purple
    };*/

    private boolean mIsFallDown = true;
    private boolean mIsRotate = true;
    private boolean mIsMoveFigureLeft = true, mIsMoveFigureRight = true;
    private boolean mIsEndOfGame = false;

    private int mScore = 0, mSpeed = 1, mLevel = 1;

    private Timer mTimer;
    private long mTimeInterval = 550;

    private GameMap mMap = null;
    private Semaphore mMapSemaphore = new Semaphore(1);

    public GameLogic(Context context)
    {
        mDatabase = new RecordsBaseHelper(context)
                .getWritableDatabase();
    }

    public int[][] getGameField()
    {
        int[][] field = null;
        try {
            mMapSemaphore.acquire();
            field = mMap.getDrawableField();
            mMapSemaphore.release();
        } catch(InterruptedException e) {}
        return field;
    }

    // TODO: fallen figure list should be used
    public int[][] getFixedBlocks()
    {
        return mMap.getFixedField();
    }

    public int[][] getNextFigureGameField()
    {
        int[][] field = null;
        try {
            mMapSemaphore.acquire();
            field = mMap.getNextFigureDrawableField().clone();
            mMapSemaphore.release();
        } catch (InterruptedException e) {}

        return field;
    }

    public boolean getEndOfGame()
    {
        boolean gameOver = false;
        try {
            mMapSemaphore.acquire();
            gameOver = mMap.isGameOver();
            mMapSemaphore.release();
        } catch(InterruptedException e) {}
        return gameOver;
    }

    public void stopTimer()
    {
        if (mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void startTimer() {
        stopTimer();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                try {
                    mMapSemaphore.acquire();
                    mMap.simulateStep();
                    if (mMap.isGameOver())
                        stopTimer();
                    mMapSemaphore.release();
                }
                catch (InterruptedException e) { }
            }
        }, 0,  mTimeInterval);
    }

    public void rotateFigure()
    {
        try {
            mMapSemaphore.acquire();
            mMap.rotateFigure();
            mMapSemaphore.release();
        } catch(InterruptedException e) {}
    }

    public void moveFigureLeft()
    {
        try {
            mMapSemaphore.acquire();
            mMap.moveFigureLeft();
            mMapSemaphore.release();
        } catch(InterruptedException e) {}
    }

    public void moveFigureRight()
    {
        try {
            mMapSemaphore.acquire();
            mMap.moveFigureRight();
            mMapSemaphore.release();
        } catch(InterruptedException e) {}
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int mScore)
    {
        this.mScore = mScore;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int mSpeed)
    {
        this.mSpeed = mSpeed;
        startTimer();
    }

    public void speedPlus()
    {
        if (getSpeed() < 10) {
            mTimeInterval -= 50;
            setSpeed((getSpeed() + 1));
        }
    }

    public void speedMinus()
    {
        if (getSpeed() > 1) {
            mTimeInterval += 50;
            setSpeed((getSpeed() - 1));
        }
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public void newGame()
    {
        try {
            mMapSemaphore.acquire();
            mMap = new GameMap();
            mMapSemaphore.release();
        } catch(InterruptedException e) { }

        setSpeed(1);
        setScore(0);
        setLevel(1);

        mTimeInterval = 550;
        startTimer();
    }
    public void pauseGame()
    {
        stopTimer();
    }

    public boolean isTopTenResult() {
        // делаем запрос всех данных из таблицы records, получаем Cursor
        Cursor c = mDatabase.query(RecordsTable.NAME, null, null, null, null, null, null);
        int count = c.getCount();

        if (count < 10) {
            c.close();
            return true;
        }
        else {
            String[] columns = new String[]{RecordsTable.Cols.SCORE};
            c = mDatabase.query(RecordsTable.NAME, columns,
                    null, null, null, null, RecordsTable.Cols.SCORE + " DESC", "9, 1");
            if (c.moveToFirst()) {
                int scoreColIndex = c.getColumnIndex(RecordsTable.Cols.SCORE);
                if (getScore() > c.getInt(scoreColIndex)) {
                    c.close();
                    return true;
                }
            }
        }

        c.close();
        return false;
    }

    public boolean updateTableOfRecords(String player) {

        Boolean isUpdate = false, isTwink = false;


        // определяем есть ли запись с ником player
        // делаем запрос всех данных из таблицы records, получаем Cursor
        Cursor c = mDatabase.query(RecordsTable.NAME, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false

        if (c.moveToFirst()) {
            // определяем номер столбца по имени в выборке
            int nickNameColIndex = c.getColumnIndex(RecordsTable.Cols.NICKNAME);
            int scoreColIndex = c.getColumnIndex(RecordsTable.Cols.SCORE);

            do {
                // обновляем запись, если player существует и его значение
                // score меньше, чем данное
                if (c.getString(nickNameColIndex).equals(player)) {
                    isTwink = true;
                    if (c.getInt(scoreColIndex) < getScore()) {
                        isUpdate = true;
                    }
                }
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        c.close();

        ContentValues values = new ContentValues();
        values.put(RecordsTable.Cols.NICKNAME, player);
        values.put(RecordsTable.Cols.SCORE, getScore());

        if (isTwink) {
            if (isUpdate) {
                mDatabase.update(RecordsTable.NAME, values,
                        RecordsTable.Cols.NICKNAME + " = ?", new String[]{player});
                return true;
            }
            return false;
        }
        else {
            // вставляем новую запись, если player не найден
            mDatabase.insert(RecordsTable.NAME, null, values);
            return false;
        }
    }

    
    public void deleteAllRowsInTableOfRecords() {
        mDatabase.delete(RecordsTable.NAME, null, null);
    }
}
