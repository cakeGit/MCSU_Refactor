package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Cmd_Main implements CommandExecutor {

    static String[] cmdNames;
    static String[] tpCommands;


    static void registerCommands() {

        for (String cmd : cmdNames) {
            MCSU_Main.Mcsu_Plugin.getCommand(cmd).setExecutor(new Cmd_Main());
        }

        for (String cmd : tpCommands) {
            MCSU_Main.Mcsu_Plugin.getCommand(cmd).setExecutor(new Cmd_Main());
        }

    }

    public static void Init() {

        cmdNames = new String[] {
                "listgames",
                "playgame",
                "givescore",
                "countdown",

                //Op tpPoint Cmds
                "createtppoint",
                "createdebugtppoint",

                //Non-Op tpPoint Cmds
                "listtppoints",
                "gotopoint"
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

            case "hub": return Game_Cmds.listGames(Sender, Cmd, Label, Args);

        }

        if (!Sender.isOp()) {
            return notOpError(Sender);
        }

        switch (Cmd.getName()) { // Operator commands

            case "listgames": return Game_Cmds.listGames(Sender, Cmd, Label, Args);
            case "playgame": return Game_Cmds.playGame(Sender, Cmd, Label, Args);

            case "givescore": return Score_Cmds.giveScore(Sender, Cmd, Label, Args);

            case "createPoint": return TpPoint_Cmds.createPoint(Sender, Cmd, Label, Args, false);
            case "listPoints": return TpPoint_Cmds.listPoints(Sender, Cmd, Label, Args);

            //case "countdown": return Game_Cmds.playGame(Sender, Cmd, Label, Args); for claude todo

        }

        Sender.sendMessage(ChatColor.RED + "No executor found for command /" + Cmd.getName());

        return true;

    }

}
