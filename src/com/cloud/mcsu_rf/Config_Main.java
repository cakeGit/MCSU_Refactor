package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Objects.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Config_Main {

    public static ArrayList<ConfigFile> Configs = new ArrayList<>();
    public static ArrayList<ConfigFile> MapMetadataConfigs = new ArrayList<>();

    public static ConfigFile getByID(String ID) {

        return Configs.stream().filter(Config -> Objects.equals(Config.id, ID)).findFirst().orElse(null);

    }

    public static void init() {

        Configs.add( new ConfigFile(
                "Config",
                "c",
                "config.yml"
        ));
        Configs.add( new ConfigFile(
                "TpPoints",
                "tp",
                "tpPoints.yml"
        ));
        Configs.add( new ConfigFile(
                "MapRegister",
                "m",
                "mapRegister.yml"
        ));
        Configs.add( new ConfigFile(
                "TeamConfig",
                "t",
                "teamConfig.yml"
        ));

        ConfigFile mapRegister = getByID("m");

        List<?> Maps = mapRegister.config.getList("Maps");

        if (Maps != null) {
            Maps.forEach(mapSublist -> {
                ((List<?>) mapSublist).forEach(mapMetadataFilename -> {
                    MapMetadataConfigs.add( new ConfigFile(
                            (String) mapMetadataFilename,
                            "",
                            mapMetadataFilename + ".yml",
                            "/mapMetadata/"

                    ));
                });
            });
        }


    }

    public static void consumeFile(File yamlFile, BiConsumer<? super String, ? super Object> consumer){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
        for (String path : config.getKeys(true)){
            if (config.get(path) != null){
                consumer.accept(path, config.get(path));
            }
        }
    }


/*
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("MCSU_Refactored").getDataFolder(), "config.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                Config_Main.setup();
                Config_Main.get().options().copyDefaults(true);
                Config_Main.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configfile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return configfile;
    }

    public static void save() {
        try {
            configfile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file.");
            e.printStackTrace();
        }
    }

    public static void updateConfig() {
        Config_Main.save();
        Config_Main.get().options().copyDefaults(true);
        Config_Main.reload();
    }

    public static void reload() {
        configfile = YamlConfiguration.loadConfiguration(file);
    }
*/
}
