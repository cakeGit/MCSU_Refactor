package com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Objects.EventListener;
import org.bukkit.event.Event;

import java.util.ArrayList;

public class GameFunctionBase {

    ArrayList<String> boundEventNames = new ArrayList<>();

    public GameFunctionBase() { }

    public void setEnabled(boolean enabled) {

        for (String eventName : this.boundEventNames) {

            if (enabled) {
                EventListenerMain.addEventListener(new EventListener(eventName, this::onBoundEvent)); // some weir adutogen code - passes the event to runEventHandler
            } else  {
                EventListenerMain.removeEventListener(new EventListener(eventName, this::onBoundEvent)); // some weir adutogen code - passes the event to runEventHandler
            }

        }

    }


    public ArrayList<String> getBoundEventNames() { return this.boundEventNames; }


    //Common methods

    public void onBoundEvent(Event event) { }

}
