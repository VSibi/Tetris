package com.sibich.tetris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sibich.tetris.database.RecordsBaseHelper;
import com.sibich.tetris.database.TetrisDbSchema.RecordsTable;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Slavon on 01.11.2016.
 */
public class GameLogic {

    private SQLiteDatabase mDatabase;

    private final int mWidthGameField = 10;
    private final int mHeightGameField = 20;
    private int[][] mGameField =  new int [mHeightGameField][mWidthGameField];
    private int[][] mFixedBlocks = new int[mHeightGameField][mWidthGameField];

    private int[][] mNextFigureGameField = new int[2][4];

    private Figure_T figure_T = new Figure_T();
    private Figure_SQ figure_SQ = new Figure_SQ();
    private Figure_S_Left figure_S_Left = new Figure_S_Left();
    private Figure_I figure_I = new Figure_I();

    private Figure[] mAllFigures = new Figure[] {
            figure_SQ, figure_T, figure_S_Left, figure_I
    };

    private LinkedList<Figure> mListNextFigures = new LinkedList<>();

    private Figure mCurrFigure = new Figure();

    private int [] mFullLines = new int[mHeightGameField];

    private int [] mColors = new int[] {
            R.color.green, R.color.yellow,
            R.color.blue, R.color.purple
    };

    private boolean mIsFallDown = true;
    private boolean mIsRotate = true;
    private boolean mIsMoveFigureLeft = true, mIsMoveFigureRight = true;
    private boolean mIsEndOfGame = false;

    private int mScore = 0, mSpeed = 1, mLevel = 1;

    private Timer mTimer;
    private long mTimeInterval = 550;



    public GameLogic(Context context) {

        mDatabase = new RecordsBaseHelper(context)
                .getWritableDatabase();



        for (int i = 0; i < mGameField.length; i++) {
            for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = 0;
                mFixedBlocks[i][j] = 0;
            }
        }

        initListNextFigures();
        updateNextFigureGameField(mListNextFigures.get(1));
        mCurrFigure = mListNextFigures.poll();
        mCurrFigure.setPosition(4, 0);


        addRandomFigureToList();
    }

    private void addRandomFigureToList() {
        Random mRandom = new Random();
        int i = 0;
        Figure figure;

        i = mRandom.nextInt(mAllFigures.length);
        figure = mAllFigures[i].clone();

        i = mRandom.nextInt(mColors.length);
        figure.setColor(mColors[i]);

        mListNextFigures.add(figure);
    }

    private void initListNextFigures() {
        for (int i = 0; i < 2; i++) {
            addRandomFigureToList();

        }

    }

    public int[][] getGameField() {
        return mGameField;
    }

    public int[][] getFixedBlocks() {
        return mFixedBlocks;
    }
    public void setFixedBlocks(int[][] fixedBlocks) {
        for (int i = 0; i < mFixedBlocks.length; i++) {
            for (int j = 0; j < mFixedBlocks[i].length; j++) {
                mFixedBlocks[i][j] = fixedBlocks[i][j];
            }
        }
    }

    public int[][] getNextFigureGameField() {
        return mNextFigureGameField;
    }

   /* public int[] getFullLines() {
        return mFullLines;
    }*/

    public boolean getEndOfGame() {
        return mIsEndOfGame;
    }

    public void initNewGame() {
        mIsEndOfGame = false;
    }

  /*  public int[] getColors() {
        return mColors;
    }*/

    public Figure getCurrFigure() {
        return mCurrFigure;
    }

    public void nextFigure() {
        if (!mIsEndOfGame) {

            updateNextFigureGameField(mListNextFigures.get(1));
            mCurrFigure = mListNextFigures.poll();
            mCurrFigure.setPosition(4, 0);

            mIsFallDown = true;
            mIsMoveFigureLeft = true;
            mIsMoveFigureRight = true;

            addRandomFigureToList();
        }
    }

    public void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                        fallDownFigure(mCurrFigure);

                        if (getEndOfGame()) pauseGame();

            }
        }, 0,  mTimeInterval);
    }

    public void fallDownFigure(Figure figure) {
        checkCollision(figure);

        if (!mIsFallDown) {
            fixFigure();
            do {
                figure.rotate();
            }while (!figure.getStateOfRotation().equals("Normal"));
            nextFigure();
        }
        else {
            updateGameField(figure);
            figure.setTranslate(0, 1);
            mIsRotate = true;
        }
    }

    public void quickFallDownFigure(Figure figure) {
        checkCollision(figure);

        while (mIsFallDown) {
            updateGameField(figure);
            figure.setTranslate(0, 1);
            checkCollision(figure);
        }

        fixFigure();
        do {
            figure.rotate();
        }while (!figure.getStateOfRotation().equals("Normal"));
        nextFigure();
    }

    public void rotateFigure(Figure figure) {
        checkCollision(figure);
        if (mIsRotate) {
            figure.rotate();
            updateGameField(figure);
        }

    }

    public void moveFigureLeft(Figure figure) {
        checkCollision(figure);
        if (mIsMoveFigureLeft) {
            figure.setTranslate(-1, 0);
            mIsMoveFigureRight = true;
            updateGameField(figure);
        }
    }

    public void moveFigureRight(Figure figure) {
        checkCollision(figure);
        if (mIsMoveFigureRight) {
            figure.setTranslate(1, 0);
            mIsMoveFigureLeft = true;
            updateGameField(figure);
        }
    }

    private void fixFigure() {
        for (int i = 0; i < mFixedBlocks.length; i++) {
            for (int j = 0; j < mFixedBlocks[i].length; j++) {
                mFixedBlocks[i][j] = mGameField[i][j];
            }
        }
        deleteFullLines();
        checkEndOfGame();
    }

    private void checkCollision(Figure figure) {
        if (figure.getBasicPoint().y == mGameField.length) {
            mIsFallDown = false;
            mIsRotate = false;
            return;
        }

        for (int i = 0; i < figure.getAllCoord().length; i++) {
            int x = figure.getAllCoord()[i].x;
            int y = figure.getAllCoord()[i].y;
            if (mFixedBlocks[(y)][x] != 0) {
                mIsFallDown = false;
                mIsRotate = false;
            }

            if (x < 1) {
                mIsMoveFigureLeft = false;
            }else {
                if (mFixedBlocks[y][x - 1] != 0) {
                    mIsMoveFigureLeft = false;
                }
            }


            if (x > mGameField[0].length - 2) {
                mIsMoveFigureRight = false;
            }
            else {
                if (mFixedBlocks[y][x + 1] != 0) {
                    mIsMoveFigureRight = false;
                }
            }
        }
    }


    public void updateNextFigureGameField(Figure figure) {

        for (int i = 0; i < mNextFigureGameField.length; i++) {
            for (int j = 0; j < mNextFigureGameField[i].length; j++) {
                mNextFigureGameField[i][j] = 0;
            }
        }

        figure.setPosition(0, 0);

        for (int i = 0; i < figure.getAllCoord().length; i++) {
            int x = figure.getAllCoord()[i].x;
            int y = figure.getAllCoord()[i].y;
            switch (figure.getColor()) {
                case R.color.green:
                    mNextFigureGameField[y][x] = mColors[0];
                    break;
                case R.color.yellow:
                    mNextFigureGameField[y][x] = mColors[1];
                    break;
                case R.color.blue:
                    mNextFigureGameField[y][x] = mColors[2];
                    break;
                case R.color.purple:
                    mNextFigureGameField[y][x] = mColors[3];
                    break;
            }
        }
    }

    public void updateGameField(Figure figure) {

        for (int i = 0; i < mGameField.length; i++) {
            for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = mFixedBlocks[i][j];
            }
        }

        for (int i = 0; i < figure.getAllCoord().length; i++) {
            int x = figure.getAllCoord()[i].x;
            int y = figure.getAllCoord()[i].y;
            switch (figure.getColor()) {
                case R.color.green:
                    mGameField[y][x] = mColors[0];
                    break;
                case R.color.yellow:
                    mGameField[y][x] = mColors[1];
                    break;
                case R.color.blue:
                    mGameField[y][x] = mColors[2];
                    break;
                case R.color.purple:
                    mGameField[y][x] = mColors[3];
                    break;
            }
        }
    }

    private void checkEndOfGame() {
        if (!mIsFallDown) {
            if (mFixedBlocks[1][5] != 0) {
                mIsEndOfGame = true;
                return;
            }
            for (int i = 0; i < mFixedBlocks[0].length; i++) {
                if (mFixedBlocks[0][i] != 0) mIsEndOfGame = true;
            }
        }
    }

    public void checkFullLines() {
        int index = 0;
        for (int i = 0; i < mFixedBlocks.length; i++) {
            for (int j = 0; j < mFixedBlocks[i].length; j++) {
                if (mFixedBlocks[i][j] != 0) index++;
            }
            if(index == mWidthGameField) mFullLines[i] = 1;
            else mFullLines[i] = 0;
            index = 0;
        }
    }

    public void deleteFullLines() {
        boolean isMoreFullLines = true;

        while (isMoreFullLines) {
            checkFullLines();
            int index = 0;
            for (int line : mFullLines) {
                if (line == 1) index++;
            }
            if (index == 0) isMoreFullLines = false;
            else {
                for (int i = mFixedBlocks.length - 1; i > 2; i--) {
                    if (mFullLines[i] == 1) {
                        moveLinesDown(i);
                        setScore((getScore() + 15));
                        if (getScore() % 105 == 0) speedPlus();
                        break;
                    }
                }

            }
        }
    }

    public void moveLinesDown(int line) {
                for (int j = line; j > 2; j--) {
                    for (int k = (mFixedBlocks[j].length - 1); k > -1; k--) {
                        mFixedBlocks[j][k] = mFixedBlocks[j - 1][k];
                    }
                }
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int mSpeed) {
        this.mSpeed = mSpeed;
    }

    public void speedPlus() {
        if (getSpeed() < 10) {
            mTimeInterval -= 50;
            setSpeed((getSpeed() + 1));

            startTimer();
        }
    }

    public void speedMin() {
        if (getSpeed() > 1) {
            mTimeInterval += 50;
            setSpeed((getSpeed() - 1));

            startTimer();
        }
    }

   /* public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }*/

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public void pauseGame() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void newGame() {
        for (int i = 0; i < mGameField.length; i++) {
            for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = 0;
                mFixedBlocks[i][j] = 0;
            }
        }

        setSpeed(1);
        setScore(0);
        setLevel(1);
        mIsEndOfGame = false;
        mListNextFigures.clear();
        initListNextFigures();
        nextFigure();

        mTimeInterval = 550;
        startTimer();
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
