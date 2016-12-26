package com.sibich.tetris;

import android.graphics.Point;

/**
 * Created by slavust on 12/26/16.
 */

public class BrokenFigure extends Figure
{
    private Matrix3.RotationDegree[] mAvailableOrientations
            = null;

    public BrokenFigure(Point[] coords, Point position, Matrix3.RotationDegree orientation)
    {
        mAvailableOrientations = new Matrix3.RotationDegree[]{orientation};
        setLocalCoords(coords);
        setPosition(position.x, position.y);
        setOrientationIndx(0);
    }

    public Matrix3.RotationDegree[] getAvailableOrientations()
    {
        return mAvailableOrientations;
    }
}
