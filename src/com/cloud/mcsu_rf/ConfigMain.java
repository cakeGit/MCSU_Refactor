package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Objects.ConfigFile;

import java.util.ArrayList;
import java.util.Objects;

public class ConfigMain {

    public static ArrayList<ConfigFile> Configs = new ArrayList<>();

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
