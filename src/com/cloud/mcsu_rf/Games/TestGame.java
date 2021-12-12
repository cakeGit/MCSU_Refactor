package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.PointAwarder;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;

public class TestGame {

    Game game;
    GameState gameStates;

    public void init() {



        this.game = new Game("testgame")
                .setMapName("testgame")
                .addStartInterval()
                .addGameState(
                        new GameState("main", true)
                        .addGameFunction( new PointAwarder("Survival", 2) )

                );

    }

}
