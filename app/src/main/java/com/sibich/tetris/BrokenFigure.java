package com.sibich.tetris;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sibic_000 on 03.01.2017.
 */

public class BrokenFigure extends Figure implements Cloneable{
    private ArrayList<Point> mAllCoords = new ArrayList<>();
    private String mStateOfRotation;

    public BrokenFigure (ArrayList<Point> coords) {
        mStateOfRotation = "Normal";
        for (int i = 0; i < coords.size(); i++) {
            mAllCoords.add(coords.get(i));
        }
    }

    public void setTranslate(int x, int y) {
        for(int i = 0; i < mAllCoords.size(); i++) {
            Point coord = mAllCoords.get(i);
            coord.x = coord.x + x;
            coord.y = coord.y + y;
            mAllCoords.set(i, coord);
        }
    }

    public String getStateOfRotation() {
        return mStateOfRotation;
    }

    public ArrayList<Point> getAllCoords() {
        return mAllCoords;
    }

    public BrokenFigure clone() {
        BrokenFigure o = (BrokenFigure) super.clone();
        o.mAllCoords = new ArrayList<>();
        for (int i = 0; i < mAllCoords.size(); i++) {
            o.mAllCoords.add(new Point(mAllCoords.get(i).x, mAllCoords.get(i).y));
        }

        return o;
    }
}
