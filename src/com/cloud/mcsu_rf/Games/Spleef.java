package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.PointAwarder;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;

public class Spleef {

    Game game;
    GameState gameStates;

    public void init() {

        this.game = new Game("Spleef")
                .setMapManager("basic", "spleef")
                .addStartInterval()
                .addGameState(
                        new GameState("main", true)
                                .addGameFunction(new PointAwarder("Survival", 2))
                );

    }
}
