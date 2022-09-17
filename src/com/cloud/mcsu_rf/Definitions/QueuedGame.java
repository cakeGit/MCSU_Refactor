package com.cloud.mcsu_rf.Definitions;

import java.util.HashMap;

public class QueuedGame {

    String gameName;
    HashMap<String, String> options = new HashMap<>();

    public QueuedGame(String gameName, HashMap<String, String> options) {
        this.gameName = gameName;
        this.options = options;
    }

    public String getGameName() {
        return gameName;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOption(String option, String value) {
        this.options.put(option, value);
    }
}
