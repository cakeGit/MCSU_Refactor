package com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function;

import com.cloud.mcsu_rf.EventListeners.EventListener_Main;
import com.cloud.mcsu_rf.Objects.EventListener;
import org.bukkit.event.Event;

import java.util.ArrayList;

public class GameFunction {

    ArrayList<String> boundEventNames = new ArrayList<>();

    public GameFunction() { }

    public void setEnabled(boolean enabled) {

        for (String eventName : this.boundEventNames) {

            if (enabled) {
                EventListener_Main.addEventListener(new EventListener(eventName, this::runEventHandler)); // some weir adutogen code - passes the event to runEventHandler
            } else  {
                EventListener_Main.removeEventListener(new EventListener(eventName, this::runEventHandler)); // some weir adutogen code - passes the event to runEventHandler
            }

        }

    }


    public ArrayList<String> getBoundEventNames() { return this.boundEventNames; }


    //Common methods

    public void runEventHandler(Event event) { }

}
