package com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses;

import java.util.ArrayList;

public class ParseArr {
    public static Integer[] toInteger(String[] arr) {

        ArrayList<Integer> arrList = new ArrayList<>();
        for (String str : arr) {
            arrList.add(Integer.parseInt(str));
        }

        return arrList.toArray(new Integer[0]);
    }
    public static Double[] toDouble(String[] arr) {

        ArrayList<Double> arrList = new ArrayList<>();
        for (String str : arr) {
            arrList.add(Double.parseDouble(str));
        }

        return arrList.toArray(new Double[0]);
    }
}
