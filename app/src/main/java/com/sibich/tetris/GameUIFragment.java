package com.sibich.tetris;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sibic_000 on 14.01.2017.
 */
public class GameUIFragment extends Fragment{
    public static final String ARG_GAME_MODE = "game_mode";


        private static final String DIALOG_PAUSE = "Pause";
        private static final String DIALOG_INPUT_NICK = "Input_nick";
        private static final String DIALOG_INPUT_NICK_FOR_TABLE_OF_RECORDS =
                "Input_nick_for_table_of_records";
        private static final String DIALOG_TOP_10 = "Top_10_dialog";
        private static final String DIALOG_WINIMAGE = "WinImage_dialog";

        private static final int REQUEST_ACTION_IN_PAUSE_DIALOG = 0;
        private static final int REQUEST_ACTION_IN_INPUT_NICK_DIALOG = 1;
        private static final int REQUEST_ACTION_IN_INPUT_NICK_FOR_TABLE_OF_RECORDS_DIALOG = 2;
        private static final int REQUEST_ACTION_IN_CONTINUE_SAVED_GAME_DIALOG = 3;
        private static final int REQUEST_ACTION_IN_WINIMAGE_DIALOG = 4;

        private final int mWidthGameField = 10;
        private final int mHeightGameField = 20;

    //    private SaveOrLoadGame mSaveOrLoadGame;
   //    private static final String FILENAME = "gamefield.json";

        private int[][] mGameField =  new int [mHeightGameField][mWidthGameField];
        private int [][] mNextFigureGameField = new int[2][4];

        private Timer mTimer;
        private long mTimeInterval = 58;

        private GameLogic mGameLogic;

        private FrameLayout mFrameLayout;
        private TableLayout mGameFieldTableLayout, mNextFigureTableLayout,
                mBorderLeftTableLayout, mBorderRightTableLayout;

        private TextView mSpeedTextView, mScoreTextView, mLevelTextView,
                mNickNameTextView, mGameOverTextView,  mGameWinTextView,
                mThingTextView, mThingTitleTextView, mLevelUpTextView;

        private ImageButton mSpeedMinus, mSpeedPlus, mGamePause, mInputNickName;

        private String mNickName = "";
        private String mGameMode = "openImage";

        private boolean mIsPause = false;
        private int mMaxLevelCount = 10;
        private int mCurrLevel;

    public static GameUIFragment newInstance(String gameMode) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_GAME_MODE, gameMode);
            GameUIFragment fragment = new GameUIFragment();
            fragment.setArguments(args);
            return fragment;
        }



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mGameMode = (String) getArguments().getSerializable(ARG_GAME_MODE);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_game, parent, false);

            mNickName = getString(R.string.PLAYER);

            mGameLogic = new GameLogic(getActivity());
            mGameLogic.setGameMode(mGameMode);
        //    mSaveOrLoadGame = new SaveOrLoadGame(getActivity(), FILENAME);

            mThingTextView = (TextView) v.findViewById(R.id.thing_textView);
            mThingTitleTextView = (TextView) v.findViewById(R.id.thing_title_textView);

            mFrameLayout = (FrameLayout) v.findViewById(R.id.frame_layout);
            mNextFigureTableLayout = (TableLayout) v.findViewById(R.id.next_figure_game_field_tablelayout);
            mGameFieldTableLayout = (TableLayout) v.findViewById(R.id.game_field_tablelayout);
            mBorderLeftTableLayout = (TableLayout) v.findViewById(R.id.border_left_tablelayout);
            mBorderRightTableLayout = (TableLayout) v.findViewById(R.id.border_right_tablelayout);
            switch (mGameMode) {
                case "classic":
                    mGameFieldTableLayout.setBackgroundResource(R.color.background_game_field);
                    mThingTextView.setVisibility(View.INVISIBLE);
                    mThingTitleTextView.setVisibility(View.INVISIBLE);
                    break;
                case "openImage":
                    mGameFieldTableLayout.setBackgroundResource(R.drawable.background_game_field);
                    for (int i = mBorderLeftTableLayout.getChildCount() - 1; i > 1; i--) {
                        TableRow row = (TableRow) mBorderLeftTableLayout.getChildAt(i);
                        ImageView imageview = (ImageView) row.getChildAt(0);
                        imageview.setBackgroundResource(R.color.red);

                        row = (TableRow) mBorderRightTableLayout.getChildAt(i);
                        imageview = (ImageView) row.getChildAt(0);
                        imageview.setBackgroundResource(R.color.red);
                    }
                    break;
            }

         /*   mBorderLeftTableLayout = (TableLayout) v.findViewById(R.id.border_left_tablelayout);
            mBorderRightTableLayout = (TableLayout) v.findViewById(R.id.border_right_tablelayout);*/
          //  if (mGameLogic.getGameMode().equals("openImage")) {
           /* if (mGameMode.equals("openImage")) {
                for (int i = mBorderLeftTableLayout.getChildCount() - 1; i > 1; i--) {
                    TableRow row = (TableRow) mBorderLeftTableLayout.getChildAt(i);
                    ImageView imageview = (ImageView) row.getChildAt(0);
                    imageview.setBackgroundResource(R.color.red);

                    row = (TableRow) mBorderRightTableLayout.getChildAt(i);
                    imageview = (ImageView) row.getChildAt(0);
                    imageview.setBackgroundResource(R.color.red);
                }
            }*/

         /*   mBorderRightTableLayout = (TableLayout) v.findViewById(R.id.border_right_tablelayout);
            for (int i = mBorderRightTableLayout.getChildCount()-1; i > 1; i--) {
                TableRow row = (TableRow) mBorderRightTableLayout.getChildAt(i);
                ImageView imageview = (ImageView) row.getChildAt(0);
                imageview.setBackgroundResource(R.color.red);
            }*/


            mSpeedTextView = (TextView) v.findViewById(R.id.speed_textView);
            mScoreTextView = (TextView) v.findViewById(R.id.score_textView);
            mLevelTextView = (TextView) v.findViewById(R.id.level_textView);
            mNickNameTextView = (TextView) v.findViewById(R.id.nickName_textView);
            mGameOverTextView = (TextView) v.findViewById(R.id.game_over_textView);
            mGameWinTextView = (TextView) v.findViewById(R.id.game_win_textView);
            mLevelUpTextView = (TextView) v.findViewById(R.id.level_up_textView);

            mSpeedMinus = (ImageButton)v.findViewById(R.id.imageButtonSpeedMinus);
            mSpeedPlus = (ImageButton)v.findViewById(R.id.imageButtonSpeedPlus);

            mSpeedMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGameLogic.speedMin();
                }
            });

            mSpeedPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGameLogic.speedPlus();
                }
            });

            mGamePause = (ImageButton)v.findViewById(R.id.game_pause_imageButton);
            mGamePause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pauseGame();
                }
            });

            mInputNickName = (ImageButton)v.findViewById(R.id.input_nick_imageButton);
            mInputNickName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputNickName();
                }
            });

            mFrameLayout.setOnTouchListener(new View.OnTouchListener() {
                boolean isMove, isMoveDown;
                float startPointX = 0, endPointX = 0;
                float startPointY = 0, endPointY = 0;
                float resultX = 0, resultY = 0;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startPointX = motionEvent.getX();
                            startPointY = motionEvent.getY();
                            isMove = false;
                            isMoveDown = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            endPointX = motionEvent.getX();
                            endPointY = motionEvent.getY();
                            resultX = endPointX - startPointX;
                            resultY = endPointY - startPointY;

                            if (Math.abs(resultX) > 30.0f) {
                                isMove = true;
                                isMoveDown = false;
                                if (resultX < 0) {
                                    mGameLogic.moveFigureLeft(mGameLogic.getCurrFigure());
                                    draw();
                                } else {
                                    mGameLogic.moveFigureRight(mGameLogic.getCurrFigure());
                                    draw();
                                }
                                startPointX = endPointX;
                                resultX = 0;
                            }
                            else {
                                if (Math.abs(resultY) > 80.0f) {
                                    isMove = true;
                                    if (resultY > 0) isMoveDown = true;
                                    startPointY = endPointY;
                                    resultY = 0;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:

                      /*  if (!isMove) {
                            mGameLogic.rotateFigure(mGameLogic.getCurrFigure());
                            draw();
                        }*/

                     /*   if (isMoveDown) {
                            isMove = true;
                            endPointY = motionEvent.getY();
                            resultY = endPointY - startPointY;

                            if (resultY != 0) {
                                if (Math.abs(resultY) > 30) {
                                    if (resultY > 0) {
                                        mGameLogic.quickFallDownFigure(mGameLogic.getCurrFigure());
                                        draw();
                                    }
                                }
                            }

                            resultY = 0;
                        }

                        if (!isMove) {
                            mGameLogic.rotateFigure(mGameLogic.getCurrFigure());
                            draw();
                        }

                        isMoveDown = true;*/
                            if (isMove) {
                                if (isMoveDown) {
                                    mGameLogic.quickFallDownFigure(mGameLogic.getCurrFigure());
                                    draw();
                                }
                            }
                            else {
                                mGameLogic.rotateFigure(mGameLogic.getCurrFigure());
                                draw();
                            }
                            break;
                    }
                    return true;
                }
            });

            if(isSavedGame()) {
                continueSavedGame();
            //    Toast.makeText(getActivity(), "EXIST SAVE GAME!", Toast.LENGTH_SHORT).show();
            }
            else {
                loadGame();
            //    startGame();
            }

            return v;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            if (requestCode == REQUEST_ACTION_IN_CONTINUE_SAVED_GAME_DIALOG) {
                String titleOnButton = (String) intent
                        .getSerializableExtra(ContinueSavedGameDialog.EXTRA_CONTINUE_SAVED_GAME_DIALOG_PUSH_BUTTON);
                switch (titleOnButton) {
                    case "continue":
                        loadGame();
                   //     startGame();
                        break;
                    case "new_game":
                        newGame(1);
                        break;
                }
            }

            if (requestCode == REQUEST_ACTION_IN_INPUT_NICK_DIALOG
                    || requestCode == REQUEST_ACTION_IN_INPUT_NICK_FOR_TABLE_OF_RECORDS_DIALOG) {
                switch (requestCode) {
                    case REQUEST_ACTION_IN_INPUT_NICK_DIALOG:
                        mNickName = (String) intent
                                .getSerializableExtra(InputNickDialog.EXTRA_INPUT_NICK_DIALOG_NICKNAME_STRING) ;
                        break;
                    case REQUEST_ACTION_IN_INPUT_NICK_FOR_TABLE_OF_RECORDS_DIALOG:
                        mNickName = (String) intent
                                .getSerializableExtra(InputNickForTableOfRecordsDialog
                                        .EXTRA_INPUT_NICK_FOR_TABLE_OF_RECORDS_DIALOG_NICKNAME_STRING) ;
                        break;
                }

                if (mNickName.equals("")) mNickName = getString(R.string.PLAYER);
                mNickNameTextView.setText(mNickName);

                if (mGameLogic.getEndOfGame()) {
                    if (mGameLogic.isTopTenResult()) {
                        mGameLogic.updateTableOfRecords(mNickName);
                        Intent i = new Intent(getActivity(), TableOfRecordsActivity.class);
                        startActivity(i);
                    }
                }
                else {
                    startGame();
                }
            }

            if (requestCode == REQUEST_ACTION_IN_PAUSE_DIALOG) {
                String titleOnButton = (String) intent
                        .getSerializableExtra(GamePauseDialog.EXTRA_PAUSE_DIALOG_PUSH_BUTTON);
                switch (titleOnButton) {
                    case "resume":
                        if(!mGameLogic.getEndOfGame()) {
                            startGame();
                        }
                        mIsPause = false;
                        break;
                    case "new_game":
                        newGame(1);
                        mIsPause = false;
                        break;
                    case "records":
                        Intent i = new Intent(getActivity(), TableOfRecordsActivity.class);
                        startActivity(i);
                        mIsPause = false;
                        break;
                    case "help":
                        Intent j = new Intent(getActivity(), HelpActivity.class);
                        startActivity(j);
                        mIsPause = false;
                        break;
                    case "main_menu":
                        getActivity().onBackPressed();
                        mIsPause = false;
                        break;



                }
            }

            if (requestCode == REQUEST_ACTION_IN_WINIMAGE_DIALOG) {
                String push_on_image = (String) intent
                        .getSerializableExtra(WinImageDialog.EXTRA_WINIMAGE_PUSH_ON_IMAGE);

                if (push_on_image.equals("level_up")) {
                    if (mGameLogic.getLevel() < mMaxLevelCount) {
                        int mNextLevel = (mGameLogic.getLevel() + 1);
                      //  mGameLogic.setLevel(mNextLevel);

                        newGame(mNextLevel);

                       /* mGameLogic.clearGameField();
                        draw();

                        mLevelUpTextView.setVisibility(View.VISIBLE);
                        String text = getString(R.string.Level_up) + " " + mNextLevel;
                        mLevelUpTextView.setText(text);
                        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                                R.animator.win_text_view_anim);
                        set.setTarget(mLevelUpTextView);
                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                newGame();
                                mGameLogic.setLevel(mNextLevel);
                            }
                        });
                        set.start();*/
                    }
                }
            }
        }


        private void startGame() {

            startTimer();
            mGameLogic.startTimer();
        }

        private void pauseGame(){
            mIsPause = true;
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mGameLogic.pauseGame();

            FragmentManager fm = getFragmentManager();
            GamePauseDialog dialog = new GamePauseDialog();
            dialog.setTargetFragment(GameUIFragment.this, REQUEST_ACTION_IN_PAUSE_DIALOG);
            dialog.setCancelable(false);
            dialog.show(fm, DIALOG_PAUSE);
        }

        private void continueSavedGame() {
            FragmentManager fm = getFragmentManager();
            ContinueSavedGameDialog dialog = new ContinueSavedGameDialog();
            dialog.setTargetFragment(GameUIFragment.this, REQUEST_ACTION_IN_CONTINUE_SAVED_GAME_DIALOG);
            dialog.setCancelable(false);
            dialog.show(fm, DIALOG_PAUSE);
        }

        private void stopGame() {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                    R.animator.game_over_text_view_anim);
            set.setTarget(mGameOverTextView);

            switch (mGameMode) {
                case "classic":
                    mGameOverTextView.setVisibility(View.VISIBLE);

                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (mGameLogic.isTopTenResult()) {
                                if (mNickName.equals(getString(R.string.PLAYER))) inputNickNameForTableOfRecords();
                                else {
                                    if (mGameLogic.updateTableOfRecords(mNickName)) updateTop10Dialog();
                                    else top10Dialog();
                                }
                            }
                        }
                    });
                    set.start();

                   /* if (mGameLogic.isTopTenResult()) {
                        if (mNickName.equals(getString(R.string.PLAYER))) inputNickNameForTableOfRecords();
                        else {
                            if (mGameLogic.updateTableOfRecords(mNickName)) updateTop10Dialog();
                            else top10Dialog();
                        }
                    }*/
                    break;
                case "openImage":
                    if (!mGameLogic.isWin()) {
                        mGameOverTextView.setVisibility(View.VISIBLE);
                        set.start();
                    }
                    else {
                        mGameWinTextView.setVisibility(View.VISIBLE);
                        set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                                R.animator.win_text_view_anim);
                        set.setTarget(mGameWinTextView);
                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                winImageDialog();
                            }
                        });
                        set.start();
                        //  winImageDialog();
                    }
                    break;
            }
          /*  if (mGameLogic.isTopTenResult()) {
                if (mNickName.equals(getString(R.string.PLAYER))) inputNickNameForTableOfRecords();
                else {
                    if (mGameLogic.updateTableOfRecords(mNickName)) updateTop10Dialog();
                    else top10Dialog();
                }
            }*/
        }

    private void winImageDialog() {
        FragmentManager fm = getFragmentManager();
        WinImageDialog dialog = WinImageDialog.newInstance(mGameLogic.getLevel());
        dialog.setTargetFragment(GameUIFragment.this, REQUEST_ACTION_IN_WINIMAGE_DIALOG);
        dialog.setCancelable(false);
        dialog.show(fm, DIALOG_WINIMAGE);
    }

 /*   private void levelUp() {}*/

        private void newGame(int level) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            this.mCurrLevel = level;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mNickName = sharedPreferences.getString("NickName", getString(R.string.PLAYER));
            mNickNameTextView.setText(mNickName);

            mGameOverTextView.setVisibility(View.INVISIBLE);

            mGameLogic.clearGameField();
            mThingTextView.setText("");
            mLevelTextView.setText("");
            mScoreTextView.setText("");
            mSpeedTextView.setText("");
            draw();

         /*   if (mGameMode.equals("openImage")) {
                for (int i = mBorderLeftTableLayout.getChildCount() - 1; i > 1; i--) {
                    TableRow row = (TableRow) mBorderLeftTableLayout.getChildAt(i);
                    ImageView imageview = (ImageView) row.getChildAt(0);
                    imageview.setBackgroundResource(R.color.red);

                    row = (TableRow) mBorderRightTableLayout.getChildAt(i);
                    imageview = (ImageView) row.getChildAt(0);
                    imageview.setBackgroundResource(R.color.red);
                }
            }*/

            mLevelUpTextView.setVisibility(View.VISIBLE);
            String text = getString(R.string.Level_up) + " " + mCurrLevel;
            mLevelUpTextView.setText(text);
            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                    R.animator.win_text_view_anim);
            set.setTarget(mLevelUpTextView);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mGameLogic.newGame(mCurrLevel);
                    startTimer();
                }
            });
            set.start();

          /*  mGameLogic.newGame();
            startTimer();*/
        }

        public void saveGame() {

            editSharedPreferences(mGameLogic.getScore(), mGameLogic.getSpeed(),
                    mGameLogic.getLevel());

       /*     SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int maxLevelForOpenImageMode = sharedPreferences.getInt("MaxLevelForOpenImageMode", 1);

            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("NickName", mNickName);
            if(mGameMode.equals("classic")) {
                edit.putInt("Score_Classic", mGameLogic.getScore());
                edit.putInt("Speed_Classic", mGameLogic.getSpeed());
                edit.putInt("Level_Classic", mGameLogic.getLevel());

            }
            else {
                edit.putInt("Score_openImage", mGameLogic.getScore());
                edit.putInt("Speed_openImage", mGameLogic.getSpeed());
                edit.putInt("Level_openImage", mGameLogic.getLevel());
                if (mGameLogic.getLevel() > maxLevelForOpenImageMode) {
                    edit.putInt("maxLevelForOpenImageMode", mGameLogic.getLevel());
                }
            }
            edit.apply();*/

            try {
                if (!mGameLogic.saveGameField(mGameLogic.getListFallenFigures())) {
                    Toast.makeText(getActivity(), "NO SAVE", Toast.LENGTH_SHORT).show();
                }
              /*  else {
                    //   Toast.makeText(getActivity(), "SAVE IS OK!", Toast.LENGTH_SHORT).show();
                }/*
              /*  mSaveOrLoadGame.saveGameField(mGameLogic.getFixedBlocks());
                Toast.makeText(getActivity(), "OK!", Toast.LENGTH_SHORT).show();*/
            }catch (Exception e) {
                Toast.makeText(getActivity(), "Error SAVE GAME", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean isSavedGame() {
            // проверяем существует ли сохраненная игра
            try {
                return mGameLogic.isSaveGame();
              /*  String path = getActivity().getApplicationInfo().dataDir + "/files/" + FILENAME;
                File config = new File(path);
                return config.exists();*/
            }
            catch (Exception e) {
                return false;
            }
        }

        private void loadGame() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mNickName = sharedPreferences.getString("NickName", getString(R.string.PLAYER));
            mNickNameTextView.setText(mNickName);
            if(mGameMode.equals("classic")) {
                mGameLogic.setScore(sharedPreferences.getInt("Score_Classic", 0));
                mGameLogic.setSpeed(sharedPreferences.getInt("Speed_Classic", 1));
                mGameLogic.setLevel(sharedPreferences.getInt("Level_Classic", 1));
            }
            else {
                mGameLogic.setScore(sharedPreferences.getInt("Score_openImage", 0));
                mGameLogic.setSpeed(sharedPreferences.getInt("Speed_openImage", 1));
                mGameLogic.setLevel(sharedPreferences.getInt("Level_openImage", 1));
            }

            mGameLogic.clearGameField();
            draw();

            mLevelUpTextView.setVisibility(View.VISIBLE);
            String text = getString(R.string.Level_up) + " " + mGameLogic.getLevel();
            mLevelUpTextView.setText(text);
            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                    R.animator.win_text_view_anim);
            set.setTarget(mLevelUpTextView);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    try {
                        if(mGameLogic.loadGameField()) {
                            startGame();
                            //      Toast.makeText(getActivity(), "LOAD IS OK!", Toast.LENGTH_SHORT).show();
                        }
                        else  Toast.makeText(getActivity(), "NO LOAD", Toast.LENGTH_SHORT).show();
                        //     mGameLogic.setFixedBlocks(mSaveOrLoadGame.loadGameField());

                        //     Toast.makeText(getActivity(), "LOAD IS OK!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        Toast.makeText(getActivity(), "Error LOAD GAME", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            set.start();
        }

        private void inputNickName() {
            if(!mGameLogic.getEndOfGame()) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }

                mGameLogic.pauseGame();

                FragmentManager fm = getFragmentManager();
                InputNickDialog dialog = new InputNickDialog();
                dialog.setTargetFragment(GameUIFragment.this, REQUEST_ACTION_IN_INPUT_NICK_DIALOG);
                dialog.setCancelable(false);
                dialog.show(fm, DIALOG_INPUT_NICK);
            }

        }

        private void inputNickNameForTableOfRecords() {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mGameLogic.pauseGame();

            FragmentManager fm = getFragmentManager();
            InputNickForTableOfRecordsDialog dialog = new InputNickForTableOfRecordsDialog();
            dialog.setTargetFragment(GameUIFragment.this,
                    REQUEST_ACTION_IN_INPUT_NICK_FOR_TABLE_OF_RECORDS_DIALOG);
            dialog.setCancelable(false);
            dialog.show(fm, DIALOG_INPUT_NICK_FOR_TABLE_OF_RECORDS);
        }

        private void updateTop10Dialog() {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mGameLogic.pauseGame();

            FragmentManager fm = getFragmentManager();
            Update_Top_10_Dialog dialog = new Update_Top_10_Dialog();
            dialog.setCancelable(false);
            dialog.show(fm, DIALOG_TOP_10);
        }

        private void top10Dialog() {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mGameLogic.pauseGame();

            FragmentManager fm = getFragmentManager();
            Top_10_Dialog dialog = new Top_10_Dialog();
            dialog.setCancelable(false);
            dialog.show(fm, DIALOG_TOP_10);
        }


        private void draw() {

            for (int i = 0; i < mNextFigureGameField.length; i++) {
                System.arraycopy(mGameLogic.getNextFigureGameField()[i], 0, mNextFigureGameField[i], 0, mNextFigureGameField[i].length);
                // ручное копирование массива заменено системным
              /*  for (int j = 0; j < mNextFigureGameField[i].length; j++) {
                    mNextFigureGameField[i][j] = mGameLogic.getNextFigureGameField()[i][j];
                }*/
            }

            for (int i = 0; i < mNextFigureTableLayout.getChildCount(); i++) {
                TableRow row = (TableRow) mNextFigureTableLayout.getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    ImageView imageview = (ImageView) row.getChildAt(j);
                    switch (mNextFigureGameField[i][j]) {
                        case 0:
                            imageview.setVisibility(View.INVISIBLE);
                            break;
                        case R.color.green:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_green);
                        //    imageview.setBackgroundResource(R.color.green);
                            break;
                        case R.color.yellow:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_yellow);
                          //  imageview.setBackgroundResource(R.color.yellow);
                            break;
                        case R.color.blue:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_blue);
                          //  imageview.setBackgroundResource(R.color.blue);
                            break;
                        case R.color.purple:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_purple);
                        //    imageview.setBackgroundResource(R.color.purple);
                            break;
                        default:
                            imageview.setVisibility(View.INVISIBLE);
                            break;
                    }
                }
            }

            for (int i = 0; i < mGameField.length; i++) {
                System.arraycopy(mGameLogic.getGameField()[i], 0, mGameField[i], 0, mGameField[i].length);
                // ручное копирование массива заменено системным
              /*  for (int j = 0; j < mGameField[i].length; j++) {
                    mGameField[i][j] = mGameLogic.getGameField()[i][j];
                }*/
            }

            for (int i = 0; i < mGameFieldTableLayout.getChildCount(); i++) {
                TableRow row = (TableRow) mGameFieldTableLayout.getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    ImageView imageview = (ImageView) row.getChildAt(j);
                    switch (mGameField[i][j]) {
                        case 0:
                            if (mGameMode.equals("openImage")) {
                                if (mGameLogic.getFullLinesForOpenImage()[i] != 1) {
                                    imageview.setVisibility(View.VISIBLE);
                                    imageview.setBackgroundResource(R.color.background_game_field);
                                }
                                else imageview.setVisibility(View.INVISIBLE);
                            } else imageview.setVisibility(View.INVISIBLE);
                            break;
                        case R.color.green:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_green);
                         //   imageview.setBackgroundResource(R.color.green);
                            break;
                        case R.color.yellow:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_yellow);
                         //   imageview.setBackgroundResource(R.color.yellow);
                            break;
                        case R.color.blue:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_blue);
                         //   imageview.setBackgroundResource(R.color.blue);
                            break;
                        case R.color.purple:
                            imageview.setVisibility(View.VISIBLE);
                            imageview.setBackgroundResource(R.drawable.shape_shadowed_purple);
                          //  imageview.setBackgroundResource(R.color.purple);
                            break;
                        default:
                            imageview.setVisibility(View.INVISIBLE);
                            break;
                    }
                }
            }

          //  if (mGameLogic.getGameMode().equals("openImage")) {
            if (mGameMode.equals("openImage")) {
             /*   int index = 0;
                for (int line : mGameLogic.getFullLinesForOpenImage()) {
                    if (line == 1) index++;
                }
                int lastLines = (mGameLogic.getLevel() + 6) - index;
                mThingTextView.setText("" + lastLines);*/

              //  for (int i = 0; i < mBorderLeftTableLayout.getChildCount(); i++) {
                for (int i = mBorderLeftTableLayout.getChildCount() - 1; i > 1; i--) {
                    TableRow row_leftBorder = (TableRow) mBorderLeftTableLayout.getChildAt(i);
                    ImageView imageview = (ImageView) row_leftBorder.getChildAt(0);
                    if (mGameLogic.getFullLinesForOpenImage()[i] == 1) {
                        imageview.setBackgroundResource(R.color.green);
                    }
                    else imageview.setBackgroundResource(R.color.red);

                    TableRow row_rightBorder = (TableRow) mBorderRightTableLayout.getChildAt(i);
                    imageview = (ImageView) row_rightBorder.getChildAt(0);
                    if (mGameLogic.getFullLinesForOpenImage()[i] == 1) {
                        imageview.setBackgroundResource(R.color.green);
                    }
                    else imageview.setBackgroundResource(R.color.red);
                }
            }


        }

        private void startTimer() {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }

            mTimer = new Timer();

            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            draw();

                            if (mGameMode.equals("openImage")) {
                                int index = 0;
                                for (int line : mGameLogic.getFullLinesForOpenImage()) {
                                    if (line == 1) index++;
                                }

                                int lastLines = (mGameLogic.getLevel() + 6) - index;
                                String text = "" + lastLines;
                                if (lastLines >= 0) mThingTextView.setText(text);
                                else  mThingTextView.setText("0");
                            }

                            String text = "" + mGameLogic.getLevel();
                            mLevelTextView.setText(text);
                            text = "" + mGameLogic.getScore();
                            mScoreTextView.setText(text);
                            text = "" + mGameLogic.getSpeed();
                            mSpeedTextView.setText(text);

                         /*   mLevelTextView.setText("" + mGameLogic.getLevel());
                            mScoreTextView.setText("" + mGameLogic.getScore());
                            mSpeedTextView.setText("" + mGameLogic.getSpeed());*/

                            if (mGameLogic.getEndOfGame()) stopGame();
                        }
                    });

                }
            }, 0,  mTimeInterval);
        }

    private void editSharedPreferences(int score, int speed, int level) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int maxLevelForOpenImageMode = sharedPreferences.getInt("MaxLevelForOpenImageMode", 1);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("NickName", mNickName);

        if(mGameMode.equals("classic")) {
            edit.putInt("Score_Classic", score);
            edit.putInt("Speed_Classic", speed);
            edit.putInt("Level_Classic", level);

        }
        else {
            edit.putInt("Score_openImage", score);
            edit.putInt("Speed_openImage", speed);
            edit.putInt("Level_openImage", level);

            if (mGameLogic.getLevel() > maxLevelForOpenImageMode) {
                edit.putInt("maxLevelForOpenImageMode", mGameLogic.getLevel());
            }

        }
        edit.apply();
    }


        @Override
        public void onPause() {
            super.onPause();
            if (!mIsPause) pauseGame();
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mGameLogic.getEndOfGame()) {
              //  mSaveOrLoadGame.deleteSavedGame();
                mGameLogic.deleteAllRowsInTableGameField();
                mGameLogic.deleteAllRowsInTableBorderGameField();
                editSharedPreferences(0, 1, 1);

            /*    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                int maxLevelForOpenImageMode = sharedPreferences.getInt("MaxLevelForOpenImageMode", 1);

                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("NickName", mNickName);

                if(mGameMode.equals("classic")) {
                    edit.putInt("Score_Classic", 0);
                    edit.putInt("Speed_Classic", 1);
                    edit.putInt("Level_Classic", 1);

                }
                else {
                    edit.putInt("Score_openImage", 0);
                    edit.putInt("Speed_openImage", 1);
                    edit.putInt("Level_openImage", 1);

                    if (mGameLogic.getLevel() > maxLevelForOpenImageMode) {
                        edit.putInt("maxLevelForOpenImageMode", mGameLogic.getLevel());
                    }

                }
                edit.apply();*/

            }
            else saveGame();
        }

        @Override
        public void onResume() {
            super.onResume();
          /*  if(isSavedGame()) {
                Toast.makeText(getActivity(), "EXIST SAVE GAME!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
            }*/
        }

}

