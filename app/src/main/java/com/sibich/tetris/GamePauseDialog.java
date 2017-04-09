package com.sibich.tetris;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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

    private Button mResumeButton, mNewGameButton,
            mRecordsButton, mHelpButton, mMainMenuButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_pause_game, null);

        mResumeButton = (Button)v.findViewById(R.id.game_pause_button_resume);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "resume";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mNewGameButton = (Button)v.findViewById(R.id.game_pause_button_new_game);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "new_game";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mRecordsButton = (Button) v.findViewById(R.id.game_pause_button_records);
        mRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "records";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mHelpButton = (Button) v.findViewById(R.id.game_pause_button_help);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "help";
                sendResult(Activity.RESULT_OK, pushButton);
                GamePauseDialog.this.getDialog().cancel();
            }
        });

        mMainMenuButton = (Button)v.findViewById(R.id.game_pause_button_main_menu);
        mMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "main_menu";
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

    private void startAnimation() {
        AnimatorSet animatorResumeButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.menu_pause_anim);
        animatorResumeButton.setTarget(mResumeButton);
        animatorResumeButton.setStartDelay(100);
        animatorResumeButton.start();

        AnimatorSet animatorNewGameButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.menu_pause_anim);
        animatorNewGameButton.setTarget(mNewGameButton);
        animatorNewGameButton.setStartDelay(300);
        animatorNewGameButton.start();

        AnimatorSet animatorRecordsButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.menu_pause_anim);
        animatorRecordsButton.setTarget(mRecordsButton);
        animatorRecordsButton.setStartDelay(500);
        animatorRecordsButton.start();

        AnimatorSet animatorHelpButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.menu_pause_anim);
        animatorHelpButton.setTarget(mHelpButton);
        animatorHelpButton.setStartDelay(700);
        animatorHelpButton.start();

        AnimatorSet animatorExitButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.menu_pause_anim);
        animatorExitButton.setTarget(mMainMenuButton);
        animatorExitButton.setStartDelay(900);
        animatorExitButton.start();
       /* ObjectAnimator resumeButtonAnimator = ObjectAnimator
                .ofFloat(mResumeButton, "scaleX", 0.0f, 1.0f)
                .setDuration(500);
        resumeButtonAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator newGameButtonAnimator = ObjectAnimator
                .ofFloat(mNewGameButton, "scaleX", 0.0f, 1.0f)
                .setDuration(500);
        newGameButtonAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(resumeButtonAnimator)
                .before(newGameButtonAnimator);
        animatorSet.start();*/

    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimation();
    }
}
