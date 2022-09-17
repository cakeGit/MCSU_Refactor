package com.cloud.mcsu_rf.Definitions.GameFunctions;

import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Lambdas.StringReturnMcsuPlayerLambda;

public class GamePrefixText extends GameFunctionBase {

    StringReturnMcsuPlayerLambda prefixFunc;

    public GamePrefixText(StringReturnMcsuPlayerLambda func) {
        prefixFunc = func;
    }

    public void onEnabledToggle(boolean enabled) {
        Game.setGameSetPrefixFunc((enabled ? prefixFunc : null));
    }

}
