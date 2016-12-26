package com.sibich.tetris;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sibic_000 on 09.10.2016.
 */


public abstract class Figure implements Cloneable {

    private Point mPosition = new Point();
    private int mColor;

    private int mCurOrientationIndx = 0;
    private Point[] mLocalCoord = new Point[0];

    public Figure() {}

    public void setLocalCoords(Point[] coords)
    {
        mLocalCoord = coords;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public Point getPosition() {
        return mPosition;
    }

    public void translate(int x, int y)
    {
        mPosition.x += x;
        mPosition.y += y;
    }

    public void setPosition (int x, int y)
    {
        mPosition.x = x;
        mPosition.y = y;
    }

    // передаёт массив всех возможных углов поворота для данной фигуры
    public abstract Matrix3.RotationDegree[] getAvailableOrientations();

    protected void setOrientationIndx(int indx)
    {
        Debug.Assert(indx < getAvailableOrientations().length);
        mCurOrientationIndx = indx;
    }

    protected int getOrientationIndx()
    {
        return mCurOrientationIndx;
    }

    public void rotate()
    {
        int indx = getOrientationIndx();
        indx++;

        if(indx >= getAvailableOrientations().length)
            indx = 0;

        setOrientationIndx(indx);
    }

    public Matrix3.RotationDegree getOrientation()
    {
        return getAvailableOrientations()[mCurOrientationIndx];
    }

    public Matrix3 getTransformMatrix()
    {
        return Matrix3.translate(mPosition.x, mPosition.y)
                .mul(Matrix3.rotate(getOrientation()));
    }

    public Matrix3 getTransformMatrixInverse()
    {
        return Matrix3.rotate(Matrix3.invertAngle(getOrientation())).mul(
                Matrix3.translate(-mPosition.x, -mPosition.y));
    }

    // глобальные координаты зависят от поворота и позиции фигуры
    public Point[] getTransformedCoord()
    {
        Point[] localCoord = getLocalCoord();
        Point[] globalCoord = new Point[localCoord.length];

        Matrix3 transform = getTransformMatrix();

        for(int i = 0; i < localCoord.length; i++)
        {
            globalCoord[i] = transform.mul(localCoord[i]);
        }

        return globalCoord;
    }

    public Point[] getLocalBoundingBox()
    {
        Debug.Assert(!empty());

        Point[] localCoords = getLocalCoord();

        Point p1 = new Point(localCoords[0]);
        Point p2 = new Point(localCoords[0]);

        for(int i = 0; i < localCoords.length; i++) {
            Point coord = localCoords[i];
            if(p1.x > coord.x)
                p1.x = coord.x;
            if(p1.y > coord.y)
                p1.y = coord.y;
            if(p2.x < coord.x)
                p2.x = coord.x;
            if(p2.y < coord.y)
                p2.y = coord.y;
        }

        return new Point[]{p1, p2};
    }

    public Point[] getTransformedBoundingBox()
    {
        Debug.Assert(!empty());

        Point[] localCoords = getLocalCoord();

        Matrix3 transform = getTransformMatrix();

        Point p1 = new Point(localCoords[0]);
        p1 = transform.mul(p1);

        Point p2 = new Point(localCoords[0]);
        p2 = transform.mul(p2);

        for(int i = 0; i < localCoords.length; i++) {
            Point coord = new Point(localCoords[i]);

            coord = transform.mul(coord);

            if(p1.x > coord.x)
                p1.x = coord.x;
            if(p1.y > coord.y)
                p1.y = coord.y;
            if(p2.x < coord.x)
                p2.x = coord.x;
            if(p2.y < coord.y)
                p2.y = coord.y;
        }

        return new Point[]{p1, p2};
    }

    // Встречал где-то в логике сброс значений фигуры, по-моему
    void reset()
    {
        mCurOrientationIndx = 0;
        mPosition = new Point(0, 0);
    }

    public Figure clone() {
        Figure o = null;
        try {
            o = (Figure)super.clone();
            o.mPosition = new Point(mPosition.x, mPosition.y);
            o.mLocalCoord = new Point[mLocalCoord.length];
            for(int i = 0; i < mLocalCoord.length; i++)
                o.mLocalCoord[i] = new Point(mLocalCoord[i].x, mLocalCoord[i].y);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    private void removeLocalCoord(int x, int y)
    {
        String coordList = "";
        for(Point c : mLocalCoord)
        {
            coordList = coordList + " (" + c.x + ", " + c.y + ")";
        }
        Debug.Log("Figure: removeLocalCoord(" + x + ", " + y + ")" +
                "; CURRENT COORDS:" + coordList);

        Point[] newCoords = new Point[mLocalCoord.length - 1];
        int indx = 0;
        for(int i = 0; i < mLocalCoord.length; i++)
        {
            Point c = mLocalCoord[i];
            if(c.x == x && c.y == y)
                continue;
            newCoords[indx] = c;
            indx++;
        }

        mLocalCoord = newCoords;
    }

    public void removeGlobalCoord(int x, int y)
    {
        /*Point testPoint = new Point(2, 19);
        Point translatedTestPoint = Matrix3.translate(-2, -17).mul(testPoint);
        Point rotatedTestPoint = Matrix3.rotate(Matrix3.RotationDegree.DEGREE_270)
                .mul(translatedTestPoint);
        Point transformedAtOncePoint = Matrix3.rotate(Matrix3.RotationDegree.DEGREE_270)
                .mul(Matrix3.translate(-2, -17)).mul(testPoint);*/

        String coordList = "";
        Point[] globalCoords = getTransformedCoord();
        for(Point c : globalCoords)
        {
            coordList = coordList + " (" + c.x + ", " + c.y + ")";
        }
        Debug.Log("Figure: removeGlobalCoord(" + x + ", " + y + ")" +
                "; CURRENT GLOBAL COORDS:" + coordList);
        Debug.Log("Pos: (" + mPosition.x + ", " + mPosition.y
                + "); Orientation: " + getOrientation());

        Point p = new Point(x, y);
        Matrix3 invTransform = getTransformMatrixInverse();
        p = invTransform.mul(p);

        removeLocalCoord(p.x, p.y);
    }

    public boolean empty()
    { return mLocalCoord.length == 0;}

    public Point[] getLocalCoord() { return mLocalCoord; }

    public boolean checkIntegrity()
    {
        // should be valid for our figures

        Debug.Assert(!empty());

        if(mLocalCoord.length == 1)
            return true;

        for(Point p1 : mLocalCoord)
        {
            boolean hasNeighbours = false;
            for(Point p2 : mLocalCoord)
            {
                int dist_x = Math.abs(p1.x - p2.x);
                int dist_y = Math.abs(p1.y - p2.y);

                if(dist_x == 0 && dist_y == 1
                        || dist_x == 1 && dist_y == 0)
                {
                    hasNeighbours = true;
                    break;
                }
            }
            if(!hasNeighbours)
                return false;
        }

        return true;
    }

    public ArrayList<BrokenFigure> breakFigure()
    {
        // чит: наши фигуры устроены так, что без сохранения целостности оставшиеся клетки
        // либо группируются парой, либо находятся по отдельнсти
        Debug.Assert(!checkIntegrity());
        Debug.Assert(!empty());

        String coordList = "";
        for(Point coord : mLocalCoord)
        {
            coordList = coordList + " (" + coord.x + ", " + coord.y + ")";
        }
        Debug.Log("Figure: breakFigure(), LOCAL COORDS:" + coordList);

        ArrayList<BrokenFigure> ret = new ArrayList<>();

        boolean[] processed = new boolean[mLocalCoord.length];
        for(int i = 0; i < processed.length; i++)
            processed[i] = false;

        for(int i = 0; i < mLocalCoord.length; i++)
        {
            Point p1 = new Point(mLocalCoord[i]);
            Point p2 = null;

            if(processed[i])
                continue;

            for(int j = i + 1; j < mLocalCoord.length; j++)
            {
                if(processed[j])
                    continue;

                int dist_x = Math.abs(p1.x - mLocalCoord[j].x);
                int dist_y = Math.abs(p1.y - mLocalCoord[j].y);

                if(dist_x == 0 && dist_y == 1
                        || dist_x == 1 && dist_y == 0)
                {
                    processed[j] = true;
                    p2 = new Point(mLocalCoord[j]);
                    break;
                }
            }

            processed[i] = true;

            if(p2 == null) {
                Debug.Log("BrokenFigure: (" + p1.x + ", " + p1.y + ")");
                ret.add(new BrokenFigure(new Point[]{p1},
                        getPosition(), getOrientation()));
            }
            else {
                Debug.Log("BrokenFigure: (" + p1.x + ", " + p1.x
                        + "), (" + p2.x + ", " + p2.y + ")");
                ret.add(new BrokenFigure(new Point[]{p1, p2},
                        getPosition(), getOrientation()));
            }
        }

        return ret;
    }
}
