package com.sibich.tetris;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Slavon on 14.11.2016.
 */
public class Top_10_Dialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_top_10, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
             //   .setTitle(R.string.Pause_game
             //     .setPositiveButton(android.R.string.ok, null)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getActivity(), TableOfRecordsActivity.class);
                                startActivity(i);
                            }
                        })
                .create();
    }


}
