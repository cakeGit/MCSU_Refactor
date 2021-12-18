package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Objects.Enums.GamemodeOptionChoiceType;

public class GamemodeOptionChoice {

    GamemodeOption gamemodeOption;
    GamemodeOptionChoiceType optionBlockType;
    String optionBlockName;

    public GamemodeOptionChoice(GamemodeOption gamemodeOption, GamemodeOptionChoiceType optionBlockType, String optionBlockName) {

        this.gamemodeOption = gamemodeOption;
        this.optionBlockType = optionBlockType;
        this.optionBlockName = optionBlockName;

    }

    public GamemodeOption getGamemodeOption() { return gamemodeOption; }
    public GamemodeOptionChoiceType getOptionBlockType() { return optionBlockType; }
    public String getOptionBlockName() { return optionBlockName; }

}
