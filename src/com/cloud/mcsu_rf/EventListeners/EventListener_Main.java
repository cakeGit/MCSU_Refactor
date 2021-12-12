package com.cloud.mcsu_rf.EventListeners;

import com.cloud.mcsu_rf.Objects.EventListener;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Objects;

public class EventListener_Main implements Listener {

    public static ArrayList<EventListener> eventListeners = new ArrayList();

    public static void addEventListener(EventListener eventListener) { eventListeners.add(eventListener); }

    public static void removeEventListener(EventListener eventListener) { eventListeners.remove(eventListener); }

    public static void executeListenerArrayList(ArrayList<EventListener> listeners, Event event) {

        for (EventListener listener : listeners) {

            listener.getOnEvent().exec(event);

        }

    }

    public static void onRegisteredEvent(Event event) {

        for (EventListener eventListener : eventListeners) {
            if (Objects.equals(eventListener.getEventName(), event.getEventName())) {
                eventListener.getOnEvent().exec(event);
            }
        }

    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) { onRegisteredEvent(e);
        Player p = e.getPlayer();
        String pName = p.getDisplayName();
        String joinMessage;

        switch (pName) { // use UUID in the future

            case "CakeIsTasty":
                joinMessage = ChatColor.BLUE + pName + ChatColor.WHITE + " has joined the pajin :("; // OG join message :)
                break;

            default:
                joinMessage= ChatColor.DARK_GREEN + pName + " has joined mCSU!!11!11!";
                break;

        }

        MCSU_Player.MCSU_Players.add(new MCSU_Player(p));
        p.setScoreboard(Scoreboard_Main.Current_Scoreboard);

        e.setJoinMessage(joinMessage);

    }

    @EventHandler public void onPlayerDeath(PlayerDeathEvent e) { onRegisteredEvent(e); }

}
