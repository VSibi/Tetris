package com.sibich.tetris;

import android.util.Log;

/**
 * Created by slavust on 12/25/16.
 */

public final class Debug {

    public static void Assert(boolean assertion) throws AssertionError
    {
        if(BuildConfig.DEBUG && !assertion)
            throw new AssertionError();
    }

    public static void Log(String line)
    {
        Log.d("tetris_dbg", line);
    }
}
