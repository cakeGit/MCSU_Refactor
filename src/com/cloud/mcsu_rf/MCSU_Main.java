package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Command_Handlers.Cmd_Main;
import com.cloud.mcsu_rf.EventListeners.EventListener_Main;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Objects.Enums.CharacterSize;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MCSU_Main extends JavaPlugin implements Listener {

    public static MCSU_Main Mcsu_Plugin;
    public static MCSU_Main instance;
    public static String FileDir;

    @Override
    public void onEnable() {


        instance = this;
        Mcsu_Plugin = MCSU_Main.getPlugin(MCSU_Main.class);
        FileDir = MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath();
        getLogger().info(FileDir);

        getLogger().info("MCSU Plugin has begun startup :)");

        CharacterSize.init();

        getServer().getPluginManager().registerEvents(new EventListener_Main(),this);
        Config_Main.init();

        EventListener_Main.registerActivityRules();
        Game_Main.init();
        Cmd_Main.Init();
        Team_Main.init();
        Scoreboard_Main.init();

    }

}