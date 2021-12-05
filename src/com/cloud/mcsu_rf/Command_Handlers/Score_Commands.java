package com.cloud.mcsu_rf.Command_Handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Score_Commands implements CommandExecutor  { //TODO: make do stuff

    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        if (!Sender.isOp()) {
            return Command_Main.notOpError(Sender);
        }


        //Should not happen but so intellij shuts up
        return false;

    }

}
