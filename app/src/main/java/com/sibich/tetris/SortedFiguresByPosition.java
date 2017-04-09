package com.sibich.tetris;

import java.util.Comparator;

/**
 * Created by Sibic_000 on 06.01.2017.
 */
public class SortedFiguresByPosition implements Comparator<Figure> {

    public int compare(Figure obj1, Figure obj2) {

        Integer y1 = obj1.getAllCoords().get(obj1.getAllCoords().size() - 1).y;
        Integer y2 = obj2.getAllCoords().get(obj2.getAllCoords().size() - 1).y;

        return y1.compareTo(y2);

     /*   if(y1 > y2) {
            return 1;
        }
        else {
            if(y1 < y2) {
                return -1;
            }
            else return 0;
        }*/

    }

}
