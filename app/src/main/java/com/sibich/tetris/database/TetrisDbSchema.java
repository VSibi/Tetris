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

    public static final class GameFieldTable {
        public static final String NAME = "gameField";

        public static final class Cols {
            public static final String COORD1_X = "coord1_x";
            public static final String COORD1_Y = "coord1_y";
            public static final String COORD2_X = "coord2_x";
            public static final String COORD2_Y = "coord2_y";
            public static final String COORD3_X = "coord3_x";
            public static final String COORD3_Y = "coord3_y";
            public static final String COORD4_X = "coord4_x";
            public static final String COORD4_Y = "coord4_y";
            public static final String COLOR = "color";
        }
    }

}
