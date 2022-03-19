package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Break;
import com.cloud.mcsu_rf.MCSUEvent.MCSUEvent;
import com.cloud.mcsu_rf.Objects.Game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Player_Commands {

    public static boolean startEvent(CommandSender sender, String[] args) {
        if(args.length >= 1) {
            if ("start".equals(args[0])) {
                MCSUEvent event = new MCSUEvent();
                return true;
            }
        }
        return true;
    }
}
