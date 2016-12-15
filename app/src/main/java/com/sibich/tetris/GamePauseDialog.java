package com.sibich.tetris;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Slavon on 14.11.2016.
 */
public class GamePauseDialog extends DialogFragment {

    public static final String EXTRA_PAUSE_DIALOG_PUSH_BUTTON =
            "com.sibich.tetris.pausedialog.push_button";

    private Button mResume, mSaveGame, mNewGame, mRecords, mHelp, mExit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_pause_game, null);

        mResume = (Button)v.findViewById(R.id.game_pause_button_resume);
        mResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "resume";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mNewGame = (Button)v.findViewById(R.id.game_pause_button_new_game);
        mNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "new_game";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mRecords = (Button) v.findViewById(R.id.game_pause_button_records);
        mRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "records";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mHelp = (Button) v.findViewById(R.id.game_pause_button_help);
        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "help";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mExit = (Button)v.findViewById(R.id.game_pause_button_exit);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "exit";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    private void sendResult(int resultCode, String pushButton) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PAUSE_DIALOG_PUSH_BUTTON, pushButton);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
