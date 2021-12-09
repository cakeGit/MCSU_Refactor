package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Objects.ConfigFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TpPoint_Cmds {

    public static boolean createPoint(CommandSender Sender, Command Cmd, String Label, String[] Args, boolean debugPoint) {

        ConfigFile tpPointConfig = Config_Main.getByID("t");

        if(Args.length >= 4) {

            String tpPointName = Args[0];
            Sender.sendMessage(tpPointName);

            double x = Double.parseDouble(Args[1]);
            double y = Double.parseDouble(Args[2]);
            double z = Double.parseDouble(Args[3]);
            Sender.sendMessage(x+" "+y+" "+z);

            tpPointConfig.config.set( "InformalPoints."+Args[0]+".Name", tpPointName );
            tpPointConfig.config.set( "InformalPoints."+Args[0]+".Coordinates", (x+" "+y+" "+z) );

            return true;

        }

        return false;

    }

    public static boolean listPoints(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        ConfigFile tpPointConfig = Config_Main.getByID("t");

        tpPointConfig.config.getConfigurationSection("InformalPoints").getValues(false).forEach((key, value) -> {
            Sender.sendMessage(key);
            Sender.sendMessage(value.toString());
        });

        return false;


    }

    /*public static boolean tpToPlayerPoint(CommandSender Sender, Command Cmd, String Label, String[] Args)  {

    }*/

}
