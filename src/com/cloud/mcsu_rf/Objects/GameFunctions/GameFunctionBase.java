package com.cloud.mcsu_rf.Objects.GameFunctions;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Objects.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.ArrayList;

public class GameFunctionBase {

    protected ArrayList<String> boundEventNames = new ArrayList<>();
    protected ArrayList<EventListener> eventListeners = new ArrayList<>();

    protected boolean isTemporary = false;

    public GameFunctionBase() { }

    public void setEnabled(boolean enabled) {

        onEnabledToggle(enabled);

        if (enabled) {

            onEnable();

            for (String eventName : this.boundEventNames) {
                /*Bukkit.getLogger().info(
                        "Enabling " +
                                eventName +
                                " binding to an instance of " +
                                this.getClass().getSimpleName()
                );*/

                EventListener newListener = new EventListener(eventName, this::onBoundEvent);
                eventListeners.add(newListener);
                EventListenerMain.addEventListener(newListener);
            }

        } else  {

            onDisable();

            /*Bukkit.getLogger().info(
                    "Disabling all listener bindings for gameFunction " +
                            this.getClass().getSimpleName()
            );*/

            for ( EventListener listener : eventListeners ) {

                /*Bukkit.getLogger().info(
                        "Unbinding " +
                                listener.getEventName()
                );*/

                EventListenerMain.removeEventListener(
                        listener
                );
            }

        }

    }


    public ArrayList<String> getBoundEventNames() { return boundEventNames; }

    public void setIsTemporary(boolean temporary) { isTemporary = temporary; }
    public boolean getIsTemporary() { return isTemporary; }

    //Common methods

    public void onBoundEvent(Event event) { }
    public void onEnable() {}
    public void onDisable() {}
    public void onEnabledToggle(boolean enabled) {}

}
