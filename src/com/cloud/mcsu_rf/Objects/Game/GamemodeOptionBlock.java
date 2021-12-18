package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Objects.Enums.GamemodeOptionChoiceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamemodeOptionBlock {

    public static boolean numberInside(double a, double b, double point) { //copied from actionzone lol
        return Math.abs(a-point) + Math.abs(b-point) == Math.abs(a-b);
    }

    static GamemodeOption chooseOption( ArrayList<GamemodeOption> options ) {
        int totalWeight = 0;

        for ( GamemodeOption option : options ) {
            totalWeight += option.getWeight();
        }

        int pickedInt = new Random().nextInt(totalWeight);

        int currentWeight = 0;

        for ( GamemodeOption option : options ) {
            if ( numberInside( currentWeight, currentWeight+option.getWeight(), pickedInt ) ) {
                return option;
            }

            currentWeight += option.getWeight();
        }

        return null;

    }

    GamemodeOption[] gamemodeOptions;
    GamemodeOptionChoiceType optionBlockType;
    String optionBlockName;
    int pickNoneWeight = 0;

    public GamemodeOptionBlock(String optionBlockName, GamemodeOption... gamemodeOptions) {

        this.optionBlockName = optionBlockName;
        this.gamemodeOptions = gamemodeOptions;

        if (gamemodeOptions.length > 1) {
            optionBlockType = GamemodeOptionChoiceType.ListChoice;
        } else {
            optionBlockType = GamemodeOptionChoiceType.BoolChoice;
        }

    }

    public GamemodeOptionBlock(String optionBlockName,  int pickNoneWeight, GamemodeOption... gamemodeOptions) {

        this.optionBlockName = optionBlockName;
        this.gamemodeOptions = gamemodeOptions;
        this.pickNoneWeight = pickNoneWeight;

        if (gamemodeOptions.length > 1) {
            optionBlockType = GamemodeOptionChoiceType.ListChoice;
        } else {
            optionBlockType = GamemodeOptionChoiceType.BoolChoice;
        }

    }

    public GamemodeOptionChoice pickGamemode() {

        ArrayList<GamemodeOption> gamemodeOptionsClone = new ArrayList<>(List.of(gamemodeOptions.clone()));

        gamemodeOptionsClone.add(
                new GamemodeOption(pickNoneWeight, "None")
        );

        return new GamemodeOptionChoice( chooseOption(gamemodeOptionsClone), optionBlockType, optionBlockName);

    }

}
