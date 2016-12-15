package com.sibich.tetris;

import android.support.v4.app.Fragment;

/**
 * Created by Slavon on 05.12.2016.
 */
public class HelpActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new HelpFragment();
    }
}
