package com.cloud.mcsu_rf.Command_Handlers;
import com.cloud.mcsu_rf.Config_Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Player_Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        switch (cmd.getName()) {
            case "setspawn":
                if(args.length >= 4) {
                    String spawnpoint = args[0];
                    double x = Double.parseDouble(args[1]);
                    double y = Double.parseDouble(args[2]);
                    double z = Double.parseDouble(args[3]);
                    if(spawnpoint.equalsIgnoreCase("Hub")) {
                        Config_Main.get().set("Spawns.Hub.X",x);
                        Config_Main.get().set("Spawns.Hub.Y",y);
                        Config_Main.get().set("Spawns.Hub.Z",z);
                        sender.sendMessage(ChatColor.GREEN+"Successfully set the spawn for the Hub!");
                    } else {
                        sender.sendMessage(ChatColor.RED+"Invalid spawnarea.");
                    }
                    Config_Main.updateConfig();
                } else {
                    sender.sendMessage(ChatColor.RED+"Invalid command. Usage: /setspawn <SpawnArea> <x> <y> <z>");
                }
                return true;
            case "hub":
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    Location spawnLoc = p.getLocation();
                    spawnLoc.setX((Double) Config_Main.get().get("Spawns.Hub.X"));
                    spawnLoc.setY((Double) Config_Main.get().get("Spawns.Hub.Y"));
                    spawnLoc.setZ((Double) Config_Main.get().get("Spawns.Hub.Z"));
                    p.teleport(spawnLoc);
                } else {
                    sender.sendMessage(ChatColor.RED+"Only players can run this command!");
                }
                return true;
        }
        return false;
    }
}
