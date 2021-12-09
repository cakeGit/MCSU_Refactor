package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigFile  {

    public File configFile;
    public FileConfiguration config;

    public String name;
    public String id;
    public String fileName;

    public ConfigFile(String name, String id, String fileName) {

        this.name = name;
        this.id = id;
        this.fileName = fileName;

        this.configFile = new File(MCSU_Main.FileDir, this.fileName);

        if (!this.configFile.exists()) {

            this.configFile.getParentFile().mkdir();

            MCSU_Main.instance.saveResource("EmptyYml.yml", false);

            Path source = Paths.get(MCSU_Main.FileDir + "/EmptyYml.yml");

            try{

                // rename a file in the same directory
                Files.move(source, source.resolveSibling(this.fileName));

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
}