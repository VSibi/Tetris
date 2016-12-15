package com.sibich.tetris;

import android.support.v4.app.Fragment;

/**
 * Created by Sibic_000 on 04.12.2016.
 */
public class TableOfRecordsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment () {
        return new TableOfRecordsFragment();
    }

}
