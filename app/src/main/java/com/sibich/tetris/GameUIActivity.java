package com.sibich.tetris;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Slavon on 27.09.2016.
 */
public class GameUIActivity extends SingleFragmentActivity {
    private static final String EXTRA_GAME_MODE = "com.sibich.tetris_game_mode";

    public static Intent newIntent(Context packageContext, String gameMode) {
        Intent i = new Intent(packageContext, GameUIActivity.class);
        i.putExtra(EXTRA_GAME_MODE, gameMode);
        return i;
    }

    @Override
    protected Fragment createFragment () {
        String gameMode = (String) getIntent().getSerializableExtra(EXTRA_GAME_MODE);
        return GameUIFragment.newInstance(gameMode);
    }

}


