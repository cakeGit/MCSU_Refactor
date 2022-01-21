package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Objects.McsuEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Player_Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        switch (cmd.getName()) {
            case "event":
                if(args.length >= 1) {
                    McsuEvent event = new McsuEvent();
                }
                return true;
        }
        return false;
    }
}
