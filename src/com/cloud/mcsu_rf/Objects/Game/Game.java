package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Game_Handlers.Schematic_Loader;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.GameFunction;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;

public class Game {

    String Name;
    GameState gameStates;

    String[] Maps = new String[1];

    ArrayList<GameFunction> gameFunctions = new ArrayList<>();

    public Game(String Name) {

        this.Name = Name;

        Bukkit.getLogger().info("Loading game " + Name);

        Game_Main.RegisteredGames.add(this);

    }

    public String getName() { return this.Name; }
    public void addGameFunction(GameFunction gameFunction) { this.gameFunctions.add(gameFunction); }

    @Deprecated
    public void setMapName(String Map_Name) {

        this.Maps[0] = Map_Name;

    }

    @Deprecated
    public void setMapNames(String[] Map_Names) {

        this.Maps = Map_Names;

    }

    public void startGame(World world) {

        Schematic_Loader.loadSchematic(this.Maps[0], BlockVector3.at(0, 100, 0), world);

        for (GameFunction gameFunction : this.gameFunctions) {
            gameFunction.enableFunction();
        }

    }
}
