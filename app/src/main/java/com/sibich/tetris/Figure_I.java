package com.sibich.tetris;

/**
 * Created by Sibic_000 on 09.10.2016.
 */
import android.graphics.Point;


public class Figure_I extends Figure
{
    private static Point [] sLocalCoord =
            {
                new Point(-2, 0),
                new Point(-1, 0),
                new Point(0, 0),
                new Point(1, 0)
            };

    private static Matrix3.RotationDegree[] sAvailableOrientations =
            {
                    Matrix3.RotationDegree.DEGREE_0,
                    Matrix3.RotationDegree.DEGREE_90
            };

    public Figure_I() { }

    public Point[] getLocalCoord() {
        return sLocalCoord;
    }
    public Matrix3.RotationDegree[] getAvailableOrientations() { return sAvailableOrientations; }
}