package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.Game_Handlers.GameManager;
import com.cloud.mcsu_rf.Game_Handlers.GameState;
import com.cloud.mcsu_rf.Objects.Game;

public class testgame {

    private static GameManager gameManager;

    public static void Init() {
        Game game = new Game("testgame");
        game.setMapName("testgame");
        gameManager.setGameState(GameState.STARTING);
    }

}
