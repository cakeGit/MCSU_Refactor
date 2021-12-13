package com.cloud.mcsu_rf.ShorthandClasses;

import java.util.Random;

public class Pick {

    @SafeVarargs
    public static final <T> T Random(T... items) {
        return items[new Random().nextInt(items.length)];
    }

}
