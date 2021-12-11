package com.cloud.mcsu_rf.EventListeners;

import com.cloud.mcsu_rf.Objects.EventListener;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class EventListener_Main implements Listener {

    public static ArrayList<EventListener> playerDeathListeners = new ArrayList();

    public static ArrayList<EventListener> getListenersArrayList(EventListener eventListener) {

        switch (eventListener.getEventName()) {

            case "PlayerDeathEvent": return playerDeathListeners;

        }


        throw new NullPointerException("Error: could not find event thingy for "+ eventListener.getEventName());
    }

    public static void addEventListener(EventListener eventListener) {

        getListenersArrayList(eventListener).add(eventListener);

        for (EventListener ebsenif : playerDeathListeners) {
            Bukkit.getLogger().info(ebsenif.getEventName());
        }

        for (EventListener playerDeathListener : playerDeathListeners) {
            Bukkit.getLogger().info(playerDeathListener.getEventName());
        }

    }

    public static void removeEventListener(EventListener eventListener) {

        switch (eventListener.getEventName()) {

            case "PlayerDeathEvent": playerDeathListeners.remove(eventListener); break;

        }

    }

    public static void executeListenerArrayList(ArrayList<EventListener> listeners, Event event) {
        Bukkit.getLogger().info("sdmfopsndfaaawqqq");

        for (EventListener listener : listeners) {

            Bukkit.getLogger().info("sdmfopsndfsssssss");

            listener.getOnEvent().exec(event);

        }

    }

    public static void onRegisteredEvent(Event event) {
        Bukkit.getLogger().info("sdmfopsnasddf");

        switch (event.getEventName()) {

            case "PlayerDeathEvent":
                Bukkit.getLogger().info("sdmfopsndasaf");
                executeListenerArrayList(playerDeathListeners, event);
                break;

        }

    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) { onRegisteredEvent(e);
        Player p = e.getPlayer();
        String pName = p.getDisplayName();
        String joinMessage;

        switch (pName) { // use UUID in the future

            case "CakeIsTasty":
                joinMessage = ChatColor.BLUE + pName + ChatColor.WHITE + " has joined the pain :("; // OG join message :)
                break;

            default:
                joinMessage= ChatColor.DARK_GREEN + pName + " has joined mCSU!!11!11!";
                break;

        }

        MCSU_Player.MCSU_Players.add(new MCSU_Player(p));

        e.setJoinMessage(joinMessage);

    }

    @EventHandler public void onPlayerDeath(PlayerDeathEvent e) { onRegisteredEvent(e); }

}
