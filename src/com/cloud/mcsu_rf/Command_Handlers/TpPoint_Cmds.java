package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Objects.ConfigFile;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TpPoint_Cmds {

    static ConfigFile tpPointConfig = Config_Main.getByID("tp");

    public static void createPoint(String name, double x, double y, double z, double rotx, double roty, Boolean isGamePoint) {

        if (isGamePoint) {
            tpPointConfig.config.set( "UnsortedMapPoints."+name+".Coordinates", (x+" "+y+" "+z) );
            tpPointConfig.config.set( "UnsortedMapPoints."+name+".Rotation", (rotx+" "+roty) );
        } else {
            tpPointConfig.config.set( "TpPoints."+name+".Coordinates", (x+" "+y+" "+z) );
            tpPointConfig.config.set( "TpPoints."+name+".Rotation", (rotx+" "+roty) );
        }

        tpPointConfig.saveDat();

    }

    public static void listPoints(CommandSender Sender) {

        Objects.requireNonNull(tpPointConfig.config.getConfigurationSection("TpPoints")).getValues(false).forEach((key, value) -> {
            Sender.sendMessage(key);
            Sender.sendMessage("    at: " + Objects.requireNonNull(tpPointConfig.config.getString("TpPoints." + key + ".Coordinates")));
        });

    }

    public static boolean teleportPlayerToPoint(Player player, String name) {

        String[] coordsStr = Objects.requireNonNull(tpPointConfig.config.get("TpPoints." + name + ".Coordinates")).toString().split(" ");
        ArrayList<Double> coordsArrList = new ArrayList<>();

        for (String str : coordsStr) {
            coordsArrList.add(Double.parseDouble(str));
        }

        Double[] coords = coordsArrList.toArray(new Double[0]);

        player.teleport(new Location(player.getWorld(),  coords[0], coords[1], coords[2]));

        return false;

    }

    public static boolean tpPoint(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Location pLoc = p.getLocation();

        double[] pXyz = new double[] { pLoc.getX(), pLoc.getY(), pLoc.getZ() };

        ArrayList<Double> pXyzRrnArrList = new ArrayList<>();
        for (double pos : pXyz) {
            pXyzRrnArrList.add((double) Math.round(pos/0.5) *0.5);
        }

        Double[] pXyzRrn = pXyzRrnArrList.toArray(new Double[0]);


        double[] pPy = new double[] { pLoc.getPitch(), pLoc.getYaw() };
        ArrayList<Double> pPyRrnArrList = new ArrayList<>();

        for (double pos : pPy) {
            pPyRrnArrList.add((double) Math.round(pos/45) * 45);
        }

        Double[] pPyRrn = pPyRrnArrList.toArray(new Double[0]);


        switch (args[0]) {

            case "new":
                Boolean isGamePoint = Boolean.parseBoolean(args[args.length - 1]);
                if (args[1] == "-p") { // precise - creates the point with no rounding
                    createPoint(args[1], pLoc.getX(), pLoc.getY(), pLoc.getZ(), pLoc.getPitch(), pLoc.getYaw(), isGamePoint);
                } else if (args[1] == "-h") { //here= creates the point at player loc with rounding
                    createPoint(args[1], pXyzRrn[0], pXyzRrn[1], pXyzRrn[2], pPyRrn[0], pPyRrn[1], isGamePoint);
                } else if (args[1] == "-a") { // creates the point at specified thing
                    createPoint(
                            args[0],
                            Double.parseDouble(args[2]),
                            Double.parseDouble(args[3]),
                            Double.parseDouble(args[4]),
                            Double.parseDouble(args[5]),
                            Double.parseDouble(args[6]),
                            isGamePoint
                    );
                }
            case "list":
                listPoints(sender);

        }

        return true;

    }

    /*public static boolean tpToPlayerPoint(CommandSender Sender, Command Cmd, String Label, String[] Args)  {

    }*/

}
