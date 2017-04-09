package com.sibich.tetris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Slavon on 05.12.2016.
 */
public class HelpFragment extends Fragment {

    private FragmentManager fm;
    private HelpClassicModeFragment mHelpClassicModeFragment;
    private HelpOpenImageModeFragment mHelpOpenImageModeFragment;

    private Button mHelpClassicButton, mHelpOpenImageButton;

    private ImageButton mBackButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelpClassicModeFragment = new HelpClassicModeFragment();
        mHelpOpenImageModeFragment = new HelpOpenImageModeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, parent, false);

        fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.help_fragment_container, mHelpClassicModeFragment)
                .commit();

        mHelpClassicButton = (Button) v.findViewById(R.id.help_classic_mode_Button);
        mHelpClassicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.help_fragment_container, mHelpClassicModeFragment)
                        .commit();
            }
        });
        mHelpOpenImageButton = (Button) v.findViewById(R.id.help_open_image_mode_Button);
        mHelpOpenImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.help_fragment_container, mHelpOpenImageModeFragment)
                        .commit();
            }
        });

        mBackButton = (ImageButton) v.findViewById(R.id.help_back_ImageButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return v;

    }
}
