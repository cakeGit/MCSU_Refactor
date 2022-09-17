package com.cloud.mcsu_rf.Definitions.Game;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class GamemodeOption {

    int weight;
    String name;
    ChatColor chatColor;
    Sound selectSound;

    public GamemodeOption( int weight, String name ) {init(weight, name, null, null);}

    public GamemodeOption( int weight, String name, ChatColor chatColor ) {init(weight, name, chatColor, null);}

    public GamemodeOption( int weight, String name, Sound selectSound ) {init(weight, name, null, selectSound);}

    public GamemodeOption( int weight, String name, ChatColor chatColor, Sound selectSound ) {init(weight, name, chatColor, selectSound);}

    private void init( int weight, String name, ChatColor chatColor, Sound selectSound ) {
        this.weight = weight;
        this.name = name;
        this.chatColor = chatColor;
        this.selectSound = selectSound;
    }

    public String getName() { return name; }
    public int getWeight() { return weight; }

    public Object getStyledName() {
        return chatColor == null ? name : chatColor + name;
    }

    public Sound getSelectSound() {
        return selectSound;
    }
}
