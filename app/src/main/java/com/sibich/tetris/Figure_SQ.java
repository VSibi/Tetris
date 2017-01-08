package com.sibich.tetris;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Sibic_000 on 23.10.2016.
 */
public class Figure_SQ extends Figure {

    private ArrayList<Point> mAllCoords = new ArrayList<>();

    private Point mCoord1 = new Point();
    private Point mCoord2 = new Point();
    private Point mCoord3 = new Point();
    private String mStateOfRotation;

    public Figure_SQ() {
        super.setBasicPoint(super.getBasicPoint().x + 1, super.getBasicPoint().y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));

        mAllCoords.add(mCoord1);
        mAllCoords.add(mCoord2);
        mAllCoords.add(mCoord3);
        mAllCoords.add(getBasicPoint());

        mStateOfRotation = "Normal";
    }

    public void setTranslate(int x, int y) {
        for(int i = 0; i < mAllCoords.size(); i++) {
            Point coord = mAllCoords.get(i);
            coord.x = coord.x + x;
            coord.y = coord.y + y;
            mAllCoords.set(i, coord);
        }
    }

    public void setPosition(int x, int y) {
        super.setBasicPoint(x + 1, y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));

        mAllCoords.set(0, mCoord1);
        mAllCoords.set(1, mCoord2);
        mAllCoords.set(2, mCoord3);
        mAllCoords.set(3, getBasicPoint());
    }

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public void rotate() {}

    public ArrayList<Point> getAllCoords() {
        return mAllCoords;
    }

    public Figure_SQ clone() {
        Figure_SQ o = (Figure_SQ) super.clone();
        o.mAllCoords = new ArrayList<>();
        for (int i = 0; i < mAllCoords.size(); i++) {
            o.mAllCoords.add(new Point(mAllCoords.get(i).x, mAllCoords.get(i).y));
        }

        return o;
    }





  /*  private Point mCoord1 = new Point();
    private Point mCoord2 = new Point();
    private Point mCoord3 = new Point();
    private String mStateOfRotation;


    private Point [] mAllCoord = {
           mCoord1, mCoord2, mCoord3,  super.getBasicPoint()
    };

    public Figure_SQ() {
        super.setBasicPoint(super.getBasicPoint().x + 1, super.getBasicPoint().y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));
        mStateOfRotation = "Normal";
    }


    public void setTranslate(int x, int y) {
        for (int i = 0; i < mAllCoord.length; i++) {
            mAllCoord[i].x = mAllCoord[i].x + x;
            mAllCoord[i].y = mAllCoord[i].y + y;
        }
    }

    public void setPosition(int x, int y) {
        super.setBasicPoint(x + 1, y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));
    }

    public void rotate() {}

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public Point[] getAllCoord() {
        return mAllCoord;
    }*/


}
