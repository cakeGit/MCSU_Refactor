package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.EventListener_Main;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Game_Handlers.Schematic_Loader;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Objects.Lambdas.MapLoaderLambda;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
import com.cloud.mcsu_rf.Objects.Map.MapLoader;
import com.cloud.mcsu_rf.Objects.Map.SpawnManager;
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

    //defaults
    static int DefaultStartLength = 15;
    static Sound DefaultStartTimerTickSound = Sound.BLOCK_NOTE_BLOCK_SNARE;
    static Sound DefaultStartTimerEndSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;


    String Name;
    boolean startIntervalEnabled;
    MapLoader mapLoader;

    ArrayList<GameState> gameStates = new ArrayList<>();
    ArrayList<MCSU_Player> alivePlayers = new ArrayList<>();

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

        for (GameState gameState : getEnabledGameStates()) {
            gameState.setEnabled(true);
        }

        this.alivePlayers = MCSU_Player.MCSU_Players;

        GameInitEvent event = new GameInitEvent(this);
        Bukkit.getPluginManager().callEvent(event);

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
                                        EventListener_Main.setActivityRule("PlayerMovement", false);
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
        EventListener_Main.setActivityRule("PlayerMovement", true);

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "GO!", "");
        }

        GameCountdownEndEvent event = new GameCountdownEndEvent(this);
        Bukkit.getPluginManager().callEvent(event);

    }

    public Game addGameState(GameState gameState) { this.gameStates.add(gameState); return this; }
    public Game setMapLoader(MapLoader mapLoader) { this.mapLoader = mapLoader; return this; }
    public Game addStartInterval() { this.startIntervalEnabled = true; return this; }


    public ArrayList<MCSU_Player> getAlivePlayers() { return this.alivePlayers; }
    public void removeFromAlivePlayers(MCSU_Player player) { this.alivePlayers.remove(player); }
    public String getName() { return this.Name; }

    public ArrayList<GameState> getEnabledGameStates() {

        ArrayList<GameState> enabled = new ArrayList<>();
        this.gameStates.stream().filter(gameState -> gameState.enabled).forEach(enabled::add);
        return enabled;

    }

}
