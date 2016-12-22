package com.sibich.tetris;

import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by Sibic_000 on 09.10.2016.
 */


public abstract class Figure implements Cloneable {

    private Point mPosition = new Point();
    private int mColor;

    private int mCurOrientationIndx;

    public Figure()
    {
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

    // локальные координаты не меняются в процессе падения фигуры
    // их можно использовать при отрисовке следующей фигуры
    public abstract Point[] getLocalCoord();

    // глобальные координаты зависят от поворота и позиции фигуры
    public Point[] getTransformedCoord()
    {
        Point[] localCoord = getLocalCoord();
        Point[] globalCoord = new Point[localCoord.length];

        Matrix3 transform = Matrix3.translate(mPosition.x, mPosition.y)
                .mul(Matrix3.rotate(getOrientation()));

        for(int i = 0; i < localCoord.length; i++)
        {
            globalCoord[i] = transform.mul(localCoord[i]);
        }

        return globalCoord;
    }

    // Встречал где-то в логике сброс значений фигуры, по-моему
    void reset()
    {
        mCurOrientationIndx = 0;
        mPosition = new Point();
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
}
