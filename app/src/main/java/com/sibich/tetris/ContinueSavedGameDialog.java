package com.sibich.tetris;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Slavon on 14.11.2016.
 */
public class ContinueSavedGameDialog extends DialogFragment {

    public static final String EXTRA_CONTINUE_SAVED_GAME_DIALOG_PUSH_BUTTON =
            "com.sibich.tetris.continueSavedGameDialog.push_button";


    private Button mContinue,  mNewGame;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_continue_saved_game, null);

        mContinue = (Button)v.findViewById(R.id.continueOnContinueSavedGameButton);
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "continue";
                sendResult(Activity.RESULT_OK, pushButton);
                ContinueSavedGameDialog.this.getDialog().cancel();
            }
        });

        mNewGame = (Button)v.findViewById(R.id.newGameOnContinueSavedGameButton);
        mNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "new_game";
                sendResult(Activity.RESULT_OK, pushButton);
                ContinueSavedGameDialog.this.getDialog().cancel();
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
        intent.putExtra(EXTRA_CONTINUE_SAVED_GAME_DIALOG_PUSH_BUTTON, pushButton);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
