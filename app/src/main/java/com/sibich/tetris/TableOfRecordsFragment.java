package com.sibich.tetris;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sibich.tetris.database.TetrisBaseHelper;
import com.sibich.tetris.database.TetrisDbSchema.RecordsTable;

/**
 * Created by Sibic_000 on 04.12.2016.
 */
public class TableOfRecordsFragment extends Fragment {

    private SQLiteDatabase mDataBase;

    private TableLayout mRecordsTable;
    private ImageButton mBackButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        mDataBase = new TetrisBaseHelper(getActivity())
                .getWritableDatabase();

        View v = inflater.inflate(R.layout.fragment_table_of_records, parent, false);

        mRecordsTable = (TableLayout)v.findViewById(R.id.records_tablelayout);

        readFromTableOfRecords();

        mBackButton = (ImageButton) v.findViewById(R.id.tableOfRecordsBackImageButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    public void readFromTableOfRecords() {

        int numRecords = 0;
        String orderBy = RecordsTable.Cols.SCORE + " DESC";
        // делаем запрос всех данных из таблицы records, получаем Cursor
        Cursor c = mDataBase.query(RecordsTable.NAME, null, null, null, null, null, orderBy);

        // проверяем количество записей в таблице
        if( c.getCount() < mRecordsTable.getChildCount()) {
            numRecords = (c.getCount() + 1);
        }
        else {
            numRecords = mRecordsTable.getChildCount();
        }
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
          //  int idColIndex = c.getColumnIndex("_id");
            int nickNameColIndex = c.getColumnIndex(RecordsTable.Cols.NICKNAME);
            int scoreColIndex = c.getColumnIndex(RecordsTable.Cols.SCORE);

            for (int i = 1; i < numRecords; i++) {
                TableRow row = (TableRow)mRecordsTable.getChildAt(i);

                TextView textview_index = (TextView)row.getChildAt(0);
                textview_index.setText(Integer.toString(i));

                TextView textview_Nick = (TextView)row.getChildAt(1);
                textview_Nick.setText(c.getString(nickNameColIndex));

                TextView textview_Score = (TextView)row.getChildAt(2);
                textview_Score.setText(Integer.toString(c.getInt(scoreColIndex)));

                c.moveToNext();

            }
        }

        c.close();

    }

}
