package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.MCSU_Main;

public class Command_Main {

    static void registerCommands() {

        MCSU_Main.Mcsu_Plugin.getCommand("listgames").setExecutor(new Game_Commands());
        MCSU_Main.Mcsu_Plugin.getCommand("playgame").setExecutor(new Game_Commands());

    }

    public static void Init() {

        registerCommands();

    }

}
