package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Command_Handlers.Cmd_Main;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.plugin.java.JavaPlugin;

public class MCSU_Main extends JavaPlugin {

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

        getServer().getPluginManager().registerEvents(new EventListener_Main(),this);

        Game_Main.Init();
        Cmd_Main.Init();
        Team_Main.init();
        Scoreboard_Main.init();
        Config_Main.init();

    }
}