package com.sibich.tetris;

/**
 * Created by Sibic_000 on 09.10.2016.
 */
import android.graphics.Point;

import java.util.ArrayList;


public class Figure_T extends Figure implements Cloneable{

    private ArrayList<Point> mAllCoords = new ArrayList<>();

    private Point mCoord1 = new Point();
    private Point mCoord2 = new Point();
    private Point mCoord3 = new Point();
    private String mStateOfRotation;

    public Figure_T() {
        super.setBasicPoint(super.getBasicPoint().x + 1, super.getBasicPoint().y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y - 1));

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
        mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y - 1));

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
                mCoord1.set(super.getBasicPoint().x, super.getBasicPoint().y - 2);
                mCoord2.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
                mCoord3.set(super.getBasicPoint().x, (super.getBasicPoint().y - 1));
                mStateOfRotation = "Rotate_90_degrees";

                mAllCoords.set(0, mCoord1);
                mAllCoords.set(1, mCoord2);
                mAllCoords.set(2, mCoord3);
                break;
            case "Rotate_90_degrees" :
                super.setBasicPoint(super.getBasicPoint().x + 1, super.getBasicPoint().y - 1);
                mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
                mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
                mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));
                mStateOfRotation = "Rotate_180_degrees";

                mAllCoords.set(0, mCoord1);
                mAllCoords.set(1, mCoord2);
                mAllCoords.set(2, mCoord3);
                mAllCoords.set(3, getBasicPoint());
                break;
            case "Rotate_180_degrees" :
                super.setBasicPoint(super.getBasicPoint().x - 1, super.getBasicPoint().y + 1);
                mCoord1.set(super.getBasicPoint().x, super.getBasicPoint().y - 2);
                mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
                mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y - 1));
                mStateOfRotation = "Rotate_270_degrees";

                mAllCoords.set(0, mCoord1);
                mAllCoords.set(1, mCoord2);
                mAllCoords.set(2, mCoord3);
                mAllCoords.set(3, getBasicPoint());
                break;
            case "Rotate_270_degrees" :
                mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
                mStateOfRotation = "Normal";

                mAllCoords.set(0, mCoord1);
                break;
        }
    }

    public ArrayList<Point> getAllCoords() {
        return mAllCoords;
    }

    public Figure_T clone() {
        Figure_T o = (Figure_T) super.clone();
            o.mAllCoords = new ArrayList<>();
            for (int i = 0; i < mAllCoords.size(); i++) {
                o.mAllCoords.add(new Point(mAllCoords.get(i).x, mAllCoords.get(i).y));
            }

        return o;
    }

   /* private Point mCoord1 = new Point();
    private Point mCoord2 = new Point();
    private Point mCoord3 = new Point();
    private String mStateOfRotation;


    private Point [] mAllCoord = {
           mCoord1, mCoord2, mCoord3, super.getBasicPoint()
    };

    public Figure_T() {
        super.setBasicPoint(super.getBasicPoint().x + 1, super.getBasicPoint().y + 1);
        mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
        mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
        mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y - 1));
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
        mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y - 1));
    }

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public void rotate() {
        switch (mStateOfRotation) {
            case "Normal":
                mCoord1.set(super.getBasicPoint().x, super.getBasicPoint().y - 2);
                mCoord2.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
                mCoord3.set(super.getBasicPoint().x, (super.getBasicPoint().y - 1));
                mStateOfRotation = "Rotate_90_degrees";
                break;
            case "Rotate_90_degrees" :
                super.setBasicPoint(super.getBasicPoint().x + 1, super.getBasicPoint().y - 1);
                mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
                mCoord2.set(super.getBasicPoint().x - 2, super.getBasicPoint().y);
                mCoord3.set(super.getBasicPoint().x - 1, (super.getBasicPoint().y));
                mStateOfRotation = "Rotate_180_degrees";
                break;
            case "Rotate_180_degrees" :
                super.setBasicPoint(super.getBasicPoint().x - 1, super.getBasicPoint().y + 1);
                mCoord1.set(super.getBasicPoint().x, super.getBasicPoint().y - 2);
                mCoord2.set(super.getBasicPoint().x, super.getBasicPoint().y - 1);
                mCoord3.set(super.getBasicPoint().x + 1, (super.getBasicPoint().y - 1));
                mStateOfRotation = "Rotate_270_degrees";
                break;
            case "Rotate_270_degrees" :
                mCoord1.set(super.getBasicPoint().x - 1, super.getBasicPoint().y - 1);
             /*   mCoord2.set(super.getBasicPoint().x + 1, super.getBasicPoint().y);
                mCoord3.set(super.getBasicPoint().x, (super.getBasicPoint().y + 1));*/
        /*        mStateOfRotation = "Normal";
                break;
        }
    }

    public Point[] getAllCoord() {
        return mAllCoord;
    }
*/

}
