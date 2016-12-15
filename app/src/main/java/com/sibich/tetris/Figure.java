package com.sibich.tetris;

import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by Sibic_000 on 09.10.2016.
 */
public class Figure implements Cloneable {

    private Point mBasicPoint = new Point();
    private int mColor;

    private Point [] mAllCoord;
    private String mStateOfRotation;

    public Figure() {}

    public Point getBasicPoint() {
        return mBasicPoint;
    }

    public void setBasicPoint(int x, int y) {
        mBasicPoint.set(x, y);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public void setTranslate(int x, int y) {}

    public void setPosition (int x, int y) {}

    public void rotate() {}

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public Point[] getAllCoord() {
        return mAllCoord;
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
