package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.CustomEventListener;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.PointAwarder;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
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
                                    this.game.removeFromAlivePlayers(McsuPlayer.getPlayerByBukkitPlayer(((PlayerDeathEvent) Event).getEntity()));
                                    checkIfEnded();
                                }))
                                .addGameFunction(new CustomEventListener("GameInitEvent", Event -> {
                                    Bukkit.getLogger().info("Checking if ss");
                                    EventListenerMain.setActivityRule("PVP", true);
                                }))
                                .addGameFunction(new CustomEventListener("GameCountdownEndEvent", Event -> {
                                    Bukkit.getLogger().info("Checking if game 'Spleef' has ended");
                                    checkIfEnded();
                                }))
                );

    }

    public void checkIfEnded() {
        if (this.game.getAlivePlayers().size() == 1) {

            this.game.genericGameEnd(this.game.getAlivePlayers().get(0));

        }
    }
}
