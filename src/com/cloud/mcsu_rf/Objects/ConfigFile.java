package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile { // the most extra object to exist

    public String name;
    public String configID;
    public String fileName;

    public File file;
    public FileConfiguration configFile;

    public ConfigFile(String name, String configID, String fileName) {

        this.name = name;
        this.configID = configID;
        this.fileName = fileName;

        this.file = new File(MCSU_Main.Mcsu_Plugin.getDataFolder(), fileName);

        this.initFile();

    }

    public void loadFileDat() {
        this.configFile = YamlConfiguration.loadConfiguration(this.file);
    }

    public void initFile() {

        if(!this.file.exists()) {

            try {

                this.file.createNewFile();
                this.initFile();
                this.configFile.options().copyDefaults(true);
                this.save();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        this.loadFileDat();

    }

    public void save() {

        try {

            configFile.save(file);

        } catch (IOException e) {

            Bukkit.getLogger().info("Couldn't save file " + this.fileName);
            e.printStackTrace();

        }

    }

    public void updateConfig() {
        this.save();
        this.configFile.options().copyDefaults(true);
        this.loadFileDat();
    }

}
