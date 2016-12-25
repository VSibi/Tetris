package com.sibich.tetris;

import android.graphics.Point;

/**
 * Created by Sibic_000 on 23.10.2016.
 */
public class Figure_SQ extends Figure {

    private static Point [] sLocalCoord = {
            new Point(0, 0),
            new Point(-1, -1),
            new Point(0, -1),
            new Point(-1, 0)
    };

    private static Matrix3.RotationDegree[] sAvailableOriebtations = {
            Matrix3.RotationDegree.DEGREE_0
    };

    public Figure_SQ() {
    }

    public Matrix3.RotationDegree[] getAvailableOrientations()
    {
        return sAvailableOriebtations;
    }

    public Point[] genStartLocalCoord() {
        return sLocalCoord.clone();
    }
}
