package com.sibich.tetris;

import android.support.v4.app.Fragment;

/**
 * Created by Slavon on 09.02.2017.
 */
public class StartMenuActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment () {
        return new StartMenuFragment();
    }
}
