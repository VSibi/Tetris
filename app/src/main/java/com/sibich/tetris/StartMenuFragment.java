package com.sibich.tetris;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Slavon on 09.02.2017.
 */
public class StartMenuFragment extends Fragment {

    private ImageView mLogoImageView, mSeparateLineImageView;
    private ImageButton mInfoImageButton, mHelpImageButton, mShareImageButton, mClassicModeImageButton;
    private Button /*mClassicModeImageButton,*/ mOpenImageModeButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start_menu, parent, false);

        mLogoImageView = (ImageView) v.findViewById(R.id.start_menu_imageView_logo);

        mClassicModeImageButton = (ImageButton) v.findViewById(R.id.start_menu_imageButton_classic);
        mClassicModeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = GameUIActivity.newIntent(getActivity(), "classic");
                startActivity(i);
            }
        });
        mOpenImageModeButton = (Button) v.findViewById(R.id.start_menu_Button_open_image);
        mOpenImageModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = GameUIActivity.newIntent(getActivity(), "openImage");
                startActivity(i);
            }
        });

        mSeparateLineImageView = (ImageView) v.findViewById(R.id.start_menu_imageView_separate_line);

        mInfoImageButton = (ImageButton) v.findViewById(R.id.start_menu_imageButton_info);
        mInfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mHelpImageButton = (ImageButton) v.findViewById(R.id.start_menu_imageButton_help);
        mHelpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HelpActivity.class);
                startActivity(i);
            }
        });

        mShareImageButton = (ImageButton) v.findViewById(R.id.start_menu_imageButton_share);
        mShareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MenuLevelsImageActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

    private void startAnimation() {
        /*float startXClassic = /*(float)mClassicLayout.getLeft()*/ /*- mClassicModeImageButton.getWidth(),*/
              /*  endXClassic =*/ /*(float)mClassicModeImageButton.getLeft();*/ /*80f;*/

        // Get the screen`s absolute width in pixels
        int widthPixels = getResources().getDisplayMetrics().widthPixels;

        // Get the screen's density scale
        float scale = getResources().getDisplayMetrics().density;

        float startXClassic = - (float)(widthPixels / 2)/*-(float)mClassicLayout.getWidth()/*-200f/*(mClassicModeImageButton.getLeft() + *//*-(float)mClassicModeImageButton.getWidth()*/,
                endXClassic = (5f * scale); /*20f/*(float)mClassicModeImageButton.getLeft();*/

      //  mText.setText("" + endXClassic);

        ObjectAnimator animXClassicModeButton = ObjectAnimator
                .ofFloat(mClassicModeImageButton, "x", startXClassic, endXClassic)
                .setDuration(800);
        animXClassicModeButton.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animAlphaClassicModeButton = ObjectAnimator
                .ofFloat(mClassicModeImageButton, "alpha", 0f, 1f)
                .setDuration(800);
      //  animXClassicModeButton.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animSetClassicModeButton = new AnimatorSet();
        animSetClassicModeButton.playTogether(animAlphaClassicModeButton,
                animXClassicModeButton);
        animSetClassicModeButton.setStartDelay(400);
        animSetClassicModeButton.start();

        float startXOpenImage =  (float)(widthPixels / 2) /*mOpenImageLayout.getWidth()*/,
                /*mOpenImageModeButton.getLeft() + /*400f*//* mOpenImageModeButton.getWidth(),*/
                endXOpenImage = (10f * scale)/*mOpenImageModeButton.getLeft()*/;

        ObjectAnimator animXOpenImageModeButton = ObjectAnimator
                .ofFloat(mOpenImageModeButton, "x", startXOpenImage, endXOpenImage)
                .setDuration(800);
        animXOpenImageModeButton.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animAlphaOpenImageModeButton = ObjectAnimator
                .ofFloat(mOpenImageModeButton, "alpha", 0f, 1f)
                .setDuration(800);
     //   animAlphaOpenImageModeButton.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animSetOpenImageModeButton = new AnimatorSet();
        animSetOpenImageModeButton.playTogether(animAlphaOpenImageModeButton,
                animXOpenImageModeButton);
        animSetOpenImageModeButton.setStartDelay(400);
        animSetOpenImageModeButton.start();

        ObjectAnimator animAlphaSeparateLine = ObjectAnimator
                .ofFloat(mSeparateLineImageView, "alpha", 0f, 1f)
                .setDuration(800);
        animAlphaSeparateLine.setStartDelay(500);
        animAlphaSeparateLine.start();

        AnimatorSet animatorLogo = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.fade_in_anim);
        animatorLogo.setTarget(mLogoImageView);
        animatorLogo.setStartDelay(300);
        animatorLogo.start();

        AnimatorSet animatorInfoButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.fade_in_anim);
        animatorInfoButton.setTarget(mInfoImageButton);
        animatorInfoButton.setStartDelay(500);
        animatorInfoButton.start();

        AnimatorSet animatorHelpButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.fade_in_anim);
        animatorHelpButton.setTarget(mHelpImageButton);
        animatorHelpButton.setStartDelay(600);
        animatorHelpButton.start();

        AnimatorSet animatorShareButton = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.animator.fade_in_anim);
        animatorShareButton.setTarget(mShareImageButton);
        animatorShareButton.setStartDelay(700);
        animatorShareButton.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        startAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLogoImageView.setAlpha(0f);
        mClassicModeImageButton.setAlpha(0f);
        mOpenImageModeButton.setAlpha(0f);
        mSeparateLineImageView.setAlpha(0f);
        mInfoImageButton.setAlpha(0f);
        mHelpImageButton.setAlpha(0f);
        mShareImageButton.setAlpha(0f);

    }

}
