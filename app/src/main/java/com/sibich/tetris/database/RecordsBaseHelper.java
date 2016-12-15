package com.sibich.tetris.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sibich.tetris.database.TetrisDbSchema.RecordsTable;

/**
 * Created by Slavon on 02.12.2016.
 */
public class RecordsBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recordsBase.db";

    public RecordsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RecordsTable.NAME  + "("
                + " _id integer primary key autoincrement, "
                + RecordsTable.Cols.NICKNAME + ", "
                + RecordsTable.Cols.SCORE +
                ");"

        );

      /*  db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text" + ");");*/



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
