package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ConfigFile  {

    public File configFile;
    public FileConfiguration config;

    public String name;
    public String id;
    public String fileName;

    public ConfigFile(String name, String id, String fileName) {

        String subdirectory = "";
        configFileConstructor(name, id, fileName, subdirectory);

    }
    public ConfigFile(String name, String id, String fileName, String subdirectory) {

        configFileConstructor(name, id, fileName, subdirectory);

    }

    private void configFileConstructor(String name, String id, String fileName, String subdirectory) {

        this.name = name;
        this.id = id;
        this.fileName = fileName;

        this.configFile = new File(MCSU_Main.FileDir + subdirectory, this.fileName);

        if ( !this.configFile.exists() ) {

            new File(MCSU_Main.FileDir + subdirectory).mkdir();

            MCSU_Main.instance.saveResource("EmptyYml.yml", false);

            Path source = Paths.get(MCSU_Main.FileDir + "/EmptyYml.yml");

            try{

                // rename a file in the same directory
                Files.move(source, Path.of(MCSU_Main.FileDir + subdirectory + this.fileName));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void saveDat() {
        try {
            this.config.save( this.configFile );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}