package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import org.bukkit.Bukkit;

public class Game {

    public String Name;

    String[] Maps = new String[1];

    public Game(String Name) {

        this.Name = Name;

        Bukkit.getLogger().info("Creating game " + Name);

        Game_Main.RegisteredGames.add(this);

    }

    public String getName() {

        return this.Name;

    }

    public void setMapName(String Map_Name) {

        this.Maps[0] = Map_Name;

    }

    public void setMapNames(String[] Map_Names) {

        this.Maps = Map_Names;

    }

}
