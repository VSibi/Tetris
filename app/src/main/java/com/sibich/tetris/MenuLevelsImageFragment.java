package com.sibich.tetris;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Slavon on 13.03.2017.
 */
public class MenuLevelsImageFragment extends Fragment {

    private int [] mWinLevelImages = {
            R.drawable.win_image_level_1_for_menu, R.drawable.win_image_level_2_for_menu,
            R.drawable.win_image_level_3_for_menu, R.drawable.win_image_level_4_for_menu,
            R.drawable.win_image_level_1_for_menu, R.drawable.win_image_level_2_for_menu,
            R.drawable.win_image_level_3_for_menu, R.drawable.win_image_level_4_for_menu,
            R.drawable.win_image_level_1_for_menu, R.drawable.win_image_level_2_for_menu
    };

    private static final String DIALOG_WINIMAGE = "WinImage_dialog";

    private TableLayout mLevelsImageTableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_levels_image, parent, false);

        mLevelsImageTableLayout = (TableLayout) v.findViewById(R.id.menu_levels_image_tablelayout);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int mMaxLevel = sharedPreferences.getInt("maxLevelForOpenImageMode", 1);

        int number = 1;
        for (int i = 0; i < mLevelsImageTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow)mLevelsImageTableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView imageView = (ImageView) row.getChildAt(j);
                if (number < mMaxLevel) {
                    imageView.setBackgroundResource(mWinLevelImages[number - 1]);
                    imageView.setOnClickListener(new MyImageClickListener(number));
                }
                number++;
            }
        }



        return v;
    }













protected class MyImageClickListener implements View.OnClickListener {

    private int mLevel;

    public MyImageClickListener(int level) {
        mLevel = level;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        WinImageDialog dialog = WinImageDialog.newInstance(mLevel);
        dialog.setCancelable(false);
        dialog.show(fm, DIALOG_WINIMAGE);
    }
}



}
