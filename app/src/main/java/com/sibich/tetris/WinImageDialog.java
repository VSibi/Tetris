package com.sibich.tetris;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Slavon on 14.11.2016.
 */
public class WinImageDialog extends DialogFragment {

    private static final String ARG_LEVEL = "level";
    public static final String EXTRA_WINIMAGE_PUSH_ON_IMAGE =
            "com.sibich.tetris.win_image_push_on_image";

    private int [] mWinLevelImages = {
            R.drawable.win_image_level_1, R.drawable.win_image_level_2,
            R.drawable.win_image_level_3, R.drawable.win_image_level_4,
            R.drawable.win_image_level_1, R.drawable.win_image_level_2,
            R.drawable.win_image_level_3, R.drawable.win_image_level_4,
            R.drawable.win_image_level_1, R.drawable.win_image_level_2
    };
    private LinearLayout mLinearLayoutI;

    public static WinImageDialog newInstance(int level) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LEVEL, level);
        WinImageDialog dialog = new WinImageDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int level = (int) getArguments().getSerializable(ARG_LEVEL);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_win_image, null);


        mLinearLayoutI = (LinearLayout) v.findViewById(R.id.win_image_layout);
        mLinearLayoutI.setBackgroundResource(mWinLevelImages[level - 1]);
        mLinearLayoutI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pushButton = "level_up";
                sendResult(Activity.RESULT_OK, pushButton);
                WinImageDialog.this.getDialog().cancel();
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
        intent.putExtra(EXTRA_WINIMAGE_PUSH_ON_IMAGE, pushButton);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
