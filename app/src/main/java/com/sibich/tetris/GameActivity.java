package com.sibich.tetris;


import android.support.v4.app.Fragment;

/**
 * Created by Slavon on 27.09.2016.
 */
public class GameActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new GameFragment();
    };



}


