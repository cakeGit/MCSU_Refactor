package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Cmd_Main implements CommandExecutor {

    static String[] cmdNames;
    static String[] tpCommands;


    static void registerCommands() {

        for (String cmd : cmdNames) {
            Objects.requireNonNull(MCSU_Main.Mcsu_Plugin.getCommand(cmd)).setExecutor(new Cmd_Main());
        }

        for (String cmd : tpCommands) {
            Objects.requireNonNull(MCSU_Main.Mcsu_Plugin.getCommand(cmd)).setExecutor(new Cmd_Main());
        }

    }

    public static void Init() {

        cmdNames = new String[] {
                "listgames",
                "playgame",
                "givescore",
                "tppoint",
                "gamepoint",
                "goto",
                "team",
                "enablepvp"
        };

        tpCommands = new String[] {
                "hub",
        };

        registerCommands();

    }

    public static boolean notOpError(CommandSender sender) {
        sender.sendMessage( ChatColor.RED + "oi mate bugger off you need op to do that");
        return true;
    }

    @Override
    public boolean onCommand(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        switch (Cmd.getName()) { // Non-Operator commands

            case "hub": return TpPoint_Cmds.teleportPlayerToPoint((Player) Sender, "hub");
            case "goto": return TpPoint_Cmds.teleportPlayerToPoint((Player) Sender, Args[0]);

        }

        if (!Sender.isOp()) {
            return notOpError(Sender);
        }

        switch (Cmd.getName()) { // Operator commands

            case "listgames": return Game_Cmds.listGames(Sender);
            case "playgame": return Game_Cmds.playGame(Sender, Args);

            case "givescore": return Score_Cmds.giveScore(Sender, Args);

            case "tppoint": return TpPoint_Cmds.tpPoint(Sender, Args);
            case "team": return Score_Cmds.Team(Sender, Args);
            case "spawnsled": return Game_Cmds.spawnSled(Sender, Args);

            case "enablepvp": EventListenerMain.setActivityRule("PVP", true); return true;

        }

        Sender.sendMessage(ChatColor.RED + "[MCSU]: No executor found for command /" + Cmd.getName());

        return true;

    }

}
