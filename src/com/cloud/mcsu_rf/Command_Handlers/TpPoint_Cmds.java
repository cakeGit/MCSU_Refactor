package com.cloud.mcsu_rf.Command_Handlers;

import com.cak.what.ConfigApi.ConfigFile;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Break;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TpPoint_Cmds {

    static ConfigFile tpPointConfig = new ConfigFile("tpPoints.yml");

    public static void createPoint(String id, double x, double y, double z, double rotx, double roty) {

        //add a tp point to the list of TpPoints

        List<Object> tpPoints = (List<Object>) tpPointConfig.getConfig().getList("TpPoints");

        HashMap<String, Object> newPoint = new HashMap<>();
        newPoint.put("Id", id);
        newPoint.put("Coordinates", x + " " + y + " " + z);
        newPoint.put("Rotation", rotx + " " + roty);

        tpPoints.add(newPoint);

        tpPointConfig.saveDat();

    }

    public static void listPoints(CommandSender Sender) {

        Break.line(Sender);
        Sender.sendMessage("Total Points: "+ tpPointConfig.getConfig().getList("TpPoints").size());
        Sender.sendMessage("");
        Sender.sendMessage("List of TpPoints:");
        tpPointConfig.getConfig().getList("TpPoints").forEach(
                (point) -> {
                    HashMap hashmapPoint = (HashMap) point;

                    Sender.sendMessage("    Id: "+ hashmapPoint.get("Id") + ChatColor.DARK_GRAY +
                            " Coordinates: "+ hashmapPoint.get("Coordinates") +
                            " Rotation: "+ hashmapPoint.get("Rotation"));
                }
        );
        Break.line(Sender);

        try {
            Sender.sendMessage(tpPointConfig.getConfig().getString("TpPoints[0].Coordinates"));
        } catch (Exception ignored) {}

    }

    public static boolean teleportPlayerToPoint(Player player, String name) {

        String[] coordsStr = Objects.requireNonNull(tpPointConfig.getConfig().get("TpPoints." + name + ".Coordinates")).toString().split(" ");
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

        //rounding location to nearest 0.5


        double[] pXyz = new double[] { pLoc.getX(), pLoc.getY(), pLoc.getZ() };

        ArrayList<Double> pXyzRrnArrList = new ArrayList<>();
        for (double pos : pXyz) {
            pXyzRrnArrList.add((double) Math.round(pos/0.5) *0.5);
        }

        Double[] pXyzRrn = pXyzRrnArrList.toArray(new Double[0]);


        double[] pPy = new double[] {  pLoc.getYaw(), pLoc.getPitch() };
        ArrayList<Double> pPyRrnArrList = new ArrayList<>();

        for (double pos : pPy) {
            pPyRrnArrList.add((double) Math.round(pos/45) * 45);
        }

        Double[] pPyRrn = pPyRrnArrList.toArray(new Double[0]);


        switch (args[0]) {

            case "new":
                createPoint(args[1], pXyzRrn[0], pXyzRrn[1], pXyzRrn[2], pPyRrn[0], pPyRrn[1]);
                sender.sendMessage(
                        ChatColor.GREEN +
                        "Created point with id '" +
                        args[1] + "' at " +
                        pXyzRrn[0] +" "+
                        pXyzRrn[1] +" "+
                        pXyzRrn[2] +
                        " with rotation " +
                        pPyRrn[0] +" "+
                        pPyRrn[1]
                );
                break;
            case "list":
                listPoints(sender);
                break;

        }

        return true;

    }

}
