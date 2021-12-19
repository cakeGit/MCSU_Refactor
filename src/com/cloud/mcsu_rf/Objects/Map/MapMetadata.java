package com.cloud.mcsu_rf.Objects.Map;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Objects.ConfigFile;
import org.bukkit.Bukkit;

public class MapMetadata {
    public static ArrayList<MapMetadata> RegisteredMapMetadata = new ArrayList<>();

    public static void loadData() {

        ConfigFile mapRegister = Config_Main.getByID("m");

        List<?> Maps = mapRegister.config.getList("Maps");

        if (Maps != null) {
            Maps.forEach(mapMetadataFilename -> {
                RegisteredMapMetadata.add(new MapMetadata( new ConfigFile(
                        (String) mapMetadataFilename,
                        "",
                        mapMetadataFilename + ".yml",
                        "/mapMetadata/"
                )));
            });
        }

    }

    ArrayList<MapPoint> LobbyPoints = new ArrayList<>();
    ArrayList<MapPoint> GamePoints = new ArrayList<>();

    String Name;
    String Game;
    String SchemFileName;
    ConfigFile configFile;
    String gameSpawnType;

    public MapMetadata(ConfigFile configFile) {

        this.configFile = configFile;

        ( (List) get("Points.Lobby") ).forEach(
                Element -> {
                    HashMap<String, Object> ElementHashmap = (HashMap<String, Object>) Element;

                    LobbyPoints.add( new MapPoint(ElementHashmap.get("Coordinates").toString(), ElementHashmap.get("Rotation").toString()) );
                }
        );

        ( (List) get("Points.Game") ).forEach(
                Element -> {
                    HashMap<String, Object> ElementHashmap = (HashMap<String, Object>) Element;

                    GamePoints.add(
                            new MapPoint(
                                    ElementHashmap.get("Coordinates").toString(),
                                    ElementHashmap.get("Rotation").toString())
                                    .setId(ElementHashmap.get("Id").toString())
                    );
                }
        );

        this.Name = (String) get("MapData.Name");
        this.Game = (String) get("MapData.Game");
        this.SchemFileName = (String) get("MapData.SchemFile");
        this.gameSpawnType = (String) get("MapData.GameSpawnType");

        RegisteredMapMetadata.add(this);

        Bukkit.getLogger().info("Indexed map metadata for  a " + this.Game + " map '" + this.Name + "'");

    }

    public ArrayList<MapPoint> getGamePoints() { return GamePoints; }
    public ArrayList<MapPoint> getLobbyPoints() { return LobbyPoints; }
    @Deprecated public ConfigFile getConfigFile() { return configFile; }
    public Object get(String key) { return configFile.config.get(key); }
    public String getGame() { return Game; }
    public String getName() { return Name; }
    public String getSchemFileName() { return SchemFileName; }
    public String getGameSpawnType() { return gameSpawnType; }
}
