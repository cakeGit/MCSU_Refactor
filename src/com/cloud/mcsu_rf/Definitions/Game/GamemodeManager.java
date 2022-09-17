package com.cloud.mcsu_rf.Definitions.Game;

import java.util.ArrayList;

public class GamemodeManager {

    private final GamemodeOptionBlock[] optionBlocks;

    public GamemodeManager(GamemodeOptionBlock... optionBlocks) {

        this.optionBlocks = optionBlocks;

    }

    public ArrayList<GamemodeOptionChoice> pickOptions() {

        ArrayList<GamemodeOptionChoice> options = new ArrayList<>();

        for ( GamemodeOptionBlock optionBlock : optionBlocks ) {
            options.add(optionBlock.pickGamemode());
        }

        return options;

    }

}
