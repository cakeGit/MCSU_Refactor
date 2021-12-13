package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.CustomEventListener;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.PointAwarder;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.swing.plaf.basic.BasicIconFactory;

public class TestGame {

    Game game;
    GameState gameStates;

    public void init() {

        this.game = new Game("testgame")
                .setMapManager("basic", "testgame")
                .addStartInterval()
                .addGameState(
                        new GameState("main", true)
                                .addGameFunction(new PointAwarder("Survival", 2))
                                .addGameFunction(new CustomEventListener("PlayerDeathEvent", Event -> {
                                    game.removeFromAlivePlayers(MCSU_Player.getPlayerByBukkitPlayer(((PlayerDeathEvent) Event).getEntity()));
                                    Bukkit.getServer().broadcastMessage(((PlayerDeathEvent) Event).getEntity().getDisplayName());
                                    Bukkit.getServer().broadcastMessage("skmaosjdfnlkasdflkhb");
                                    checkIfEnded();
                                }))
                );

    }

    public void checkIfEnded() {
        if (this.game.getAlivePlayers().size() == 1) {
            Bukkit.getServer().broadcastMessage("game end???");
        }
    }
}
