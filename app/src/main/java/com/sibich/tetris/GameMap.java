package com.sibich.tetris;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by slavust on 12/25/16.
 */

public class GameMap
{
    private static int sMapWidth = 10;
    private static int sMapHeight = 20;

    boolean mGameOver = false;

    private Figure mCurrentFigure = null;
    private int mCurrentFigureIndx = 0;

    private Figure mNextFigure = null;
    private int[][] mNextFigureField = new int[4][4];
    private int mNextFigureIndx = 0;

    private Figure[] mAvailableFigures = {new Figure_I(), new Figure_S_Left(),
            new Figure_SQ()/*, new Figure_T()*/};
    private Random mRandom = new Random();

    private int[][] mFixedBlocks = new int[sMapWidth][sMapHeight]; // содержат индексы зафиксированных фигур
    private int mRemovedLineCount = 0;


    public class FallenFigure
    {
        public int mIndx;
        public Figure mFigure;

        public FallenFigure(int indx, Figure figure)
        {
            mIndx = indx;
            mFigure = figure;
        }
    }

    private boolean mInLinesConstruction = false;
    // зафиксированные фигуры
    private ArrayList<FallenFigure> mFallenFigures = new ArrayList<FallenFigure>();

    // сравнитель зафиксированных фигур для вычисления порядка обновления фигур при удалении линии
    private static Comparator<FallenFigure> FallenFigureComparator = new Comparator<FallenFigure>()
    {
        public int compare(FallenFigure f1, FallenFigure f2)
        {
            // сделано предположение, что одна фигура не может быть прямо над другой
            // (т.е. с одинаковой координатой x, но разными y) и прямо под ней одновременно

            Point[] points1 = f1.mFigure.getTransformedCoord();
            Point[] points2 = f2.mFigure.getTransformedCoord();

            // одна фигура в списке находится раньше другой,
            // если какая-либо её клетка с координатой x
            // ниже какой-либо клетки второй фигуры с такой же координатой x
            // (тогда при симуляции сначала опустим данную фигуру, а затем следующую
            // без наступления коллизии)
            int compare_result = 0;
            for(int i = 0; i < points1.length && compare_result == 0; i++)
            {
                int x1 = points1[i].x;
                int y1 = points1[i].y;

                for(int j = 0; j < points2.length; j++)
                {
                    int x2 = points2[j].x;
                    int y2 = points2[j].y;

                    if(x1 == x2)
                    {
                        compare_result = y1 > y2 ? 1 : -1;
                        break;
                    }
                }
            }

            // фигуры не находятся друг над другом
            // первая в списке та, у которой минимальная координата y меньше.
            // Если минимальные у одинаковы, то та, у которой максимальная у меньше.
            if(compare_result == 0)
            {
                Point[] bb1 = f1.mFigure.getTransformedBoundingBox();
                Point[] bb2 = f1.mFigure.getTransformedBoundingBox();

                if(bb1[1].y != bb2[1].y)
                {
                    compare_result = bb1[1].y > bb2[1].y ? 1 : -1;
                }
                else
                {
                    compare_result = bb1[0].y == bb2[0].y ? 0 : (bb1[0].y > bb2[0].y ? 1 : -1);
                }
            }

            return compare_result;/* * -1;*/
        }
    };

    public GameMap()
    {
        ClearField();
        genNextFigure();
    }

    public int[][] getFixedField() {return cloneField(mFixedBlocks);}

    // returns blocks grouped by indices
    // index should be used to externally generate colors;
    // -1 is empty cell;
    public int[][] getDrawableField()
    {
        int[][] field = cloneField(mFixedBlocks);

        if(mCurrentFigure != null)
        {
            Point[] currentFigurePoints = mCurrentFigure.getTransformedCoord();

            for (Point p : currentFigurePoints) {
                if (p.y >= 0)
                    field[p.x][p.y] = mCurrentFigureIndx;
            }
        }

        return field;
    }

    // index in field should be used to externally generate color;
    // -1 is empty cell
    public int[][] getNextFigureDrawableField()
    {
        return cloneField(mNextFigureField);
    }

    public boolean isGameOver()
    {
        return mGameOver;
    }

    public void simulateStep()
    {
        Debug.Assert(!mGameOver);

        if(mInLinesConstruction)
        {
            boolean somethingFalled = simulateFallenFigures();
            mInLinesConstruction = somethingFalled;
            if(!somethingFalled)
                mInLinesConstruction = removeFullLines();
            return;
        }

        if(mCurrentFigure == null)
        {
            throwNextFigure();
        }

        Figure transformedFigure = mCurrentFigure.clone();
        transformedFigure.translate(0, 1);

        if(checkIntersection(transformedFigure, -1))
        {
            mGameOver = checkGameOver();
            fixCurrentFigure();
            mCurrentFigure = null;
            mInLinesConstruction = true;
        }
        else
        {
            mCurrentFigure = transformedFigure;
        }
    }

    public int getRemovedLineCount()
    {
        return mRemovedLineCount;
    }

    public void moveFigureLeft()
    {
        if(mCurrentFigure == null)
            return;

        Figure transformedFigure = mCurrentFigure.clone();
        transformedFigure.translate(-1, 0);
        if(!checkIntersection(transformedFigure, -1))
            mCurrentFigure = transformedFigure;
    }

    public void moveFigureRight()
    {
        if(mCurrentFigure == null)
            return;

        Figure transformedFigure = mCurrentFigure.clone();
        transformedFigure.translate(1, 0);
        if(!checkIntersection(transformedFigure, -1))
            mCurrentFigure = transformedFigure;
    }

    public void rotateFigure()
    {
        if(mCurrentFigure == null)
            return;

        Figure transformedFigure = mCurrentFigure.clone();
        transformedFigure.rotate();
        if(!checkIntersection(transformedFigure, -1))
            mCurrentFigure = transformedFigure;
    }

    private boolean checkGameOver()
    {
        Point[] curFigureBB = mCurrentFigure.getTransformedBoundingBox();
        return curFigureBB[0].y < 0;
    }

    private boolean simulateFallenFigures()
    {
        boolean somethingFalledDown = false;

        Collections.sort(mFallenFigures, FallenFigureComparator);

        for(int i = 0 ; i < mFallenFigures.size(); i++)
        {
            FallenFigure ff = mFallenFigures.get(i);
            Figure fig = ff.mFigure.clone();
            fig.translate(0, 1);
            if(!checkIntersection(fig, ff.mIndx))
            {
                ff.mFigure = fig;
                UpdateField();
                somethingFalledDown = true;
            }
        }

        return somethingFalledDown;
    }

    private void throwNextFigure()
    {
        Debug.Log("Map: throwNextFigure()");

        Point[] localBB = mNextFigure.getLocalBoundingBox();
        int width = localBB[0].x - localBB[1].x;
        int pos_x = (sMapWidth - width) / 2 - localBB[0].x;
        int pos_y = -localBB[1].y;
        mNextFigure.setPosition(pos_x, pos_y);

        mCurrentFigure = mNextFigure;
        mCurrentFigureIndx = mNextFigureIndx;

        genNextFigure();
    }

    private void genNextFigure()
    {
        Debug.Log("Map: genNextFigure()");

        mNextFigureIndx = genFigureIndx();

        int indx = mRandom.nextInt(mAvailableFigures.length);
        mNextFigure = mAvailableFigures[indx];

        Point[] bb = mNextFigure.getLocalBoundingBox();
        int offset_x = -bb[0].x;
        int offset_y = -bb[0].y;

        int size_x = bb[1].x - bb[0].x;
        int size_y = bb[1].y - bb[0].y;

        Debug.Assert(size_x <= 4 && size_y <= 4);

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                mNextFigureField[i][j] = -1;
            }
        }

        Point[] figureLocalPoints = mNextFigure.getLocalCoord();
        for(int i = 0; i < figureLocalPoints.length; i++)
        {
            Point p = figureLocalPoints[i];
            mNextFigureField[p.x + offset_x][p.y + offset_y] = mCurrentFigureIndx + 1;
        }
    }

    private void fixCurrentFigure()
    {
        Debug.Log("Map: fixCurrentFigure()");
        mFallenFigures.add(new FallenFigure(mCurrentFigureIndx, mCurrentFigure));
        UpdateField();
    }

    private boolean removeFullLines()
    {
        Debug.Log("Map: removeFullLines()");

        boolean lineRemoved = false;

        for(int y = sMapHeight - 1; y >= 0; y--)
        {
            boolean lineIsFull = true;
            boolean lineIsEmpty = true;

            for(int x = 0; x < sMapWidth; x++)
            {
                if(mFixedBlocks[x][y] == -1)
                {
                    lineIsFull = false;
                }
                else
                {
                    lineIsEmpty = false;
                }
            }

            if(lineIsFull)
            {
                removeLine(y);
                lineRemoved = true;
            }
            if(lineIsEmpty)
                break;
        }

        return lineRemoved;
    }

    private void removeBlock(int x, int y)
    {
        Debug.Log("Map: removeBlock(" + x + ", " + y + ")");

        int figureIndx = mFixedBlocks[x][y];
        for(int i = 0; i < mFallenFigures.size(); i++)
        {
            FallenFigure ff = mFallenFigures.get(i);
            if(ff.mIndx == figureIndx)
            {
                ff.mFigure.removeGlobalCoord(x, y);
                mFixedBlocks[x][y] = -1;
                if(ff.mFigure.empty())
                    mFallenFigures.remove(i);
                else if(!ff.mFigure.checkIntegrity())
                {
                    Debug.Log("Map: breaking figure #" + ff.mIndx);
                    ArrayList<BrokenFigure> newFigures = ff.mFigure.breakFigure();
                    mFallenFigures.remove(i);
                    for(Figure brokenFigure : newFigures)
                    {
                        mFallenFigures.add(new FallenFigure(genFigureIndx(), brokenFigure));
                    }
                    UpdateField();
                }

                return;
            }
        }

        Debug.Log("Map: removeBlock() error: figure #" + mFixedBlocks[x][y] + " not found!");
        Debug.Assert(false);
    }

    private void removeLine(int lineIndx)
    {
        Debug.Assert(lineIndx >= 0 && lineIndx < sMapHeight);

        Debug.Log("Map: removeLine (" + lineIndx + ")");

        for(int x = 0; x < sMapWidth; x++)
        {
            removeBlock(x, lineIndx);
        }

        mRemovedLineCount++;
    }

    private void ClearField()
    {
        for(int x = 0; x < sMapWidth; x++)
        {
            for(int y = 0; y < sMapHeight; y++)
            {
                mFixedBlocks[x][y] = -1;
            }
        }
    }

    // field should be updated before intersection check
    private void UpdateField()
    {
        ClearField();
        for(FallenFigure ff : mFallenFigures)
        {
            Point[] figurePoints = ff.mFigure.getTransformedCoord();

            for(Point p : figurePoints)
            {
                if(p.x < 0 || p.x >= sMapWidth || p.y < 0)
                    continue;
                mFixedBlocks[p.x][p.y] = ff.mIndx;
            }
        }
    }

    // checks if passed figure doesn't intersect with fixed blocks or field bounds
    private boolean checkIntersection(Figure figure, int additionalIgnoreIndx)
    {
        // check map bounds
        Point[] bb = figure.getTransformedBoundingBox();
        if(bb[0].x < 0 || bb[1].x >= sMapWidth || bb[1].y >= sMapHeight)
        {
            Debug.Log("Map: collision");
            return true;
        }

        //check if figure points intersect with fixed blocks
        Point[] figurePoints = figure.getTransformedCoord();
        for(int i = 0; i < figurePoints.length; i++)
        {
            Point p = figurePoints[i];
            if(p.y < 0) // только что брошенная фигура может находиться за пределами игрового поля)
                continue;

            if(mFixedBlocks[p.x][p.y] != -1 && mFixedBlocks[p.x][p.y] != additionalIgnoreIndx) {
                Debug.Log("Map: collision");
                return true;
            }
        }

        return false;
    }

    private static int[][] cloneField(int[][] array)
    {
        int[][] ret = new int[array.length][array[0].length];
        for(int i = 0; i < array.length; i++)
        {
            ret[i] = array[i].clone();
        }
        return ret;
    }

    private int mFigureIndexCount = 0;
    private int genFigureIndx()
    {
        int indx = mFigureIndexCount;
        mFigureIndexCount++;
        return indx;
    }
}
