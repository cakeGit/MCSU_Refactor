package com.cloud.mcsu_rf;
import com.cloud.mcsu_rf.Command_Handlers.Game_Commands;
import com.cloud.mcsu_rf.Game_Classes.Game_Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MCSU_Main extends JavaPlugin {


    void Register_Commands() {
        this.getCommand("listgames").setExecutor(new Game_Commands());
        this.getCommand("playgame").setExecutor(new Game_Commands());
    }

    public static MCSU_Main Mcsu_Plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    @Override
    public void onEnable() {

        getLogger().info("MCSU Plugin has begun startup :)");


        getServer().getPluginManager().registerEvents(new Event_Listeners(),this);
        Game_Main.Games_Init();
        Register_Commands();

    }
}