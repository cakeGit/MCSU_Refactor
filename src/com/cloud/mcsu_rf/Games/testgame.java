package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.PointAwarder;
import com.cloud.mcsu_rf.Objects.Game;
import com.cloud.mcsu_rf.Objects.Game_Component;

public class testgame {

    public static void Init() {
        Game game = new Game("testgame");
        game.setMapName("testgame");
        game.addGameComponent(new Game_Component(new PointAwarder("Survival", 2)));
    }

}
