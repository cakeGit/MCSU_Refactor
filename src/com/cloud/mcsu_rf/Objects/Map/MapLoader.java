package com.cloud.mcsu_rf.Objects.Map;

import com.cloud.mcsu_rf.Game_Handlers.FaweFunctions;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Pick;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Objects;

public class MapLoader {

    SpawnManager spawnManager;
    World world;
    MapMetadata mapData;

    public MapLoader() {}

    public MapLoader setSpawnManager(SpawnManager spawnManager) { this.spawnManager = spawnManager; return this; }
    public SpawnManager getSpawnManager() { return spawnManager; }

    public MapMetadata getMapData() { return mapData; }
    public World getWorld() { return world; }

    public void MapInit(Game game, World world) {

        this.world = world;

        ArrayList<MapMetadata> gameMaps = new ArrayList<>();

        for (MapMetadata registeredMapData : MapMetadata.RegisteredMapMetadata) {

            if (Objects.equals(registeredMapData.getGame(), game.getName())) {
                gameMaps.add(registeredMapData);
                Bukkit.getLogger().info("Found map with matching game name called '" + registeredMapData.getName() + "'");
            }

        }

        mapData = Pick.Random(gameMaps);
        Bukkit.broadcastMessage("Picked map '" + mapData.getName() + "'");

        game.setMapMetadata(mapData);

        FaweFunctions.loadSchematic(mapData.getSchemFileName(), BlockVector3.at(2000, 60, 0), world);

    }

}
