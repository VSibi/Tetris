package com.sibich.tetris;

/**
 * Created by Sibic_000 on 09.10.2016.
 */
import android.graphics.Point;

import java.util.ArrayList;


public class Figure_I extends Figure{

    private ArrayList<Point> mAllCoords = new ArrayList<>();

    private Point mCoord1 = new Point();
    private Point mCoord2 = new Point();
    private Point mCoord3 = new Point();
    private String mStateOfRotation;

    public Figure_I() {
        super.setBasicPoint(super.getBasicPoint().x + 3, super.getBasicPoint().y + 1);
        mCoord1.set(super.getBasicPoint().x - 3, super.getBasicPoint().y);
        mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
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
        super.setBasicPoint(x + 3, y + 1);
        mCoord1.set(super.getBasicPoint().x - 3, super.getBasicPoint().y);
        mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
        mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));

        mAllCoords.set(0, mCoord1);
        mAllCoords.set(1, mCoord2);
        mAllCoords.set(2, mCoord3);
        mAllCoords.set(3, getBasicPoint());
    }

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public void rotate() {
        switch (mStateOfRotation) {
            case "Normal":
                super.setBasicPoint(super.getBasicPoint().x - 2, super.getBasicPoint().y + 2);
                mCoord1.set(super.getBasicPoint().x, super.getBasicPoint().y - 3);
                mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 2);
                mCoord3.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
                mStateOfRotation = "Rotate_90_degrees";

                mAllCoords.set(0, mCoord1);
                mAllCoords.set(1, mCoord2);
                mAllCoords.set(2, mCoord3);
                mAllCoords.set(3, getBasicPoint());
                break;
            case "Rotate_90_degrees" :
                super.setBasicPoint(super.getBasicPoint().x + 2, super.getBasicPoint().y - 2);
                mCoord1.set(super.getBasicPoint().x - 3, super.getBasicPoint().y);
                mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
                mCoord3.set(super.getBasicPoint().x - 1, super.getBasicPoint().y);
                mStateOfRotation = "Normal";

                mAllCoords.set(0, mCoord1);
                mAllCoords.set(1, mCoord2);
                mAllCoords.set(2, mCoord3);
                mAllCoords.set(3, getBasicPoint());
                break;
        }
    }

    public ArrayList<Point> getAllCoords() {
        return mAllCoords;
    }

    public Figure_I clone() {
        Figure_I o = (Figure_I) super.clone();
        o.mAllCoords = new ArrayList<>();
        for (int i = 0; i < mAllCoords.size(); i++) {
            o.mAllCoords.add(new Point(mAllCoords.get(i).x, mAllCoords.get(i).y));
        }

        return o;
    }




   /* private Point [] mAllCoord = {
           mCoord1, mCoord2, mCoord3, super.getBasicPoint()
    };

    public Figure_I() {
        super.setBasicPoint(super.getBasicPoint().x + 3, super.getBasicPoint().y + 1);
        mCoord1.set(super.getBasicPoint().x - 3, super.getBasicPoint().y);
        mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
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
        super.setBasicPoint(x + 3, y + 1);
        mCoord1.set(super.getBasicPoint().x - 3, super.getBasicPoint().y);
        mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
        mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));
    }

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public void rotate() {
        switch (mStateOfRotation) {
            case "Normal":
                super.setBasicPoint(super.getBasicPoint().x - 2, super.getBasicPoint().y + 2);
                mCoord1.set(super.getBasicPoint().x, super.getBasicPoint().y - 3);
                mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 2);
                mCoord3.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
                mStateOfRotation = "Rotate_90_degrees";
                break;
            case "Rotate_90_degrees" :
                super.setBasicPoint(super.getBasicPoint().x + 2, super.getBasicPoint().y - 2);
                mCoord1.set(super.getBasicPoint().x - 3, super.getBasicPoint().y);
                mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
                mCoord3.set(super.getBasicPoint().x - 1, super.getBasicPoint().y);
                mStateOfRotation = "Normal";
                break;
        }
    }

    public Point[] getAllCoord() {
        return mAllCoord;
    }*/


}
