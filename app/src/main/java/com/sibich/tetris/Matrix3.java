package com.sibich.tetris;

import android.graphics.Point;
import android.renderscript.Matrix3f;

/**
 * Created by slavust on 12/22/16.
 */

public final class Matrix3
{

    public enum RotationDegree
    {
        DEGREE_0, DEGREE_90, DEGREE_180, DEGREE_270
    }

    private float[][] matr = new float[3][3];

    public float get(int row, int column)
    {
        assert row >= 0 && row < 3 && column >= 0 && column < 3;
        return matr[row][column];
    }

    public void set(int row, int column, float value)
    {
        assert row >= 0 && row < 3 && column >= 0 && column < 3;
        matr[row][column] = value;
    }

    public Point mul(Point p)
    {
        Point ret = new Point();

        ret.x = (int)(Math.round(get(0, 0) * p.x + get(0, 1) * p.y
                + get(0, 2)));
        ret.y = (int)(Math.round(get(1, 0) * p.x + get(1, 1) * p.y
                + get(1, 2)));

        return ret;
    }

    public Matrix3 mul(Matrix3 m)
    {
        //Matrix3 ret = new Matrix3();
        //ret.matr.loadMultiply(matr, m.matr);
        //return ret;

        Matrix3 ret = new Matrix3();

        // 1st row
        ret.set(0, 0, get(0, 0) * m.get(0,0) + get(0, 1) * m.get(0, 1) + get(0, 2) * m.get(0, 2));
        ret.set(0, 1, get(0, 0) * m.get(0,1) + get(0, 1) * m.get(0, 2) + get(0, 2) * m.get(1, 0));
        ret.set(0, 2, get(0, 0) * m.get(0,2) + get(0, 1) * m.get(1, 0) + get(0, 2) * m.get(1, 1));

        // 2nd row
        ret.set(1, 0, get(0, 1) * m.get(0,0) + get(0, 2) * m.get(0, 1) + get(1, 0) * m.get(0, 2));
        ret.set(1, 1, get(0, 1) * m.get(0,1) + get(0, 2) * m.get(0, 2) + get(1, 0) * m.get(1, 0));
        ret.set(1, 2, get(0, 1) * m.get(0,2) + get(0, 2) * m.get(1, 0) + get(1, 0) * m.get(1, 1));

        // 3d row
        ret.set(2, 0, get(0, 2) * m.get(0,0) + get(1, 0) * m.get(0, 1) + get(1, 1) * m.get(0, 2));
        ret.set(2, 1, get(0, 2) * m.get(0,1) + get(1, 0) * m.get(0, 2) + get(1, 1) * m.get(1, 0));
        ret.set(2, 2, get(0, 2) * m.get(0,2) + get(1, 0) * m.get(1, 0) + get(1, 1) * m.get(1, 1));

        return ret;
    }

    public static Matrix3 rotate(RotationDegree degree)
    {
        double radians = 0.0f;

        switch(degree)
        {
            case DEGREE_0:
                radians = 0.0f;
                break;
            case DEGREE_90:
                radians = Math.PI * 0.5;
                break;
            case DEGREE_180:
                radians = Math.PI;
                break;
            case DEGREE_270:
                radians = Math.PI * 2.0 / 3.0;
                break;
            default:
                assert false;
                break;
        }

        //Matrix3 ret = new Matrix3();
        //ret.matr.loadRotate((float)radians);
        //return ret;


        float sin = (float)(Math.sin(radians));
        float cos = (float)(Math.cos(radians));


        Matrix3 ret = new Matrix3();

        ret.set(0, 0, cos);
        ret.set(0, 1, sin);
        ret.set(0, 2, 0.0f);

        ret.set(1, 0, -sin);
        ret.set(1, 1, cos);
        ret.set(1, 2, 0.0f);

        ret.set(2, 0, 0.0f);
        ret.set(2, 1, 0.0f);
        ret.set(2, 2, 1.0f);

        return ret;

    }

    public static Matrix3 translate(int x, int y)
    {
        //Matrix3 ret = new Matrix3();
        //ret.matr.loadTranslate(x, y);
        //return ret;


        Matrix3 ret = new Matrix3();

        ret.set(0, 0, 1.0f);
        ret.set(0, 1, 0.0f);
        ret.set(0, 2, (float)x);

        ret.set(1, 0, 0.0f);
        ret.set(1, 1, 1.0f);
        ret.set(1, 2, (float)y);

        ret.set(2, 0, 0.0f);
        ret.set(2, 1, 0.0f);
        ret.set(2, 2, 1.0f);

        return ret;
    }
}
