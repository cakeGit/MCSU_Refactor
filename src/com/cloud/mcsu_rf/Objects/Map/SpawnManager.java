package com.cloud.mcsu_rf.Objects.Map;

import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Pick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class SpawnManager {

    public static void teleportToMapPoint(Player player, MapPoint mapPoint, World world) {
        player.teleport( new Location(
                world,
                mapPoint.Coordinates[0],
                mapPoint.Coordinates[1],
                mapPoint.Coordinates[2],
                (float) (double) mapPoint.Rotation[0],
                (float) (double)  mapPoint.Rotation[1]
        ));
    }

    public void lobbySpawns(MapLoader mapLoader) {

        MapMetadata mapData = mapLoader.getMapData();
        World world = mapLoader.getWorld();

        for (Player player : Bukkit.getOnlinePlayers()) {
            MapPoint lobbySpawn = Pick.Random(mapData.getLobbyPoints());

            teleportToMapPoint(player, lobbySpawn, world);

            /*Bukkit.broadcastMessage(lobbySpawn.Coordinates[0] +" "+
                    lobbySpawn.Coordinates[1] +" "+
                    lobbySpawn.Coordinates[2] +" "+
                    (float) (double) lobbySpawn.Rotation[0]+" "+
                    (float) (double)  lobbySpawn.Rotation[1]);*/
        }

    }

    public void gameSpawns(MapLoader mapLoader) { //Will randomly place ppl in the spawns

        MapMetadata mapData = mapLoader.getMapData();
        World world = mapLoader.getWorld();

        /*Bukkit.broadcastMessage("Loading spawns for " + mapData.getGame() + " - " + mapData.getName());*/

        switch (mapData.getGameSpawnType()) {
            case "Team":
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String playerTeamId = McsuPlayer.fromBukkit(player).getTeamID();

                    boolean foundPoint = false;

                    for (MapPoint gameSpawn : mapData.getGamePoints()) {
                        if (Objects.equals(gameSpawn.getId(), playerTeamId)) {

                            teleportToMapPoint(player, gameSpawn, world);

                            foundPoint = true;
                        }
                    }

                    if (!foundPoint) { Bukkit.broadcastMessage(ChatColor.RED + "[MCSU]: Couldn't find a point for team with id "+playerTeamId); }
                }

                break;
            default:
                ArrayList<MapPoint> unusedPoints = (ArrayList<MapPoint>) mapData.getGamePoints().clone();

                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (unusedPoints.size() == 0) {
                        Bukkit.broadcastMessage(ChatColor.RED + "[MCSU]: umm so i kinda dont have enough game spawns...");
                    }

                    MapPoint gameSpawn = unusedPoints.get(0);

                    teleportToMapPoint(player, gameSpawn, world);

                    Bukkit.getLogger().info(unusedPoints.size() + " spawns still unused ");

                    unusedPoints.remove(gameSpawn);

                }
                break;
        }



    }

}
