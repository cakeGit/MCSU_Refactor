package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Definitions.QueuedGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.UUID;

public class Event { //Not to be confused with the elusive MCSU event folder

    //Static Elements

    //Current Event data
    ArrayList<QueuedGame> queuedGames = new ArrayList<>();

    public static void init() {
        MCSU_Main.cmdHandler.registerCommandListener("event", Event::eventCmdHandler);
    }

    public static void startEvent(CommandSender sender) {

    }

    private static boolean eventCmdHandler(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/event <start | configure>");
            return true;
        }
        switch (args[0]) {
            case "start":
                startEvent(sender);
                break;
            case "configure":
                break;
            default:
                sender.sendMessage("/event <start | configure>");
                break;
        }
        return true;

    }

    //Object Elements
    ArrayList<UUID> winners = new ArrayList<>();

}