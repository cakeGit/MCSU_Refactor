package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Command_Handlers.Game_Commands;
import com.cloud.mcsu_rf.Game_Handlers.GameManager;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Listeners.Event_Listeners;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.plugin.java.JavaPlugin;

public class MCSU_Main extends JavaPlugin {

    public static MCSU_Main Mcsu_Plugin;
    private static MCSU_Main instance;
    public GameManager gameManager;

    @Override
    public void onEnable() {

        setInstance(this);

        Mcsu_Plugin = MCSU_Main.getPlugin(MCSU_Main.class);

        getLogger().info("MCSU Plugin has begun startup :)");

        getServer().getPluginManager().registerEvents(new Event_Listeners(),this);

        Game_Main.Init();
        //Command_Main.Init();
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

    public void registerCommands() {

        MCSU_Main.Mcsu_Plugin.getCommand("listgames").setExecutor(new Game_Commands(gameManager));
        MCSU_Main.Mcsu_Plugin.getCommand("playgame").setExecutor(new Game_Commands(gameManager));
        //MCSU_Main.Mcsu_Plugin.getCommand("setspawn").setExecutor(new Player_Commands());
        //MCSU_Main.Mcsu_Plugin.getCommand("hub").setExecutor(new Player_Commands());
    }
}