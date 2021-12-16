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
                                    this.game.removeFromAlivePlayers(
                                            McsuPlayer.getPlayerByBukkitPlayer(
                                                    ( (PlayerDeathEvent) Event ).getEntity()
                                            )
                                    );
                                    this.game.checkAliveTeams( true );
                                    checkIfEnded();
                                }))
                                .addGameFunction(new CustomEventListener("GameInitEvent", Event -> {
                                    Bukkit.getLogger().info("Checking if ss");
                                    EventListenerMain.setActivityRule("PVP", true);
                                }))
                                .addGameFunction(new CustomEventListener("GameCountdownEndEvent", Event -> {
                                    checkIfEnded();
                                }))
                );

    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + this.game.getName() + " has ended");

        if (this.game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + this.game.getName() + " has ended!");

            this.game.getAliveTeams().get(0).awardTeamPoints(100);

            this.game.defaultGameEnd(this.game.getAliveTeams().get(0));

        } else  {

            Bukkit.getLogger().info(this.game.getName() + " has not ended because " + this.game.getAliveTeams().size()
                    + " teams are still alive");

        }

    }
}
