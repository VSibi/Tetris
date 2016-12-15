package com.sibich.tetris;


import android.support.v4.app.Fragment;

/**
 * Created by Slavon on 27.09.2016.
 */
public class GameUIActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment () {
        return new GameUIFragment();
    }

}


