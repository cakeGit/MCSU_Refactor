package com.cloud.mcsu_rf.Objects.Game;

public class GamemodeOption {

    int weight;
    String name;

    public GamemodeOption( int weight, String name ) {

        this.weight = weight;
        this.name = name;

    }

    public String getName() { return name; }
    public int getWeight() { return weight; }

}
