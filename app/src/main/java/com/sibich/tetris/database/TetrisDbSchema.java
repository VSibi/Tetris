package com.sibich.tetris.database;

/**
 * Created by Slavon on 02.12.2016.
 */
public class TetrisDbSchema {
    public static final class RecordsTable {
        public static final String NAME = "records";

        public static final class Cols {
            public static final String NICKNAME = "nickname";
            public static final String SCORE = "score";
        }
    }
}
