package com.sibich.tetris.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sibich.tetris.database.TetrisDbSchema.BorderGameFieldTable;
import com.sibich.tetris.database.TetrisDbSchema.RecordsTable;
import com.sibich.tetris.database.TetrisDbSchema.GameFieldForOpenImageTable;
import com.sibich.tetris.database.TetrisDbSchema.GameFieldForClassicTable;

/**
 * Created by Slavon on 02.12.2016.
 */
public class TetrisBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recordsBase.db";

    public TetrisBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + BorderGameFieldTable.NAME  + "("
                + " _id integer primary key autoincrement, "
                + BorderGameFieldTable.Cols.BLOCK +
                ");"

        );

        db.execSQL("create table " + RecordsTable.NAME  + "("
                + " _id integer primary key autoincrement, "
                + RecordsTable.Cols.NICKNAME + ", "
                + RecordsTable.Cols.SCORE +
                ");"

        );

        db.execSQL("create table " + GameFieldForClassicTable.NAME  + "("
                + " _id integer primary key autoincrement, "
                + GameFieldForClassicTable.Cols.COORD1_X + ", "
                + GameFieldForClassicTable.Cols.COORD1_Y + ", "
                + GameFieldForClassicTable.Cols.COORD2_X + ", "
                + GameFieldForClassicTable.Cols.COORD2_Y + ", "
                + GameFieldForClassicTable.Cols.COORD3_X + ", "
                + GameFieldForClassicTable.Cols.COORD3_Y + ", "
                + GameFieldForClassicTable.Cols.COORD4_X + ", "
                + GameFieldForClassicTable.Cols.COORD4_Y + ", "

                + GameFieldForClassicTable.Cols.COLOR +
                ");"

        );

        db.execSQL("create table " + GameFieldForOpenImageTable.NAME  + "("
                + " _id integer primary key autoincrement, "
                + GameFieldForOpenImageTable.Cols.COORD1_X + ", "
                + GameFieldForOpenImageTable.Cols.COORD1_Y + ", "
                + GameFieldForOpenImageTable.Cols.COORD2_X + ", "
                + GameFieldForOpenImageTable.Cols.COORD2_Y + ", "
                + GameFieldForOpenImageTable.Cols.COORD3_X + ", "
                + GameFieldForOpenImageTable.Cols.COORD3_Y + ", "
                + GameFieldForOpenImageTable.Cols.COORD4_X + ", "
                + GameFieldForOpenImageTable.Cols.COORD4_Y + ", "

                + GameFieldForOpenImageTable.Cols.COLOR +
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
