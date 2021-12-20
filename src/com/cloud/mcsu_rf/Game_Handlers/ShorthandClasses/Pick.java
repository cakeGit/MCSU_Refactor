package com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses;

import java.util.ArrayList;
import java.util.Random;

public class Pick {

    @SafeVarargs
    public static final <T> T Random(T... items) {
        return items[new Random().nextInt(items.length)];
    }

    public static <T> T Random(ArrayList<T> item) {
        return item.get(new Random().nextInt(item.size()));
    }

}
