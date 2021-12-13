package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Game_Handlers.Schematic_Loader;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.Stopwatch;
import com.cloud.mcsu_rf.Objects.Lambdas.MapLoaderLambda;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
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
    Sound DefaultStartTimerTickSound = Sound.BLOCK_NOTE_BLOCK_SNARE;
    Sound DefaultStartTimerEndSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;

    String Name;
    boolean startIntervalEnabled;
    ArrayList<GameState> gameStates = new ArrayList<>();
    MapLoaderLambda mapLoaderMethod = (game, world) -> Bukkit.getLogger().info("Game '" + game.getName() + "' isn't given any mapLoader!");

    ArrayList<MCSU_Player> alivePlayers = new ArrayList<>();

    public MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    public Game(String Name) {

        this.Name = Name;

        Bukkit.getLogger().info("Registering game " + Name);

        Game_Main.RegisteredGames.add(this);

    }

    public Game addGameState(GameState gameState) { this.gameStates.add(gameState); return this;}
    public String getName() { return this.Name; }
    public Game addStartInterval() { startIntervalEnabled = true; return this; }

    public void initGameLoader(World world) {

        mapLoaderMethod.exec(this, world);

        alivePlayers = MCSU_Player.MCSU_Players;

        if (startIntervalEnabled) {
            Timer startTimer = new Timer(-1, DefaultStartLength)
                    .setOnTickIncrease(timer -> {

                        for (Player player : Bukkit.getOnlinePlayers()) {

                            String message = ChatColor.WHITE + "" + ChatColor.BOLD + this.getName() +
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

                        for (GameState gameState : getEnabledGameStates()) {
                            gameState.setEnabled(true);
                        }

                        gameStart();

                    });

        }

    }

    public void gameStart() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("something","hi sam");
        }

        //stuf to run when game begins

    }

    public ArrayList<MCSU_Player> getAlivePlayers() { return this.alivePlayers; }

    public void removeFromAlivePlayers(MCSU_Player player) { this.alivePlayers.remove(player); }

    public Game setMapManager(String type, String mapName) { // Todo: Make it like game functions with extends n that

        if ("basic".equals(type)) {
            this.mapLoaderMethod = (game, world) -> Schematic_Loader.loadSchematic(mapName, BlockVector3.at(2000, 100, 2000), world);
            return this;
        }

        throw new NullPointerException("tngoisubrd! :(");
    }

    public ArrayList<GameState> getEnabledGameStates() {

        ArrayList<GameState> enabled = new ArrayList<>();
        gameStates.stream().filter(gameState -> gameState.enabled).forEach(enabled::add);
        return enabled;

    }

}
