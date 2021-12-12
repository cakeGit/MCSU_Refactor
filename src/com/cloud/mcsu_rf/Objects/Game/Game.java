package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Game_Handlers.Schematic_Loader;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.GameFunction;
import com.cloud.mcsu_rf.Objects.Timer;
import com.sk89q.worldedit.math.BlockVector3;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {

    int DefaultStartLength = 15;
    Sound DefaultStartTimerSound = Sound.BLOCK_NOTE_BLOCK_SNARE;
    Sound DefaultStartTimerEndSound = Sound.BLOCK_BEACON_ACTIVATE;

    String Name;
    boolean startIntervalEnabled;
    String[] Maps = new String[1];
    ArrayList<GameState> gameStates = new ArrayList();

    public Game(String Name) {

        this.Name = Name;

        Bukkit.getLogger().info("Loading game " + Name);

        Game_Main.RegisteredGames.add(this);

    }


    public Game addGameState(GameState gameState) { this.gameStates.add(gameState); return this;}


    public String getName() { return this.Name; }

    public ArrayList<GameState> getEnabledGameStates() {

        ArrayList<GameState> enabled = new ArrayList<>();
        gameStates.stream().filter(gameState -> gameState.enabled).forEach(enabled::add);
        return enabled;

    }

    public Game addStartInterval() { startIntervalEnabled = true; return this; }

    public void startGame(World world) {

        Schematic_Loader.loadSchematic(this.Maps[0], BlockVector3.at(0, 100, 0), world);
        if (startIntervalEnabled) {
            Timer startTimer = new Timer(-1, DefaultStartLength)
                    .setOnTickIncrease(timer -> {

                       for (Player player : Bukkit.getOnlinePlayers()) {
                           player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                   new TextComponent(
                                           ChatColor.WHITE+
                                           this.getName()+
                                           " starting in "+
                                           timer.getTimeLeft()
                                   ));
                       }

                    });
        }

        for (GameState gameState : getEnabledGameStates()) {
            for (GameFunction gameFunction : gameState.getGameFunctions()) {
                gameFunction.enableFunction();
            }
        }

    }


    //depracated stuff - TODO:Remove

    @Deprecated public Game setMapName(String Map_Name) { this.Maps[0] = Map_Name; return this;} // add a mapmanager thing
}
