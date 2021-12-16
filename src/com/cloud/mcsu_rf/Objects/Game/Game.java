package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.Map.MapLoader;
import com.cloud.mcsu_rf.Objects.Map.SpawnManager;
import com.cloud.mcsu_rf.Objects.Timer;
import com.cloud.mcsu_rf.ShorthandClasses.Pick;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {

    public static String generateEndSplash() {

        return Pick.Random(
                "wow what a sweat lol",
                "*clap* *clap* *clap* *clap*",
                "lets gooo",
                "#fuhamroadredemption",
                "mcsu????",
                "pog????/?/???",
                ":D",
                "im happy for them",
                "gg",
                "i like ya cut g",
                "ok but who asked",
                "ratio",
                "very enterprising",
                "very christian values",
                "Mr Ebenezer approves",
                "Mr Tice approves",
                "i think that deserves a housepoint",
                "John Davison could calculate ur moms radius better than u"
                );

    }

    //defaults
    static int DefaultStartLength = 15;
    static Sound DefaultStartTimerTickSound = Sound.BLOCK_NOTE_BLOCK_SNARE;
    static Sound DefaultStartTimerEndSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;


    String Name;
    boolean startIntervalEnabled;
    MapLoader mapLoader;

    ArrayList<GameState> gameStates = new ArrayList<>();
    ArrayList<McsuPlayer> alivePlayers = new ArrayList<>();

    public Game(String Name) {

        this.Name = Name;

        Bukkit.getLogger().info("Registering game " + Name);

        Game_Main.RegisteredGames.add(this);

    }

    public void initGameLoader(World world) {

        if (this.mapLoader == null) {
            this.mapLoader = new MapLoader().setSpawnManager(new SpawnManager()); // defaults to generic map + spawn loaders
        }

        this.mapLoader.MapInit(this, world);
        this.mapLoader.getSpawnManager().lobbySpawns(this.mapLoader);

        this.alivePlayers = McsuPlayer.McsuPlayers;

        GameInitEvent event = new GameInitEvent(this);
        Bukkit.getPluginManager().callEvent(event);

       for (GameState gameState : this.gameStates) { // trust me this has a purpose
            if ( gameState.getEnabled() ) {
                gameState.setEnabled(true);
           }
        }

        if (this.startIntervalEnabled) {
            Timer startTimer = new Timer(-1, DefaultStartLength)
                    .setOnTickIncrease(timer -> {

                        for (Player player : Bukkit.getOnlinePlayers()) {


                            String message = ChatColor.WHITE + "" + ChatColor.BOLD + this.Name +
                                    ChatColor.RESET + "" + ChatColor.RED + " starting in " + timer.getTimeLeft();

                            if (timer.getTimeLeft() > 0) {
                                if (timer.getTimeLeft() <= 5) {
                                    if (timer.getTimeLeft() == 5) { // Will run once
                                        this.mapLoader.getSpawnManager().gameSpawns(this.mapLoader);
                                        EventListenerMain.setActivityRule("PlayerMovement", false);
                                    }
                                    player.sendTitle(message, "Get ready!");
                                    player.playSound(player.getLocation(), DefaultStartTimerTickSound, 1, 1);
                                } else {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                                }
                            }

                        }

                    })
                    .setOnTimerEnd(timer -> {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), DefaultStartTimerEndSound, 1, 1);
                        }

                        this.gameCountdownEnd();

                    });

        }

    }

    public void gameCountdownEnd() {
        EventListenerMain.setActivityRule("PlayerMovement", true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "GO!", "");
        }

        GameCountdownEndEvent event = new GameCountdownEndEvent(this);
        Bukkit.getPluginManager().callEvent(event);

    }

    public void genericGameEnd(McsuPlayer winner) {
        for (GameState gameState : this.gameStates) {
            gameState.setEnabled(false);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            String style = ChatColor.BOLD + "" + ChatColor.WHITE;
            player.sendTitle(winner.getColouredName() + style + " has won " + ChatColor.RESET + this.getName() + style + "!1!!!11!!!!!1!!11", generateEndSplash());
        }

        EventListenerMain.resetActivityRules();
    }

    public Game addGameState(GameState gameState) { this.gameStates.add(gameState); return this; }
    public Game setMapLoader(MapLoader mapLoader) { this.mapLoader = mapLoader; return this; }
    public Game addStartInterval() { this.startIntervalEnabled = true; return this; }


    public ArrayList<McsuPlayer> getAlivePlayers() { return this.alivePlayers; }
    public void removeFromAlivePlayers(McsuPlayer player) { this.alivePlayers.remove(player); }
    public String getName() { return this.Name; }

    public ArrayList<GameState> getEnabledGameStates() {

        ArrayList<GameState> enabled = new ArrayList<>();
        this.gameStates.stream().filter(gameState -> gameState.enabled).forEach(enabled::add);
        return enabled;

    }

}
