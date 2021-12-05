package com.cloud.mcsu_rf;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config_Main {

    private static File file;
    private static FileConfiguration configfile;

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

}
