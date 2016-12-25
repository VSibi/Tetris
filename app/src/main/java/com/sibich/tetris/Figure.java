package com.sibich.tetris;

import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by Sibic_000 on 09.10.2016.
 */


public abstract class Figure implements Cloneable {

    private Point mPosition = new Point();
    private int mColor;

    private int mCurOrientationIndx = 0;
    private Point[] mLocalCoord = null;

    public Figure()
    {
        mLocalCoord = genStartLocalCoord();
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

    public void rotate()
    {
        mCurOrientationIndx++;
        if(mCurOrientationIndx >= getAvailableOrientations().length)
            mCurOrientationIndx = 0;
    }

    public Matrix3.RotationDegree getOrientation()
    {
        return getAvailableOrientations()[mCurOrientationIndx];
    }

    // не меняются в процессе падения фигуры
    // используются только для инита
    public abstract Point[] genStartLocalCoord();

    public Matrix3 getTransformMatrix()
    {
        // TODO
        return Matrix3.translate(mPosition.x, mPosition.y);
                //.mul(Matrix3.rotate(getOrientation()));
    }

    public Matrix3 getTransformMatrixInverse()
    {
        // TODO
        return Matrix3.translate(-mPosition.x, -mPosition.y);
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
        Point[] bb = getLocalBoundingBox();
        Matrix3 transform = getTransformMatrix();

        for(int i = 0; i < bb.length; i++)
            bb[i] = transform.mul(bb[i]);

        return bb;
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
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    private void removeLocalCoord(int x, int y)
    {
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
    }

    public void removeGlobalCoord(int x, int y)
    {
        Point p = new Point(x, y);
        Matrix3 invTransform = getTransformMatrixInverse();
        p = invTransform.mul(p);

        removeLocalCoord(p.x, p.y);
    }

    public Point[] getLocalCoord() { return mLocalCoord; }
}
