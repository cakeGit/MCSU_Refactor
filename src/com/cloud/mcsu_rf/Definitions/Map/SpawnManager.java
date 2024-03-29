package com.cloud.mcsu_rf.Definitions.Map;

import com.cloud.mcsu_rf.Definitions.McsuPlayer;
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

    public static MapPoint tpPlayerToGameSpawn(MapLoader mapLoader, Player player) {

        MapMetadata mapData = mapLoader.getMapData();
        World world = mapLoader.getWorld();
        MapPoint pickedPoint = null;

        switch (mapData.getGameSpawnType()) {
            case "Team":

                String playerTeamId = Objects.requireNonNull(McsuPlayer.fromBukkit(player)).getTeamID();

                boolean foundPoint = false;

                for (MapPoint gameSpawn : mapData.getGamePoints()) {
                    if (Objects.equals(gameSpawn.getId(), playerTeamId)) {

                        teleportToMapPoint(player, gameSpawn, world);
                        pickedPoint = gameSpawn;

                        foundPoint = true;
                    }
                }

                if (!foundPoint) { Bukkit.broadcastMessage(ChatColor.RED + "[MCSU]: Couldn't find a point for team with id "+playerTeamId); }

                break;
            case "None": break;
            default:
                ArrayList<MapPoint> unusedPoints = (ArrayList<MapPoint>) mapData.getGamePoints().clone();

                if (unusedPoints.size() == 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + "[MCSU]: umm so i kinda dont have enough game spawns...");
                }

                MapPoint gameSpawn = unusedPoints.get(0);

                teleportToMapPoint(player, gameSpawn, world);
                pickedPoint = gameSpawn;

                Bukkit.getLogger().info(unusedPoints.size() + " spawns still unused ");

                unusedPoints.remove(gameSpawn);

                break;
        }

        return pickedPoint;

    }

    public void gameSpawns(MapLoader mapLoader) { //Will randomly place ppl in the spawns

        for (Player player : Bukkit.getOnlinePlayers()) {
            tpPlayerToGameSpawn(mapLoader, player);
        }

    }

}
