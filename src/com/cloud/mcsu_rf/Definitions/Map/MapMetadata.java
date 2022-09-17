package com.cloud.mcsu_rf.Definitions.Map;

import com.cak.what.ConfigApi.ConfigFile;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapMetadata {
    public static ArrayList<MapMetadata> RegisteredMapMetadata = new ArrayList<>();

    public static void loadData() {

        ConfigFile mapRegister = new ConfigFile("maps.yml");

        List<?> Maps = mapRegister.getConfig().getList("Maps");

        if (Maps != null) {
            Maps.forEach(mapMetadataFilename -> new MapMetadata( new ConfigFile(
                    mapMetadataFilename + ".yml",
                    "/mapMetadata/"
            )));
        }

    }

    ArrayList<MapPoint> LobbyPoints = new ArrayList<>();
    ArrayList<MapPoint> GamePoints = new ArrayList<>();

    String Name;
    String Game;
    String Creator;
    String SchemFileName;
    Integer[] SchemPastePosition;
    ConfigFile configFile;
    String gameSpawnType;

    public MapMetadata(ConfigFile configFile) {

        this.configFile = configFile;

        ( (List) get("Points.Lobby") ).forEach(
                Element -> {
                    HashMap<String, Object> ElementHashmap = (HashMap<String, Object>) Element;

                    LobbyPoints.add(
                            new MapPoint(
                                    ElementHashmap.get("Coordinates").toString(),
                                    ElementHashmap.get("Rotation").toString())
                    );
                }
        );

        if (get("Points.Game")  != null) {
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
        }


        this.Name = (String) get("MapData.Name");
        this.Game = (String) get("MapData.Game");
        this.Creator = (String) get("MapData.Creator");
        if(get("MapData.SchemFile") != null) {
            this.SchemFileName = (String) get("MapData.SchemFile");
            this.SchemPastePosition = ParseArr.toInteger(((String) get("MapData.SchemPastePosition")).split(" "));
        } else {
            this.SchemFileName = null;
            this.SchemPastePosition = null;
        }
        this.gameSpawnType = (String) get("MapData.GameSpawnType");

        RegisteredMapMetadata.add(this);

        //Bukkit.broadcastMessage("Indexed map metadata for a " + this.Game + " map '" + this.Name + "'");

    }

    public ArrayList<MapPoint> getGamePoints() { return GamePoints; }
    public ArrayList<MapPoint> getLobbyPoints() { return LobbyPoints; }

    public void setGamePoints(ArrayList<MapPoint> GamePoints) { configFile.saveDat(); }

    @Deprecated public ConfigFile getConfigFile() { return configFile; }
    public Object get(String key) { return configFile.getConfig().get(key); }
    public String getGame() { return Game; }
    public String getName() { return Name; }
    public String getCreator() { return Creator; }
    public String getSchemFileName() { return SchemFileName; }
    public Integer[] getSchemPastePosition() { return SchemPastePosition; }
    public String getGameSpawnType() { return gameSpawnType; }
}
