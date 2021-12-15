package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListener_Main;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.CustomEventListener;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.PointAwarder;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Spleef {

    Game game;
    GameState gameStates;

    public void init() {

        this.game = new Game("Spleef")
                .addStartInterval()
                .addGameState(
                        new GameState("main", true)
                                .addGameFunction(new PointAwarder("Survival", 2))
                                .addGameFunction(new CustomEventListener("PlayerDeathEvent", Event -> {
                                    game.removeFromAlivePlayers(MCSU_Player.getPlayerByBukkitPlayer(((PlayerDeathEvent) Event).getEntity()));
                                    checkIfEnded();
                                }))
                                .addGameFunction(new CustomEventListener("GameInitEvent", Event -> {
                                    EventListener_Main.setActivityRule("PVP", true);
                                }))
                );

    }

    public void checkIfEnded() {
        if (this.game.getAlivePlayers().size() == 1) {
            Bukkit.getServer().broadcastMessage("game end???");
        }
    }
}
