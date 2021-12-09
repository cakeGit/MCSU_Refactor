package com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.ArrayList;

public class GameFunction {

    ArrayList<String> boundEventNames = new ArrayList();

    public GameFunction() { }

    public ArrayList<String> getBoundEventNames() { return this.boundEventNames; }

    public void runEventHandler(Event event) { }

}
