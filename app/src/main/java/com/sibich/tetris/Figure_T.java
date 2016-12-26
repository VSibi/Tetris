package com.sibich.tetris;

/**
 * Created by Sibic_000 on 09.10.2016.
 */
import android.graphics.Point;


public class Figure_T extends Figure{

    private static Point [] sLocalCoord = {
            new Point(0, 0),
            new Point(-1, -1),
            new Point(0, -1),
            new Point(1, -1)
    };

    private static Matrix3.RotationDegree[] sAvailableOrientations = {
            Matrix3.RotationDegree.DEGREE_0,
            Matrix3.RotationDegree.DEGREE_90,
            Matrix3.RotationDegree.DEGREE_180,
            Matrix3.RotationDegree.DEGREE_270
    };

    public Figure_T()
    {
        setLocalCoords(genStartLocalCoord());
    }

    public Point[] genStartLocalCoord() {
        return sLocalCoord.clone();
    }

    public Matrix3.RotationDegree[] getAvailableOrientations() { return sAvailableOrientations; }

}
