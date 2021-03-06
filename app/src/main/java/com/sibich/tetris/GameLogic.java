package com.sibich.tetris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import com.sibich.tetris.database.TetrisBaseHelper;
import com.sibich.tetris.database.TetrisDbSchema.BorderGameFieldTable;
import com.sibich.tetris.database.TetrisDbSchema.RecordsTable;
import com.sibich.tetris.database.TetrisDbSchema.GameFieldForOpenImageTable;
import com.sibich.tetris.database.TetrisDbSchema.GameFieldForClassicTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sibic_000 on 14.01.2017.
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
    private Figure_S_Right figure_S_Right = new Figure_S_Right();
    private Figure_L_Left figure_L_Left = new Figure_L_Left();
    private Figure_L_Right figure_L_Right = new Figure_L_Right();
    private Figure_I figure_I = new Figure_I();

    private Figure[] mAllFigures = new Figure[] {figure_L_Right,
            figure_SQ, figure_T, figure_S_Left,
            figure_L_Left, figure_S_Right, figure_I
    };

    private LinkedList<Figure> mListNextFigures = new LinkedList<>();
    private List<Figure> mListFallenFigures = new ArrayList<>();
    private List<Figure> mListBrokenFigures = new ArrayList<>();

    private Figure mCurrFigure = new Figure();

    private int [] mFullLines = new int[mHeightGameField];
    private int[] mFullLinesForOpenImage = new int[mHeightGameField];

    private int [] mColors = new int[] {
            R.color.green, R.color.yellow,
            R.color.blue, R.color.purple
    };

    private boolean mIsFallDown = true;
    private boolean mIsRotate = true;
    private boolean mIsMoveFigureLeft = true, mIsMoveFigureRight = true;
    private boolean mIsEndOfGame = false;
    private boolean mIsWin = false;

    private int mScore = 0, mSpeed = 1, mLevel = 1;

    private Timer mTimer;
    private long mTimeInterval = 550;

    // private Timer mTimerForFallenFigures;

    private boolean mIsFallDownAllBrokenFigures = false;

    private String mGameMode = "openImage";

    private String mDBNameTable, mDBCoord1_X, mDBCoord1_Y, mDBCoord2_X, mDBCoord2_Y, mDBCoord3_X, mDBCoord3_Y,
            mDBCoord4_X, mDBCoord4_Y, mDBColor;


    public GameLogic(Context context) {

        mDatabase = new TetrisBaseHelper(context)
                .getWritableDatabase();

        mDBNameTable = GameFieldForOpenImageTable.NAME;

        mDBCoord1_X = GameFieldForOpenImageTable.Cols.COORD1_X;
        mDBCoord1_Y = GameFieldForOpenImageTable.Cols.COORD1_Y;
        mDBCoord2_X = GameFieldForOpenImageTable.Cols.COORD2_X;
        mDBCoord2_Y = GameFieldForOpenImageTable.Cols.COORD2_Y;
        mDBCoord3_X = GameFieldForOpenImageTable.Cols.COORD3_X;
        mDBCoord3_Y = GameFieldForOpenImageTable.Cols.COORD3_Y;
        mDBCoord4_X = GameFieldForOpenImageTable.Cols.COORD4_X;
        mDBCoord4_Y = GameFieldForOpenImageTable.Cols.COORD4_Y;

        mDBColor = GameFieldForOpenImageTable.Cols.COLOR;



        for (int i = 0; i < mGameField.length; i++) {
            for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = 0;
                mFixedBlocks[i][j] = 0;
            }
        }
        setSpeed(1);
        setLevel(1);

        initListNextFigures();
        updateNextFigureGameField(mListNextFigures.get(1));
        mCurrFigure = mListNextFigures.poll();
        mCurrFigure.setPosition(4, 0);


        addRandomFigureToList();
    }

    private void addRandomFigureToList() {
        Random mRandom = new Random();
        int i;
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
    public int[] getFullLinesForOpenImage() {
        return mFullLinesForOpenImage;
    }

     /*   public int[][] getFixedBlocks() {
            return mFixedBlocks;
        }
        public void setFixedBlocks(int[][] fixedBlocks) {

      /*  for (int i = 0; i < mFixedBlocks.length; i++) {
            for (int j = 0; j < mFixedBlocks[i].length; j++) {
                mFixedBlocks[i][j] = fixedBlocks[i][j];
            }
        }*/

       /*     mListFallenFigures.clear();
            for (int i = 0; i < fixedBlocks.length; i++) {
                for (int j = 0; j < fixedBlocks[i].length; j++) {

                    ArrayList<Point> allCoords = new ArrayList<>();
                    Point coord = new Point();
                    if(fixedBlocks[i][j] != 0) coord.set(j, i);
                    allCoords.add(coord);
                    BrokenFigure figure = new BrokenFigure(allCoords);
                    figure.setColor(fixedBlocks[i][j]);
                    mListFallenFigures.add(figure.clone());
                    allCoords.clear();
                }
            }
            updateFixedBlocks();
        }*/

    public List<Figure> getListFallenFigures() {
        return mListFallenFigures;}

    public int[][] getNextFigureGameField() {
        return mNextFigureGameField;
    }

   /* public int[] getFullLines() {
        return mFullLines;
    }*/

    public boolean getEndOfGame() {
        return mIsEndOfGame;
    }
    public boolean isWin() {
        return mIsWin;
    }

  /*  public void initNewGame() {
        mIsEndOfGame = false;
    }*/

  /*  public int[] getColors() {
        return mColors;
    }*/

    public Figure getCurrFigure() {
        return mCurrFigure;
    }

    public String getGameMode() {
        return mGameMode;
    }

    public void setGameMode(String mGameMod) {
        this.mGameMode = mGameMod;

        if(mGameMode.equals("classic")) {
            mDBNameTable = GameFieldForClassicTable.NAME;

            mDBCoord1_X = GameFieldForClassicTable.Cols.COORD1_X;
            mDBCoord1_Y = GameFieldForClassicTable.Cols.COORD1_Y;
            mDBCoord2_X = GameFieldForClassicTable.Cols.COORD2_X;
            mDBCoord2_Y = GameFieldForClassicTable.Cols.COORD2_Y;
            mDBCoord3_X = GameFieldForClassicTable.Cols.COORD3_X;
            mDBCoord3_Y = GameFieldForClassicTable.Cols.COORD3_Y;
            mDBCoord4_X = GameFieldForClassicTable.Cols.COORD4_X;
            mDBCoord4_Y = GameFieldForClassicTable.Cols.COORD4_Y;

            mDBColor = GameFieldForClassicTable.Cols.COLOR;
        }
        else {
            mDBNameTable = GameFieldForOpenImageTable.NAME;

            mDBCoord1_X = GameFieldForOpenImageTable.Cols.COORD1_X;
            mDBCoord1_Y = GameFieldForOpenImageTable.Cols.COORD1_Y;
            mDBCoord2_X = GameFieldForOpenImageTable.Cols.COORD2_X;
            mDBCoord2_Y = GameFieldForOpenImageTable.Cols.COORD2_Y;
            mDBCoord3_X = GameFieldForOpenImageTable.Cols.COORD3_X;
            mDBCoord3_Y = GameFieldForOpenImageTable.Cols.COORD3_Y;
            mDBCoord4_X = GameFieldForOpenImageTable.Cols.COORD4_X;
            mDBCoord4_Y = GameFieldForOpenImageTable.Cols.COORD4_Y;

            mDBColor = GameFieldForOpenImageTable.Cols.COLOR;
        }
    }

    private void nextFigure() {
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

                if (mIsFallDownAllBrokenFigures) {
                    fallDownBrokenFigures();
                 //   removeLine();
                }
                else {
                    fallDownFigure(mCurrFigure);
                }

                if (getEndOfGame()) pauseGame();

            }
        }, 0,  mTimeInterval);
    }

  /*  private void startTimerForFallenFigures() {

        if (mTimerForFallenFigures != null) {
            mTimerForFallenFigures.cancel();
            mTimerForFallenFigures = null;
        }
        mTimerForFallenFigures = new Timer();
        mTimerForFallenFigures.schedule(new TimerTask() {
            @Override
            public void run() {
                fallDownBrokenFigures();
            }
        }, 0,  50);
    }*/

    private void fallDownFigure(Figure figure) {
        checkCollision(figure);

        if (!mIsFallDown) {
            //  fixFigure();

          /*  figure.setTranslate(0, -1);
            mListFallenFigures.add(figure.clone());*/

            fixFigure(figure);

          /*  do {
                figure.rotate();
            }while (!figure.getStateOfRotation().equals("Normal"));*/
            nextFigure();
        }
        else {
            updateGameField(figure);
            figure.setTranslate(0, 1);
            mIsMoveFigureLeft = true;
            mIsMoveFigureRight = true;
            mIsRotate = true;
        }
    }

    public void quickFallDownFigure(Figure figure) {
        if (!mIsEndOfGame) {
            checkCollision(figure);

            while (mIsFallDown) {
                updateGameField(figure);
                figure.setTranslate(0, 1);
                checkCollision(figure);
            }

            //  fixFigure();

      /*  figure.setTranslate(0, -1);
        mListFallenFigures.add(figure.clone());*/

            fixFigure(figure);



      /*  do {
            figure.rotate();
        }while (!figure.getStateOfRotation().equals("Normal"));*/
            nextFigure();
        }
    }

    public void rotateFigure(Figure figure) {
        if (!mIsEndOfGame) {
            checkCollision(figure);
            if (mIsRotate) {
                figure.rotate();
                updateGameField(figure);
            }
            mIsRotate = true;
        }

    }

    public void moveFigureLeft(Figure figure) {
        if (!mIsEndOfGame) {
            checkCollision(figure);
            if (mIsMoveFigureLeft) {
                figure.setTranslate(-1, 0);
                mIsMoveFigureRight = true;
                updateGameField(figure);
            }
            mIsMoveFigureLeft = true;
        }
    }

    public void moveFigureRight(Figure figure) {
        if (!mIsEndOfGame) {
            checkCollision(figure);
            if (mIsMoveFigureRight) {
                figure.setTranslate(1, 0);
                mIsMoveFigureLeft = true;
                updateGameField(figure);
            }
            mIsMoveFigureRight = true;
        }
    }

    private void fixFigure(Figure figure) {
       /* for (int i = 0; i < mFixedBlocks.length; i++) {
            for (int j = 0; j < mFixedBlocks[i].length; j++) {
                mFixedBlocks[i][j] = mGameField[i][j];
            }
        }*/

        for (int j = 0; j < figure.getAllCoords().size(); j++) {
            int x = figure.getAllCoords().get(j).x;
            int y = figure.getAllCoords().get(j).y;
            if (y == 0) {
                switch (figure.getColor()) {
                    case R.color.green:
                        mFixedBlocks[y][x] = mColors[0];
                        break;
                    case R.color.yellow:
                        mFixedBlocks[y][x] = mColors[1];
                        break;
                    case R.color.blue:
                        mFixedBlocks[y][x] = mColors[2];
                        break;
                    case R.color.purple:
                        mFixedBlocks[y][x] = mColors[3];
                        break;
                }
                updateGameField(figure);
                checkEndOfGame();
                return;
            }
        }

        figure.setTranslate(0, -1);
        mListFallenFigures.add(figure.clone());


    /*    for (Figure f : mListFallenFigures) {
    //    for (int i = 0; i < mListFallenFigures.size(); i++) {
        //    fig = mListFallenFigures.get(i).clone();
            for (int j = 0; j < f.getAllCoords().size(); j++) {
                int x = f.getAllCoords().get(j).x;
                int y = f.getAllCoords().get(j).y;
                switch (f.getColor()) {
                    case R.color.green:
                        mFixedBlocks[y][x] = mColors[0];
                        break;
                    case R.color.yellow:
                        mFixedBlocks[y][x] = mColors[1];
                        break;
                    case R.color.blue:
                        mFixedBlocks[y][x] = mColors[2];
                        break;
                    case R.color.purple:
                        mFixedBlocks[y][x] = mColors[3];
                        break;
                }
            }
        }*/

//////////
        for (int i = 0; i < figure.getAllCoords().size(); i++) {
            int x = figure.getAllCoords().get(i).x;
            int y = figure.getAllCoords().get(i).y;
            switch (figure.getColor()) {
                case R.color.green:
                    mFixedBlocks[y][x] = mColors[0];
                    break;
                case R.color.yellow:
                    mFixedBlocks[y][x] = mColors[1];
                    break;
                case R.color.blue:
                    mFixedBlocks[y][x] = mColors[2];
                    break;
                case R.color.purple:
                    mFixedBlocks[y][x] = mColors[3];
                    break;
            }
        }

     /*   for (int i = 0; i < figure.getAllCoord().length; i++) {
            int x = figure.getAllCoord()[i].x;
            int y = figure.getAllCoord()[i].y;
            switch (figure.getColor()) {
                case R.color.green:
                    mFixedBlocks[y][x] = mColors[0];
                    break;
                case R.color.yellow:
                    mFixedBlocks[y][x] = mColors[1];
                    break;
                case R.color.blue:
                    mFixedBlocks[y][x] = mColors[2];
                    break;
                case R.color.purple:
                    mFixedBlocks[y][x] = mColors[3];
                    break;
            }
        }*/

        //   deleteFullLines();
        removeLine();
        checkEndOfGame();
    }

    private void updateNextFigureGameField(Figure figure) {

        for (int i = 0; i < mNextFigureGameField.length; i++) {
            for (int j = 0; j < mNextFigureGameField[i].length; j++) {
                mNextFigureGameField[i][j] = 0;
            }
        }

        figure.setPosition(0, 0);

        for (int i = 0; i < figure.getAllCoords().size(); i++) {
            int x = figure.getAllCoords().get(i).x;
            int y = figure.getAllCoords().get(i).y;
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


    private void checkCollision(Figure figure) {
        if (figure.getAllCoords().get(figure.getAllCoords().size() - 1).y
                == mGameField.length) {
            // if (figure.getBasicPoint().y == mGameField.length) {
            mIsFallDown = false;
            mIsRotate = false;
            return;
        }

        if (figure.getAllCoords().get(figure.getAllCoords().size() - 1).y
                == mGameField.length - 1) {
            mIsRotate = false;
        }

        for (int i = 0; i < figure.getAllCoords().size(); i++) {
            int x = figure.getAllCoords().get(i).x;
            int y = figure.getAllCoords().get(i).y;
            if (mFixedBlocks[y][x] != 0) {
                mIsFallDown = false;
                mIsRotate = false;
            }

            // проверяем возможность поворота и смещения влево
            // возле левого края игрового поля
            if (x < 1) {
                if(mCurrFigure.getClass().getSimpleName().equals("Figure_T")
                        && mCurrFigure.getStateOfRotation().equals("Rotate_270_degrees")) {
                    mIsRotate = false;
                }
                if((mCurrFigure.getClass().getSimpleName().equals("Figure_S_Left")
                        || mCurrFigure.getClass().getSimpleName().equals("Figure_S_Right")
                        || mCurrFigure.getClass().getSimpleName().equals("Figure_L_Left")
                        || mCurrFigure.getClass().getSimpleName().equals("Figure_L_Right")
                        || mCurrFigure.getClass().getSimpleName().equals("Figure_I"))
                        && mCurrFigure.getStateOfRotation().equals("Rotate_90_degrees")) {
                    mIsRotate = false;
                }
               /* if(mCurrFigure.getClass().getSimpleName().equals("Figure_I")
                        && mCurrFigure.getStateOfRotation().equals("Rotate_90_degrees")) {
                    mIsRotate = false;
                }*/

                mIsMoveFigureLeft = false;
            }else {
                // или если слева есть зафиксированные блоки
                if (mFixedBlocks[y][x - 1] != 0) {
                    mIsMoveFigureLeft = false;
                    if(mCurrFigure.getClass().getSimpleName().equals("Figure_T")
                            && mCurrFigure.getStateOfRotation().equals("Rotate_270_degrees")) {
                        mIsRotate = false;
                    }
                    if((mCurrFigure.getClass().getSimpleName().equals("Figure_S_Left")
                            || mCurrFigure.getClass().getSimpleName().equals("Figure_S_Right")
                            || mCurrFigure.getClass().getSimpleName().equals("Figure_L_Left")
                            || mCurrFigure.getClass().getSimpleName().equals("Figure_L_Right")
                            || mCurrFigure.getClass().getSimpleName().equals("Figure_I"))
                            && mCurrFigure.getStateOfRotation().equals("Rotate_90_degrees")) {
                        mIsRotate = false;
                    }
                  /*  if(mCurrFigure.getClass().getSimpleName().equals("Figure_I")
                            && mCurrFigure.getStateOfRotation().equals("Rotate_90_degrees")) {
                        mIsRotate = false;
                    }*/
                }
            }

            // проверяем возможность поворота и смещения вправо
            // возле правого края игрового поля
            if (x > mGameField[0].length - 2) {
                if((mCurrFigure.getClass().getSimpleName().equals("Figure_T")
                        || mCurrFigure.getClass().getSimpleName().equals("Figure_I"))
                        && mCurrFigure.getStateOfRotation().equals("Rotate_90_degrees")) {
                    mIsRotate = false;
                }
                if((mCurrFigure.getClass().getSimpleName().equals("Figure_L_Left")
                        || mCurrFigure.getClass().getSimpleName().equals("Figure_L_Right"))
                        && mCurrFigure.getStateOfRotation().equals("Rotate_270_degrees")) {
                    mIsRotate = false;
                }

                mIsMoveFigureRight = false;
            }
            else {
                // или если справа есть зафиксированные блоки
                if (mFixedBlocks[y][x + 1] != 0) {
                    if((mCurrFigure.getClass().getSimpleName().equals("Figure_T")
                            || mCurrFigure.getClass().getSimpleName().equals("Figure_I"))
                            && mCurrFigure.getStateOfRotation().equals("Rotate_90_degrees")) {
                        mIsRotate = false;
                    }
                    if((mCurrFigure.getClass().getSimpleName().equals("Figure_L_Left")
                            || mCurrFigure.getClass().getSimpleName().equals("Figure_L_Right"))
                            && mCurrFigure.getStateOfRotation().equals("Rotate_270_degrees")) {
                        mIsRotate = false;
                    }

                    mIsMoveFigureRight = false;
                }
            }


            if (x > mGameField[0].length - 3) {
                if (figure.getClass().getSimpleName().equals("Figure_I")
                        && figure.getStateOfRotation().equals("Rotate_90_degrees")) {
                    mIsRotate = false;
                }
            }
            else {
                // или если справа есть зафиксированные блоки
                if (mFixedBlocks[y][x + 2] != 0) {
                    if (figure.getClass().getSimpleName().equals("Figure_I")
                            && figure.getStateOfRotation().equals("Rotate_90_degrees")) {
                        mIsRotate = false;
                    }
                }
            }
        }
    }

    public void clearGameField() {
        for (int i = 0; i < mGameField.length; i++) {
            for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = 0;
                mFixedBlocks[i][j] = 0;
            }
        }

        for (int i = 0; i < mFullLinesForOpenImage.length; i++) {
            mFullLinesForOpenImage[i] = 0;
        }
    }

    private void updateGameField(Figure figure) {

        for (int i = 0; i < mGameField.length; i++) {
            System.arraycopy(mFixedBlocks[i], 0, mGameField[i], 0, mGameField[i].length);
            // manual array copy
           /* for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = mFixedBlocks[i][j];
            }*/
        }

        for (int i = 0; i < figure.getAllCoords().size(); i++) {
            int x = figure.getAllCoords().get(i).x;
            int y = figure.getAllCoords().get(i).y;
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

    private void updateFixedBlocks() {
        for (int i = 0; i < mFixedBlocks.length; i++) {
            for (int j = 0; j < mFixedBlocks[i].length; j++) {
                mFixedBlocks[i][j] = 0;
            }
        }

        //     for (Figure figure : mListFallenFigures) {
        if (mListFallenFigures.size() > 0) {
            for (int i = 0; i < mListFallenFigures.size(); i++) {
                Figure figure = mListFallenFigures.get(i).clone();
                for (int j = 0; j < figure.getAllCoords().size(); j++) {
                    int x = figure.getAllCoords().get(j).x;
                    int y = figure.getAllCoords().get(j).y;

                    // если координата за границами поля, то пропускаем ее
                    if ((x < 0 || x > mWidthGameField - 1)
                            || (y < 0 || y > mHeightGameField - 1)) continue;

                    switch (figure.getColor()) {
                        case R.color.green:
                            mFixedBlocks[y][x] = mColors[0];
                            break;
                        case R.color.yellow:
                            mFixedBlocks[y][x] = mColors[1];
                            break;
                        case R.color.blue:
                            mFixedBlocks[y][x] = mColors[2];
                            break;
                        case R.color.purple:
                            mFixedBlocks[y][x] = mColors[3];
                            break;
                    }
                }
            }
        }


        if (mListBrokenFigures.size() > 0) {
            for (int i = 0; i < mListBrokenFigures.size(); i++) {
                Figure figure = mListBrokenFigures.get(i).clone();
                for (int j = 0; j < figure.getAllCoords().size(); j++) {
                    int x = figure.getAllCoords().get(j).x;
                    int y = figure.getAllCoords().get(j).y;

                    // если координата за границами поля, то пропускаем ее
                    if ((x < 0 || x > mWidthGameField - 1)
                            || (y < 0 || y > mHeightGameField - 1)) continue;

                    switch (figure.getColor()) {
                        case R.color.green:
                            mFixedBlocks[y][x] = mColors[0];
                            break;
                        case R.color.yellow:
                            mFixedBlocks[y][x] = mColors[1];
                            break;
                        case R.color.blue:
                            mFixedBlocks[y][x] = mColors[2];
                            break;
                        case R.color.purple:
                            mFixedBlocks[y][x] = mColors[3];
                            break;
                    }
                }
            }
        }

        for (int i = 0; i < mGameField.length; i++) {
            System.arraycopy(mFixedBlocks[i], 0, mGameField[i], 0, mGameField[i].length);
            // manual array copy
          /*  for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = mFixedBlocks[i][j];
            }*/
        }
    }

    private void checkEndOfGame() {
        switch(mGameMode) {
            case "classic":
                if (!mIsFallDown) {
                    if (mFixedBlocks[1][5] != 0) {
                        mIsEndOfGame = true;
                        return;
                    }
                    for (int i = 0; i < mFixedBlocks[0].length; i++) {
                        if (mFixedBlocks[0][i] != 0) mIsEndOfGame = true;
                    }
                }
                break;
            case "openImage":
                if (!mIsFallDown) {
                    int index = 0;
                    for (int line : mFullLinesForOpenImage) {
                        if (line == 1) index++;
                    }
                    if (index >= (getLevel() + 6)) {
                        mIsEndOfGame = true;
                        mIsWin = true;
                        return;
                    }

                    if (mFixedBlocks[1][5] != 0) {
                        mIsEndOfGame = true;
                        return;
                    }
                    for (int i = 0; i < mFixedBlocks[0].length; i++) {
                        if (mFixedBlocks[0][i] != 0) mIsEndOfGame = true;
                    }
                }
                break;
        }
    }

    private boolean checkFullLines() {
        int index = 0;
        for (int i = 0; i < mFixedBlocks.length; i++) {
            for (int j = 0; j < mFixedBlocks[i].length; j++) {
                if (mFixedBlocks[i][j] != 0) index++;
            }
            if(index == mWidthGameField) {
                mFullLines[i] = 1;
                mFullLinesForOpenImage[i] = 1;
            }
            else mFullLines[i] = 0;
            index = 0;
        }

        index = 0;
        for (int fullLine : mFullLines) {
            if (fullLine == 1) index++;
        }
        return  index > 0;
    }

 /*   public void deleteFullLines() {
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
                      //  moveLinesDown(i);
                    //    removeLine(i);
                        setScore((getScore() + 15));
                        if (getScore() % 105 == 0) speedPlus();
                        break;
                    }
                }

            }
        }
        startTimer();
    }*/

    private void removeLine() {
        //  pauseGame();
       // int countFullLines = 0;
        int line;
        checkFullLines();

        for (int p = mFullLines.length - 1; p > 2; p--) {
            if (mFullLines[p] == 1) {
                line = p;

           /*     setScore((getScore() + 15));
                if (getScore() % 105 == 0) speedPlus();
                if (mGameMode.equals("classic")) {
                    if (getScore() % 105 == 0) setLevel(getLevel() + 1);
                }*/

                //  pauseGame();


                for (int i = 0; i < mListFallenFigures.size(); i++) {
                    Figure figure = mListFallenFigures.get(i);
                    // проверяем, разбивает ли линия фигуру
                    for (int j = 0; j < figure.getAllCoords().size(); j++) {
                        int y = figure.getAllCoords().get(j).y;
                        if (y == line) {
                            // создаем фигуру из первой части разбитой фигуры
                            BrokenFigure brokenFigure1;
                            ArrayList<Point> coord = new ArrayList<>();
                            for (int k = 0; k < figure.getAllCoords().size(); k++) {
                                if (figure.getAllCoords().get(k).y == line) break;
                                else coord.add(figure.getAllCoords().get(k));
                            }
                            if (coord.size() > 0) {
                                brokenFigure1 = new BrokenFigure(coord);
                                brokenFigure1.setColor(figure.getColor());
                                mListBrokenFigures.add(brokenFigure1);
                            }
                            coord.clear();

                            // создаем фигуру из второй части разбитой фигуры
                            BrokenFigure brokenFigure2;
                            for (int m = (figure.getAllCoords().size() - 1); m > -1; m--) {
                                if (figure.getAllCoords().get(m).y == line) break;
                                else coord.add(figure.getAllCoords().get(m));
                            }
                            if (coord.size() > 0) {
                                brokenFigure2 = new BrokenFigure(coord);
                                brokenFigure2.setColor(figure.getColor());
                                mListBrokenFigures.add(brokenFigure2);
                            }
                            coord.clear();

                            mListFallenFigures.set(i, null);
                            break;
                        }
                        if (j == figure.getAllCoords().size() - 1) {
                            if (y < line) {
                                mListBrokenFigures.add(figure);
                                mListFallenFigures.set(i, null);
                            }
                        }
                    }
                }

                for (int i = mListFallenFigures.size() - 1; i > -1; i--) {
                    if (mListFallenFigures.get(i) == null) {
                        mListFallenFigures.remove(i);
                    }
                }

    /*    for (int i = 0; i < mListFallenFigures.size(); i++) {
            if(mListFallenFigures.get(i) != null) {
                mListBrokenFigures.add(mListFallenFigures.get(i).clone());
            }
        }
        mListFallenFigures.clear();


        for (int i = 0; i < mListBrokenFigures.size(); i++) {
            mListFallenFigures.add(mListBrokenFigures.get(i).clone());
        }
      //  mListFallenFigures.addAll(mListBrokenFigures);
        mListBrokenFigures.clear();*/

                ////////

                //   startTimerForFallenFigures();

                mIsFallDownAllBrokenFigures = true;
                for (int i = 0; i < mFixedBlocks[line].length; i++) mFixedBlocks[line][i] = 0;

                mFullLines[line] = 0;
                setScore((getScore() + 15));
                if(getScore() > 0 ) {
                    if (getScore() % 105 == 0) speedPlus();
                    if (mGameMode.equals("classic")) {
                        if (getScore() % 105 == 0) setLevel(getLevel() + 1);
                    }
                }
                //countFullLines++;


                //  fallDownBrokenFigures();
                //   startTimer();



    /*    for (int a = 0; a < 10; a++) {

           updateFixedBlocks();


        //  for (int i = 0; i < mFixedBlocks[line].length; i++) mFixedBlocks[line][i] = 0;


           for (int i = 0; i < mListFallenFigures.size(); i++) {
               Figure figure = mListFallenFigures.get(i).clone();
               mIsFallDown = true;
///////////////
            /*   figure.setTranslate(0, 1);
               if (figure.getAllCoords().get((figure.getAllCoords().size() - 1)).y
                       == mGameField.length) {
                   mIsFallDown = false;
                   mIsRotate = false;
               }
               for (int j = 0; j < figure.getAllCoords().size(); j++) {
                   int x = figure.getAllCoords().get(j).x;
                   int y = figure.getAllCoords().get(j).y;

                   if (mFixedBlocks[y][x] != 0) {
                       mIsFallDown = false;
                       mIsRotate = false;
                   }
               }
               figure.setTranslate(0, -1);*/

        /*       int x = figure.getAllCoords().get(figure.getAllCoords().size() - 1).x;
               int y = figure.getAllCoords().get(figure.getAllCoords().size() - 1).y;

               if (y > mFixedBlocks.length - 1) continue;

               if (y == mGameField.length - 1) {
                   mIsFallDown = false;
                   mIsRotate = false;
               }
               if (y < mGameField.length - 1) {
                   if (mFixedBlocks[y + 1][x] != 0) {
                       mIsFallDown = false;
                       mIsRotate = false;
                   }
               }

               if (mIsFallDown) {
                   figure.setTranslate(0, 1);
                   mListFallenFigures.set(i, figure.clone());
                   updateFixedBlocks();

               }
           }
           mIsFallDown = false;

           updateFixedBlocks();

       }*/

            }
        }

      /*  setScore((getScore() + (15 * countFullLines)));
        if(getScore() > 0 ) {
            if (getScore() % 105 == 0) speedPlus();
            if (mGameMode.equals("classic")) {
                if (getScore() % 105 == 0) setLevel(getLevel() + 1);
            }
        }*/

        //    startTimer();

       // if (checkFullLines()) removeLine();
    }

    private void fallDownBrokenFigures() {
        //  Collections.sort(mListBrokenFigures, new SortedFiguresByPosition());

        // updateFixedBlocks();
        // setLevel(mListBrokenFigures.get(0).getAllCoords().get(0).x);
        ArrayList<Boolean> isAbilityToFiguresFallDown = new ArrayList<>();

        for (int i = 0; i < mListBrokenFigures.size(); i++) {
            Figure fig1 = mListBrokenFigures.get(i).clone();
            isAbilityToFiguresFallDown.add(true);

            mIsFallDown = true;

            fig1.setTranslate(0, 1);

         /*   int x, y;
            if (figure.getAllCoords().size() == 3) {
                x = figure.getAllCoords().get(figure.getAllCoords().size() - 2).x;
                y = figure.getAllCoords().get(figure.getAllCoords().size() - 2).y;
            } else {
                x = figure.getAllCoords().get(figure.getAllCoords().size() - 1).x;
                y = figure.getAllCoords().get(figure.getAllCoords().size() - 1).y;
            }*/

            // проверяем столкновение фигуры с другими фигурами и стенками поля

            search:  // метка дл выхода из всех вложенных циклов
            for (int j = 0; j < fig1.getAllCoords().size(); j++) {
                int x = fig1.getAllCoords().get(j).x;
                int y = fig1.getAllCoords().get(j).y;

                if (y == mGameField.length) {
                    mIsFallDown = false;
                    mIsRotate = false;
                    break;
                }

                for (int k = 0; k < mListFallenFigures.size(); k++) {
                    Figure fig2 = mListFallenFigures.get(k).clone();
                    for (int m = 0; m < fig2.getAllCoords().size(); m++) {
                        int x2 = fig2.getAllCoords().get(m).x;
                        int y2 = fig2.getAllCoords().get(m).y;
                        if (x == x2 && y == y2) {
                            mIsFallDown = false;
                            mIsRotate = false;
                            break search;
                        }
                    }
                }

                for (int n = 0; n < mListBrokenFigures.size(); n++) {
                    if (n != i) {
                        Figure fig3 = mListBrokenFigures.get(n).clone();
                        for (int p = 0; p < fig3.getAllCoords().size(); p++) {
                            int x3 = fig3.getAllCoords().get(p).x;
                            int y3 = fig3.getAllCoords().get(p).y;
                            if (x == x3 && y == y3) {
                                mIsFallDown = false;
                                mIsRotate = false;
                                break search;
                            }
                        }

                    }
                }

             /*   if (y < mGameField.length - 1) {
                    if (mFixedBlocks[y][x] != 0) {
                        mIsFallDown = false;
                        mIsRotate = false;
                    }.
                }*/
            }
            if (!mIsFallDown) {
                fig1.setTranslate(0, -1);
                mListBrokenFigures.set(i, fig1.clone());
                isAbilityToFiguresFallDown.set(i, false);
                //   mIsFallDownAllBrokenFigures = false;
            }
            else {
                mListBrokenFigures.set(i, fig1.clone());
                //    updateFixedBlocks();
                //    mIsFallDownAllBrokenFigures = true;
            }


         /*   x = figure.getAllCoords().get(figure.getAllCoords().size() - 1).x;
            y = figure.getAllCoords().get(figure.getAllCoords().size() - 1).y;*/

            //   if (y > mFixedBlocks.length - 1) continue;

         /*   if (y == mGameField.length - 1) {
                mIsFallDown = false;
                mIsRotate = false;
            }
            if (y < mGameField.length - 1) {
                if (mFixedBlocks[y + 1][x] != 0) {
                    mIsFallDown = false;
                    mIsRotate = false;
                }
            }*/

         /*   if (mIsFallDown) {
                figure.setTranslate(0, 1);
                mListBrokenFigures.set(i, figure.clone());
                updateFixedBlocks();
                mIsFallDownAllBrokenFigures = true;

            }
            else {
                mIsFallDownAllBrokenFigures = false;
            }*/
        }

        // проверяем все ли фигуры исчерпали возможность упасть
        int indx = 0;
        for (boolean isAbilityToFigureFallDown :
                isAbilityToFiguresFallDown) {
            if (!isAbilityToFigureFallDown) indx++;
        }
        if (indx == isAbilityToFiguresFallDown.size()) mIsFallDownAllBrokenFigures = false;

        if (!mIsFallDownAllBrokenFigures) {
            for (int i = 0; i < mListBrokenFigures.size(); i++) {
                mListFallenFigures.add(mListBrokenFigures.get(i).clone());
            }
            //  mListFallenFigures.addAll(mListBrokenFigures);
            mListBrokenFigures.clear();

            updateFixedBlocks();
            if (checkFullLines()) removeLine();
        }

        mIsFallDown = true;

        updateFixedBlocks();
    }

  /*  public void moveLinesDown(int line) {
        for (int j = line; j > 2; j--) {
            for (int k = (mFixedBlocks[j].length - 1); k > -1; k--) {
                mFixedBlocks[j][k] = mFixedBlocks[j - 1][k];
            }
        }
    }*/

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
        this.mSpeed = 1;
        mTimeInterval = 550;
        for (int i = 1; i < mSpeed; i++) {
            speedPlus();
        }
    }

    public void speedPlus() {
        if (!mIsEndOfGame) {
            if (getSpeed() < 10) {
                mSpeed = getSpeed() + 1;
                mTimeInterval -= 50;

                startTimer();
            }
        }
    }

    public void speedMin() {
        if (!mIsEndOfGame) {
            if(mGameMode.equals("classic")) {
                if (getSpeed() > 1 && getSpeed() > getLevel()) {
                    mSpeed = getSpeed() - 1;
                    mTimeInterval += 50;

                    startTimer();
                }
            }
            else {
                if (getSpeed() > 1) {
                    mSpeed = getSpeed() - 1;
                    mTimeInterval += 50;

                    startTimer();
                }
            }
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

    public void newGame(int level) {
        clearGameField();
       /* for (int i = 0; i < mGameField.length; i++) {
            for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = 0;
                mFixedBlocks[i][j] = 0;
            }
        }
        for (int i = 0; i < mFullLinesForOpenImage.length; i++) {
            mFullLinesForOpenImage[i] = 0;
        }*/

        setSpeed(1);
        setScore(0);
        setLevel(level);
        mIsEndOfGame = false;
        mIsWin = false;
        mListNextFigures.clear();
        mListFallenFigures.clear();
        mListBrokenFigures.clear();
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

    public void readFromTableBorderGameField() {
        for (int i = 0; i < mFullLinesForOpenImage.length; i++) {
            mFullLinesForOpenImage[i] = 0;
        }

        Cursor c = mDatabase.query(BorderGameFieldTable.NAME, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            List<Integer> LinesBorder = new ArrayList<>();
            int line_ColIndex = c.getColumnIndex(BorderGameFieldTable.Cols.BLOCK);

            do {
                int line = c.getInt(line_ColIndex);
                LinesBorder.add(line);

            } while (c.moveToNext());

            if (LinesBorder.size() == mFullLinesForOpenImage.length) {
                for(int i = 0; i < mFullLinesForOpenImage.length; i++) {
                    mFullLinesForOpenImage[i] = LinesBorder.get(i);
                }
            }
            LinesBorder.clear();
        }

        c.close();

    }

    public void readFromTableGameField() {

        mListFallenFigures.clear();
        // делаем запрос всех данных из таблицы GameFieldForOpenImage, получаем Cursor
        Cursor c = mDatabase.query(mDBNameTable, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int coord1_X_ColIndex = c.getColumnIndex(mDBCoord1_X);
            int coord1_Y_ColIndex = c.getColumnIndex(mDBCoord1_Y);
            int coord2_X_ColIndex = c.getColumnIndex(mDBCoord2_X);
            int coord2_Y_ColIndex = c.getColumnIndex(mDBCoord2_Y);
            int coord3_X_ColIndex = c.getColumnIndex(mDBCoord3_X);
            int coord3_Y_ColIndex = c.getColumnIndex(mDBCoord3_Y);
            int coord4_X_ColIndex = c.getColumnIndex(mDBCoord4_X);
            int coord4_Y_ColIndex = c.getColumnIndex(mDBCoord4_Y);

            int color_ColIndex = c.getColumnIndex(mDBColor);

            do {
                ArrayList<Point> coords = new ArrayList<>();
                // получаем значения по номерам столбцов
                int coordX = c.getInt(coord1_X_ColIndex);
                int coordY = c.getInt(coord1_Y_ColIndex);
                if ( (coordX >= 0 && coordX < mWidthGameField)
                        && (coordY >= 0 && coordY < mHeightGameField)) {
                    coords.add(new Point(coordX, coordY));

                }
                coordX = c.getInt(coord2_X_ColIndex);
                coordY = c.getInt(coord2_Y_ColIndex);
                if ( (coordX >= 0 && coordX < mWidthGameField)
                        && (coordY >= 0 && coordY < mHeightGameField)) {
                    coords.add(new Point(coordX, coordY));

                }
                coordX = c.getInt(coord3_X_ColIndex);
                coordY = c.getInt(coord3_Y_ColIndex);
                if ( (coordX >= 0 && coordX < mWidthGameField)
                        && (coordY >= 0 && coordY < mHeightGameField)) {
                    coords.add(new Point(coordX, coordY));

                }
                coordX = c.getInt(coord4_X_ColIndex);
                coordY = c.getInt(coord4_Y_ColIndex);
                if ( (coordX >= 0 && coordX < mWidthGameField)
                        && (coordY >= 0 && coordY < mHeightGameField)) {
                    coords.add(new Point(coordX, coordY));

                }
                int color = c.getInt(color_ColIndex);
                BrokenFigure figure = new BrokenFigure(coords);
                figure.setColor(color);
                mListFallenFigures.add(figure.clone());

                coords.clear();

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }

        c.close();

    }

    public boolean isSaveGame() {
        try {
            // делаем запрос всех данных из таблицы GameFieldTable одного из режимов игры, получаем Cursor
            Cursor c = mDatabase.query(mDBNameTable, null, null, null, null, null, null);

            // ставим позицию курсора на первую строку выборки
            // если в выборке нет строк, то false

            if (c.moveToFirst()) {
                int coord1_X_ColIndex = c.getColumnIndex(mDBCoord1_X);

             //   try {
                boolean result = c.getInt(coord1_X_ColIndex) >= 0
                        && c.getInt(coord1_X_ColIndex) < mWidthGameField;
                c.close();
                return result;
                 /*   return c.getInt(coord1_X_ColIndex) >= 0
                            && c.getInt(coord1_X_ColIndex) < mWidthGameField;*/
             /*   } catch (Exception e) {
                    return false;
                } finally {
                    c.close();
                }*/
            }
            c.close();
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean saveGameField(List<Figure> figures) {
        try {
            if (!figures.isEmpty()) {
                ContentValues values = new ContentValues();
                deleteAllRowsInTableGameField();
                for (int i = 0; i < figures.size(); i++) {
                    Figure figure = figures.get(i).clone();
                    switch (figure.getAllCoords().size()) {
                        case 1:
                            values.put(mDBCoord1_X, figure.getAllCoords().get(0).x);
                            values.put(mDBCoord1_Y, figure.getAllCoords().get(0).y);
                            values.put(mDBCoord2_X, -1);
                            values.put(mDBCoord2_Y, -1);
                            values.put(mDBCoord3_X, -1);
                            values.put(mDBCoord3_Y, -1);
                            values.put(mDBCoord4_X, -1);
                            values.put(mDBCoord4_Y, -1);
                            break;
                        case 2:
                            values.put(mDBCoord1_X, figure.getAllCoords().get(0).x);
                            values.put(mDBCoord1_Y, figure.getAllCoords().get(0).y);
                            values.put(mDBCoord2_X, figure.getAllCoords().get(1).x);
                            values.put(mDBCoord2_Y, figure.getAllCoords().get(1).y);
                            values.put(mDBCoord3_X, -1);
                            values.put(mDBCoord3_Y, -1);
                            values.put(mDBCoord4_X, -1);
                            values.put(mDBCoord4_Y, -1);
                            break;
                        case 3:
                            values.put(mDBCoord1_X, figure.getAllCoords().get(0).x);
                            values.put(mDBCoord1_Y, figure.getAllCoords().get(0).y);
                            values.put(mDBCoord2_X, figure.getAllCoords().get(1).x);
                            values.put(mDBCoord2_Y, figure.getAllCoords().get(1).y);
                            values.put(mDBCoord3_X, figure.getAllCoords().get(2).x);
                            values.put(mDBCoord3_Y, figure.getAllCoords().get(2).y);
                            values.put(mDBCoord4_X, -1);
                            values.put(mDBCoord4_Y, -1);
                            break;
                        case 4:
                            values.put(mDBCoord1_X, figure.getAllCoords().get(0).x);
                            values.put(mDBCoord1_Y, figure.getAllCoords().get(0).y);
                            values.put(mDBCoord2_X, figure.getAllCoords().get(1).x);
                            values.put(mDBCoord2_Y, figure.getAllCoords().get(1).y);
                            values.put(mDBCoord3_X, figure.getAllCoords().get(2).x);
                            values.put(mDBCoord3_Y, figure.getAllCoords().get(2).y);
                            values.put(mDBCoord4_X, figure.getAllCoords().get(3).x);
                            values.put(mDBCoord4_Y, figure.getAllCoords().get(3).y);
                            break;
                    }

                    values.put(mDBColor, figure.getColor());

                    mDatabase.insert(mDBNameTable, null, values);
                    values.clear();
                }

                if(mGameMode.equals("openImage")) {
                    deleteAllRowsInTableBorderGameField();
                    for (int line : mFullLinesForOpenImage) {
                        values.put(BorderGameFieldTable.Cols.BLOCK, line);
                        mDatabase.insert(BorderGameFieldTable.NAME, null, values);
                        values.clear();
                    }
                }

                return true;
            }
            else {
                deleteAllRowsInTableGameField();
            }

        }
        catch (Exception e) {
            return false;
        }

        return false;
    }

    public boolean loadGameField() {
        try {
            readFromTableBorderGameField();
            readFromTableGameField();
            updateFixedBlocks();
            mIsFallDownAllBrokenFigures = false;
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void deleteAllRowsInTableRecords() {
        mDatabase.delete(RecordsTable.NAME, null, null);
    }

    public void deleteAllRowsInTableGameField() {
        mDatabase.delete(mDBNameTable, null, null);
    }

    public void deleteAllRowsInTableBorderGameField() {
        mDatabase.delete(BorderGameFieldTable.NAME, null, null);
    }




}

