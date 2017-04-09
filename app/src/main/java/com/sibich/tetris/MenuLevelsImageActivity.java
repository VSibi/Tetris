package com.sibich.tetris;

import android.support.v4.app.Fragment;

/**
 * Created by Slavon on 05.12.2016.
 */
public class MenuLevelsImageActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new MenuLevelsImageFragment();
    }
}
