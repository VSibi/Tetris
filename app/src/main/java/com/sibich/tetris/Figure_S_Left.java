package com.sibich.tetris;

import android.graphics.Point;

/**
 * Created by Sibic_000 on 07.11.2016.
 */
public class Figure_S_Left extends Figure {

    private static Point [] sLocalCoord = {
            new Point(0, 0),
            new Point(-1, -1),
            new Point(0, -1),
            new Point(1, 0)
    };

    private static Matrix3.RotationDegree[] sAvailableOrientations = {
            Matrix3.RotationDegree.DEGREE_0,
            Matrix3.RotationDegree.DEGREE_90
    };

    public Figure_S_Left() {
    }

    public Matrix3.RotationDegree[] getAvailableOrientations() { return sAvailableOrientations; }

    public Point[] genStartLocalCoord() {
        return sLocalCoord.clone();
    }
}
