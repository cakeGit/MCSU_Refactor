package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Command_Handlers.Cmd_Main;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Objects.Enums.CharacterSize;
import com.cloud.mcsu_rf.Objects.Map.MapMetadata;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MCSU_Main extends JavaPlugin implements Listener {

    public static MCSU_Main Mcsu_Plugin;
    public static MCSU_Main instance;
    public static String FileDir;
    public static String JarFileDir;

    @Override
    public void onEnable() {


        instance = this;
        Mcsu_Plugin = MCSU_Main.getPlugin(MCSU_Main.class);
        FileDir = MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath();
        JarFileDir = MCSU_Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();

        getLogger().info(FileDir);
        getLogger().info(JarFileDir);

        getLogger().info("MCSU Plugin has begun startup :)");

        CharacterSize.init();

        getServer().getPluginManager().registerEvents(new EventListenerMain(),this);
        Config_Main.init();

        EventListenerMain.registerActivityRules();
        Game_Main.init();
        Cmd_Main.Init();
        TeamMain.init();
        Scoreboard_Main.init();
        MapMetadata.loadData();

    }

}