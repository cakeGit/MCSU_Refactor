package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameSpawnsActivatedEvent;
import com.cloud.mcsu_rf.Objects.Map.MapMetadata;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.Map.MapLoader;
import com.cloud.mcsu_rf.Objects.Map.SpawnManager;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements.MapMetadataDisplay;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.Objects.Timer;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Pick;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Objects;

public class Game {

    public static String generateEndSplash() {

        return Pick.Random(
                "wow what a sweat lol",
                "*clap* *clap* *clap* *clap*",
                "mcsu????",
                "pog????/?/???",
                ":D",
                "gg",
                "very enterprising",
                "very christian values",
                "Mr Ebenezer approves",
                "Mr Tice approves",
                "i think that deserves a housepoint",
                "my wife has filed for a divorce",
                "very mcsu",
                "sus like impostor"
                );

    }

    public static String generateNoWinnerEndSplash() {
        return  "";
        //return Pick.Random("");
    }

    //defaults
    static final int DefaultStartLength = 15;
    public static final Sound DefaultGamemodePickSound = Sound.BLOCK_NOTE_BLOCK_PLING;
    public static final Sound DefaultStartTimerTickSound = Sound.BLOCK_NOTE_BLOCK_SNARE;
    public static final Sound DefaultStartTimerEndSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;
    public static boolean gameActive = false;
    public static MapMetadata mapMetadata;

    //static
    static ArrayList<McsuPlayer> alivePlayers = new ArrayList<>();

    //Other vars
    String Name;
    World world;
    GamemodeManager gamemodeManager;
    ArrayList<GamemodeOptionChoice> gamemodeChoices;
    MapLoader mapLoader;
    boolean startIntervalEnabled = true;
    boolean freezeOnGameCountdown = true;
    GameMode playerGameMode = GameMode.SURVIVAL;

    ArrayList<GameState> gameStates = new ArrayList<>();
    ArrayList<McsuTeam> aliveTeams = new ArrayList<>();

    public Game(String Name) {

        this.Name = Name;

        Bukkit.getLogger().info("Registering game " + Name);

        Game_Main.RegisteredGames.add(this);

    }

    public void initGameLoader(World world) {

        this.world = world;

        if (gamemodeManager != null) {
            gamemodeChoices = (ArrayList<GamemodeOptionChoice>) gamemodeManager.pickOptions().clone();
        } else {
            Bukkit.getLogger().info("GamemodeManager is unset");
        }

        for (Player player : Bukkit.getOnlinePlayers()) { player.sendTitle(this.getName(), ""); }

        if (mapLoader == null) {
            mapLoader = new MapLoader().setSpawnManager(new SpawnManager()); // defaults to generic map + spawn loaders
        }

        EventListenerMain.setActivityRule("PlayerMovement", false);

        mapLoader.MapInit(this, world);
        mapLoader.getSpawnManager().lobbySpawns(mapLoader);

        alivePlayers = (ArrayList<McsuPlayer>) McsuPlayer.McsuPlayers.clone();
        aliveTeams = (ArrayList<McsuTeam>) TeamMain.Teams.clone();
        checkAliveTeams(false);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
        }

       for (GameState gameState : gameStates) { // trust me this has a purpose - gamestates are written down as enabled (for defaults) but are not initalized
            if ( gameState.getEnabled() ) {
                gameState.setEnabled(true);
           }
       }

        GameInitEvent initEvent = new GameInitEvent(this);
        Bukkit.getPluginManager().callEvent(initEvent);

        gameActive = true;
        MapMetadataDisplay.update();

        if (startIntervalEnabled) {
            Timer startTimer = new Timer(-1, DefaultStartLength)
                    .setOnTickIncrease(timer -> {

                        if (timer.getTimeLeft() == 5) {

                            mapLoader.getSpawnManager().gameSpawns(mapLoader);
                            if (freezeOnGameCountdown) EventListenerMain.setActivityRule("PlayerMovement", false);

                            GameSpawnsActivatedEvent spawnsActivatedEvent = new GameSpawnsActivatedEvent(this);
                            Bukkit.getPluginManager().callEvent(spawnsActivatedEvent);

                        }

                        for (Player player : Bukkit.getOnlinePlayers()) {

                            String message = ChatColor.WHITE + "" + ChatColor.BOLD + Name +
                                    ChatColor.RESET + "" + ChatColor.RED + " starting in " + timer.getTimeLeft();

                            if (timer.getTimeLeft() > 0) {

                                if (timer.getTimeLeft() <= 5) {

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

                        gameCountdownEnd();

                    });

        }


    }

    public void gameCountdownEnd() {

        if (freezeOnGameCountdown) EventListenerMain.setActivityRule("PlayerMovement", true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(playerGameMode);
            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "GO!", "");
        }

        GameCountdownEndEvent event = new GameCountdownEndEvent(this);
        Bukkit.getPluginManager().callEvent(event);

    }

    public void eliminatePlayer(Player bukkitPlayer) {
        bukkitPlayer.setGameMode(GameMode.SPECTATOR);
        this.removeFromAlivePlayers(
                McsuPlayer.fromBukkit(bukkitPlayer)
        );

    }

    public void spawnFireworks(Location location, int amount, Color color){
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public void checkAliveTeams( boolean announceElimination ) {

        ArrayList<McsuTeam> newDeadTeams = new ArrayList<>();

        for ( McsuTeam team : aliveTeams ) {

            boolean teamAlive = false;

            for ( String playerUUID : team.getMemberUUIDs() ) {

                if ( alivePlayers.contains( McsuPlayer.getPlayerByUUID( playerUUID ) ) ) {
                    teamAlive = true;
                    break;
                }

            }

            if ( !teamAlive ) {

                newDeadTeams.add(team);

                if (announceElimination) {
                    Bukkit.broadcastMessage( team.getStyledName() + " has been eliminated!" );
                } else {
                    Bukkit.getLogger().info( team.getStyledName() + " has been eliminated silently as announceElimination is false" );
                }

            }

        }

        for ( McsuTeam team : newDeadTeams ) { // Somewhat weird solution to a ConcurrentModificationException
            this.removeFromAliveTeams(team);
        }

    }

    public void endGame(McsuTeam winner) {
        EventListenerMain.resetActivityRules();

        for (GameState gameState : gameStates) {
            gameState.reset();
        }

        String style = ChatColor.BOLD + "" + ChatColor.WHITE;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(winner.getStyledName() + style + " win " + ChatColor.RESET + getName() + style + "!", generateEndSplash());
        }

        EventListenerMain.resetActivityRules();
    }

    public void endGame() {
        EventListenerMain.resetActivityRules();

        for (GameState gameState : gameStates) {
            gameState.reset();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {

            String style = ChatColor.BOLD + "" + ChatColor.WHITE;
            player.sendTitle(getName() + style + " has finished!", generateNoWinnerEndSplash());
        }

        EventListenerMain.resetActivityRules();
    }

    public void mapLoadingFinished() {
        EventListenerMain.setActivityRule("PlayerMovement", true);
    }

    public Game addGameState(GameState gameState) { gameStates.add(gameState); return this; }
    public Game setMapLoader(MapLoader mapLoader) { this.mapLoader = mapLoader; return this; }

    public Game setMapMetadata(MapMetadata mapMetadata) { this.mapMetadata = mapMetadata; return this; }
    public static MapMetadata getMapMetadata() { return mapMetadata; }


    public Game setGamemodeManager(GamemodeManager gamemodeManager) { this.gamemodeManager = gamemodeManager; return this; }
    public Game setPlayerGamemode(GameMode gameMode) { playerGameMode = gameMode; return this; }

    public Game setFreezeOnGameCountdown(boolean freezeOnGameCountdown) { this.freezeOnGameCountdown = freezeOnGameCountdown; return this; }

    public static ArrayList<McsuPlayer> getAlivePlayers() { return alivePlayers; }
    public void removeFromAlivePlayers(McsuPlayer player) { alivePlayers.remove(player); }

    public ArrayList<McsuTeam> getAliveTeams() { return aliveTeams; }
    public void removeFromAliveTeams(McsuTeam mcsuTeam) { aliveTeams.remove(mcsuTeam); }

    public World getWorld() { return world; }
    public String getName() { return this.Name; }
    public MapLoader getMapLoader() {  return mapLoader; }

    public GameState getGamestate(String Name) {

        return gameStates.stream().filter(gameState -> Objects.equals(gameState.getName(), Name)).findFirst().orElse(null);

    }

    public GamemodeOptionChoice getGamemodeOptionBlockChoice(String Name) {

        return gamemodeChoices.stream().filter(gamemodeChoice -> Objects.equals(gamemodeChoice.getOptionBlockName(), Name)).findFirst().orElse(null);

    }

    public ArrayList<GameState> getEnabledGameStates() {

        ArrayList<GameState> enabled = new ArrayList<>();
        gameStates.stream().filter(gameState -> gameState.enabled).forEach(enabled::add);
        return enabled;

    }

}
