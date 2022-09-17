package com.cak.what.ConfigApi;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigFile  {

    protected File configFile;
    protected FileConfiguration config;

    protected String fileName;

    public ConfigFile(String fileName) {

        configFileConstructor(fileName, "");

    }
    public ConfigFile(String fileName, String subdirectory) {

        configFileConstructor(fileName, subdirectory);

    }

    private void configFileConstructor(String fileName, String subdirectory) {

        this.fileName = fileName;
        String path = MCSU_Main.FileDir + "/" + subdirectory + "/";

        this.configFile = new File(path, this.fileName);

        if ( !this.configFile.exists() ) {

            new File(path).mkdir();

            MCSU_Main.instance.saveResource("EmptyYml.yml", false);

            Path source = Paths.get(MCSU_Main.FileDir + "/EmptyYml.yml");

            try{

                // rename a file in the same directory
                Files.move(source, Path.of(path + this.fileName));

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

    public File getConfigFile() {
        return configFile;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getFileName() {
        return fileName;
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public List getList(String path) {
        return this.config.getList(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }

    public void remove(String path) {
        this.config.set(path, null);
    }

    public Object getWhere(ObjectChecker checker) {
        for (String path : this.config.getKeys(false)) {
            if (checker.check(this.config.get(path))) {
                return this.config.get(path);
            }
        }
        return null;
    }

    public Object getWhereKey(ObjectChecker checker) {
        for (String path : this.config.getKeys(false)) {
            if (checker.check(path)) {
                return this.config.get(path);
            }
        }
        return null;
    }

}