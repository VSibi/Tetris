package com.sibich.tetris;

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

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Slavon on 01.11.2016.
 */
public class GameUIFragment extends Fragment {

    private static final String DIALOG_PAUSE = "Pause";
    private static final String DIALOG_INPUT_NICK = "Input_nick";
    private static final String DIALOG_INPUT_NICK_FOR_TABLE_OF_RECORDS =
            "Input_nick_for_table_of_records";
    private static final String DIALOG_TOP_10 = "Top_10_dialog";

    private static final int REQUEST_ACTION_IN_PAUSE_DIALOG = 0;
    private static final int REQUEST_ACTION_IN_INPUT_NICK_DIALOG = 1;
    private static final int REQUEST_ACTION_IN_INPUT_NICK_FOR_TABLE_OF_RECORDS_DIALOG = 2;
    private static final int REQUEST_ACTION_IN_CONTINUE_SAVED_GAME_DIALOG = 3;

    private final int mWidthGameField = 10;
    private final int mHeightGameField = 20;

    private SaveOrLoadGame mSaveOrLoadGame;
    private static final String FILENAME = "gamefield.json";

    private int[][] mGameField =  new int [mHeightGameField][mWidthGameField];
    private int [][] mNextFigureGameField = new int[2][4];

    private Timer mTimer;
    private long mTimeInterval = 58;

    private GameLogic mGameLogic;

    private FrameLayout mFrameLayout;
    private TableLayout mGameFieldTableLayout, mNextFigureTableLayout;

    private TextView mSpeedTextView, mScoreTextView, mLevelTextView, mNickNameTextView;

    private ImageButton mSpeedMinus, mSpeedPlus, mGamePause, mInputNickName;

    private String mNickName = "PLAYER";
    private boolean isPause = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, parent, false);

        mGameLogic = new GameLogic(getActivity());
        mSaveOrLoadGame = new SaveOrLoadGame(getActivity(), FILENAME);

        mFrameLayout = (FrameLayout) v.findViewById(R.id.frame_layout);
        mGameFieldTableLayout = (TableLayout) v.findViewById(R.id.game_field_tablelayout);
        mNextFigureTableLayout = (TableLayout) v.findViewById(R.id.next_figure_game_field_tablelayout);

        mSpeedTextView = (TextView) v.findViewById(R.id.speed_textView);
        mScoreTextView = (TextView) v.findViewById(R.id.score_textView);
        mLevelTextView = (TextView) v.findViewById(R.id.level_textView);
        mNickNameTextView = (TextView) v.findViewById(R.id.nickNameTextView);

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

                        if (Math.abs(resultX) > 20.0) {
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
                            if (Math.abs(resultY) > 80.0) {
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
            Toast.makeText(getActivity(), "EXIST SAVE GAME!", Toast.LENGTH_SHORT).show();
        }
        else {
            loadGame();
            startGame();
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
                    startGame();
                    break;
                case "new_game":
                    newGame();
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
                    isPause = false;
                    break;
                case "new_game":
                    newGame();
                    isPause = false;
                    break;
                case "records":
                    Intent i = new Intent(getActivity(), TableOfRecordsActivity.class);
                    startActivity(i);
                    isPause = false;
                    break;
                case "help":
                    Intent j = new Intent(getActivity(), HelpActivity.class);
                    startActivity(j);
                    isPause = false;
                    break;
                case "exit":
                    getActivity().onBackPressed();
                    isPause = false;
                    break;



            }
        }
    }


    private void startGame() {

        startTimer();
        mGameLogic.startTimer();
    }

    private void pauseGame(){
        isPause = true;
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
        mSpeedTextView.setText(R.string.Game_over);
        if (mGameLogic.isTopTenResult()) {
            if (mNickName.equals(getString(R.string.PLAYER))) inputNickNameForTableOfRecords();
            else {
                if (mGameLogic.updateTableOfRecords(mNickName)) updateTop10Dialog();
                else top10Dialog();
            }
        }
    }

 /*   private void levelUp() {}*/

    private void newGame() {
        mGameLogic.newGame();
        startTimer();
    }

    public void saveGame() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("NickName", mNickName);
        edit.putInt("Score", mGameLogic.getScore());
        edit.putInt("Speed", mGameLogic.getSpeed());
        edit.apply();

        try {
            mSaveOrLoadGame.saveGameField(mGameLogic.getFixedBlocks());
            Toast.makeText(getActivity(), "OK!", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Error SAVE file", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isSavedGame() {
        // проверяем существует ли файл с сохранениеями игры
        try {
        String path = getActivity().getApplicationInfo().dataDir + "/files/" + FILENAME;
        File config = new File(path);
        return config.exists();
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean loadGame() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mNickName = sharedPreferences.getString("NickName", "PLAYER");
        mNickNameTextView.setText(mNickName);
        mGameLogic.setScore(sharedPreferences.getInt("Score", 0));
        mGameLogic.setSpeed(sharedPreferences.getInt("Speed", 1));

        try {
            mGameLogic.setFixedBlocks(mSaveOrLoadGame.loadGameField());

            Toast.makeText(getActivity(), "LOAD IS OK!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void backToMenu() {}

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
            for (int j = 0; j < mNextFigureGameField[i].length; j++) {
                mNextFigureGameField[i][j] = mGameLogic.getNextFigureGameField()[i][j];
            }
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
                        imageview.setBackgroundResource(R.color.green);
                        break;
                    case R.color.yellow:
                        imageview.setVisibility(View.VISIBLE);
                        imageview.setBackgroundResource(R.color.yellow);
                        break;
                    case R.color.blue:
                        imageview.setVisibility(View.VISIBLE);
                        imageview.setBackgroundResource(R.color.blue);
                        break;
                    case R.color.purple:
                        imageview.setVisibility(View.VISIBLE);
                        imageview.setBackgroundResource(R.color.purple);
                        break;
                    default:
                        imageview.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }

        for (int i = 0; i < mGameField.length; i++) {
            for (int j = 0; j < mGameField[i].length; j++) {
                mGameField[i][j] = mGameLogic.getGameField()[i][j];
            }
        }

        for (int i = 0; i < mGameFieldTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) mGameFieldTableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView imageview = (ImageView) row.getChildAt(j);
                switch (mGameField[i][j]) {
                    case 0:
                        imageview.setVisibility(View.INVISIBLE);
                        break;
                    case R.color.green:
                        imageview.setVisibility(View.VISIBLE);
                        imageview.setBackgroundResource(R.color.green);
                        break;
                    case R.color.yellow:
                        imageview.setVisibility(View.VISIBLE);
                        imageview.setBackgroundResource(R.color.yellow);
                        break;
                    case R.color.blue:
                        imageview.setVisibility(View.VISIBLE);
                        imageview.setBackgroundResource(R.color.blue);
                        break;
                    case R.color.purple:
                        imageview.setVisibility(View.VISIBLE);
                        imageview.setBackgroundResource(R.color.purple);
                        break;
                    default:
                        imageview.setVisibility(View.INVISIBLE);
                        break;
                }
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

                        mLevelTextView.setText("" + mGameLogic.getLevel());
                        mScoreTextView.setText("" + mGameLogic.getScore());
                        mSpeedTextView.setText("" + mGameLogic.getSpeed());

                        if (mGameLogic.getEndOfGame()) stopGame();
                    }
                });

            }
        }, 0,  mTimeInterval);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isPause) pauseGame();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGameLogic.getEndOfGame()) {
            mSaveOrLoadGame.deleteSavedGame();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("NickName", mNickName);
            edit.putInt("Score", 0);
            edit.putInt("Speed", 1);
            edit.apply();
        }
        else saveGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isSavedGame()) {
            Toast.makeText(getActivity(), "EXIST SAVE GAME!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }


}
