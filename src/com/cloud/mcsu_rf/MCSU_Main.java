package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Command_Handlers.Command_Main;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.plugin.java.JavaPlugin;

public class MCSU_Main extends JavaPlugin {

    public static MCSU_Main Mcsu_Plugin;
    private static MCSU_Main instance;

    @Override
    public void onEnable() {

        setInstance(this);

        Mcsu_Plugin = MCSU_Main.getPlugin(MCSU_Main.class);

        getLogger().info("MCSU Plugin has begun startup :)");

        getServer().getPluginManager().registerEvents(new Event_Listeners(),this);

        Game_Main.Init();
        Command_Main.Init();
        Team_Main.init();
        Scoreboard_Main.init();

        //Config init

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        Config_Main.init();

        getLogger().info("Finnisherd loading");
    }

    public static MCSU_Main getInstance() {
        return instance;
    }

    private void setInstance(MCSU_Main instance) {
        this.instance = instance;
    }
}