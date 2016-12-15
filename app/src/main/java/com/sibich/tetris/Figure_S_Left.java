package com.sibich.tetris;

import android.graphics.Point;

/**
 * Created by Sibic_000 on 07.11.2016.
 */
public class Figure_S_Left extends Figure {

    private Point mCoord1 = new Point();
    private Point mCoord2 = new Point();
    private Point mCoord3 = new Point();
    private String mStateOfRotation;



    private Point [] mAllCoord = {
            mCoord1, mCoord2, mCoord3, super.getBasicPoint()
    };

    public Figure_S_Left() {
        super.setBasicPoint(super.getBasicPoint().x + 1, super.getBasicPoint().y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y));
        mStateOfRotation = "Normal";
    }


    public void setTranslate(int x, int y) {
        for (int i = 0; i < mAllCoord.length; i++) {
            mAllCoord[i].x += x;
            mAllCoord[i].y += y;
        }
    }

    public void setPosition(int x, int y) {
        super.setBasicPoint(x + 1, y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y));

    }

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public void rotate() {
        switch (mStateOfRotation) {
            case "Normal":
                super.setBasicPoint(super.getBasicPoint().x, super.getBasicPoint().y + 1);
                mCoord1.set(super.getBasicPoint().x + 1, super.getBasicPoint().y - 2);
                mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
                mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y - 1));
                mStateOfRotation = "Rotate_90_degrees";
                break;
            case "Rotate_90_degrees" :
                super.setBasicPoint(super.getBasicPoint().x, super.getBasicPoint().y - 1);
                mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
                mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
                mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y));
                mStateOfRotation = "Normal";
                break;
        }
    }


    public Point[] getAllCoord() {
        return mAllCoord;
    }


}
