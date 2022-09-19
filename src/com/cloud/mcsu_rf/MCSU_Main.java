package com.cloud.mcsu_rf;

import com.cak.what.CommandAPI.CommandHandler;
import com.cak.what.MenuAPI.InventoryMenuEvents;
import com.cak.what.RegisterAPI.Register;
import com.cloud.mcsu_rf.Cars.CarEvents;
import com.cloud.mcsu_rf.Command_Handlers.Cmd_Main;
import com.cloud.mcsu_rf.Definitions.Enums.CharacterSize;
import com.cloud.mcsu_rf.Definitions.Map.MapMetadata;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.McsuScoreboard;
import com.cloud.mcsu_rf.Devtools.Dev;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Games.SurvivalGames;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MCSU_Main extends JavaPlugin implements Listener {

    public static MCSU_Main Mcsu_Plugin;
    public static MCSU_Main instance;
    public static String FileDir;
    public static String JarFileDir;
    public static CommandHandler cmdHandler;

    @Override
    public void onEnable() {

        cmdHandler = new CommandHandler(this);

        instance = this;
        Mcsu_Plugin = MCSU_Main.getPlugin(MCSU_Main.class);
        FileDir = MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath();
        JarFileDir = MCSU_Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();

        getLogger().info("MCSU Plugin has begun startup :)");

        Register.eventHandler(Mcsu_Plugin, new InventoryMenuEvents());

        CharacterSize.init();

        getServer().getPluginManager().registerEvents(new EventListenerMain(),this);
        getServer().getPluginManager().registerEvents(new SurvivalGames(), this);
        carInit();
        EventListenerMain.registerActivityRules();
        Game_Main.init();
        Cmd_Main.Init();
        TeamMain.init();
        getCommand("mcsuevent").setExecutor(new McsuEvent());
        getCommand("winner").setExecutor(new McsuEvent());
        getCommand("stopglow").setExecutor(new McsuEvent());
        MapMetadata.loadData();
        DeathMessages.init();
        McsuScoreboard.init();
        Event.init();
        Dev.init();

        for (Player p : Bukkit.getOnlinePlayers()) { McsuPlayer.registerPlayer(p);}
        McsuScoreboard.defaultScoreboard.update();

    }

    public void carInit() {
        getServer().getPluginManager().registerEvents(new CarEvents(), this);
        CarEvents.speed = new HashMap<>();
        CarEvents.honkCooldown = new HashMap<>();
    }

}