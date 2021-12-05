package com.cloud.mcsu_rf;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config_Main {

    private static File pointsFile;
    private static File spawnsFile;
    private static FileConfiguration pointsFileConfig;
    private static FileConfiguration spawnsFileConfig;

    public static void setupPointsConfig() {
        pointsFile = new File(Bukkit.getServer().getPluginManager().getPlugin("MCSU_Refactored").getDataFolder(), "Points.yml");
        if(!pointsFile.exists()) {
            try {
                pointsFile.createNewFile();
                Config_Main.setupPointsConfig();
                Config_Main.getPointsFileConfig().options().copyDefaults(true);
                Config_Main.save("Points");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pointsFileConfig = YamlConfiguration.loadConfiguration(pointsFile);
    }

    public static void setupSpawnsConfig() {
        spawnsFile = new File(Bukkit.getServer().getPluginManager().getPlugin("MCSU_Refactored").getDataFolder(), "Spawns.yml");
        if(!spawnsFile.exists()) {
            try {
                spawnsFile.createNewFile();
                Config_Main.setupSpawnsConfig();
                Config_Main.getSpawnsFileConfig().options().copyDefaults(true);
                Config_Main.save("Spawns");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        spawnsFileConfig = YamlConfiguration.loadConfiguration(pointsFile);
    }

    public static FileConfiguration getSpawnsFileConfig() {
        return spawnsFileConfig;
    }

    public static FileConfiguration getPointsFileConfig() {
        return pointsFileConfig;
    }

    public static void save(String file) {
        try {
            switch(file) {
                case "Points":
                    pointsFileConfig.save(pointsFile);
                case "Spawns":
                    spawnsFileConfig.save(spawnsFile);
            }
        } catch (IOException e) {
            System.out.println("Couldn't save file.");
            e.printStackTrace();
        }
    }

    public static void reload() {
        spawnsFileConfig = YamlConfiguration.loadConfiguration(spawnsFile);
        pointsFileConfig = YamlConfiguration.loadConfiguration(pointsFile);
    }

}
