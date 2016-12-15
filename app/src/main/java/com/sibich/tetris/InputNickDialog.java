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
import android.widget.EditText;

/**
 * Created by Slavon on 14.11.2016.
 */
public class InputNickDialog extends DialogFragment {

    public static final String EXTRA_INPUT_NICK_DIALOG_PUSH_BUTTON =
            "com.sibich.tetris.input_nick.dialog.push_button";
    public static final String EXTRA_INPUT_NICK_DIALOG_NICKNAME_STRING =
            "com.sibich.tetris.input_nick.dialog.nickname";
    public String etNickName = "";

    private Button mEnter;
    private EditText mNickNameEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_input_nick, null);

        mNickNameEditText = (EditText) v.findViewById(R.id.inputNickEditText);

        mEnter = (Button)v.findViewById(R.id.enterInputNickButton);
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNickName = mNickNameEditText.getText().toString();

                String pushButton = "enter";
                sendResult(Activity.RESULT_OK, pushButton);
                InputNickDialog.this.getDialog().cancel();
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
        intent.putExtra(EXTRA_INPUT_NICK_DIALOG_PUSH_BUTTON, pushButton);
        intent.putExtra(EXTRA_INPUT_NICK_DIALOG_NICKNAME_STRING, etNickName);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
