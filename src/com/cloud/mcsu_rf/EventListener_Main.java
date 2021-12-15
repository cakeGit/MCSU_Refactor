package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Objects.ActivityRule;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Objects.EventListener;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.ShorthandClasses.Pick;
import com.cloud.mcsu_rf.Tab;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.Bukkit;
import com.cloud.mcsu_rf.ShorthandClasses.Centered;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.Objects;

import static com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main.reloadScoreboard;

public class EventListener_Main implements Listener {

    //Event handler stuff

    public static ArrayList<EventListener> eventListeners = new ArrayList<>();

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
        /*
        BossBar bar = Bukkit.createBossBar(ChatColor.RED+"§lMCSU", BarColor.RED, BarStyle.SOLID);
        bar.setVisible(true);
        bar.addPlayer(p);
         */
        Tab.showTab(p,Bukkit.getOnlinePlayers().size());

        switch (pName) { // use UUID in the future

            case "CakeIsTasty":
                joinMessage = ChatColor.BLUE + pName + ChatColor.WHITE + " has joined the pain :("; // OG join message :)
                break;
            case "WaitWhosCandice":
                joinMessage = ChatColor.BLUE + pName + ChatColor.WHITE + " has joined mCSU!!11!11! \n hol up is candice tho? "; // OG join message :)
                break;
            default:
                joinMessage= ChatColor.WHITE + pName + " has joined mCSU!!11!11!";
                break;

        }

        MCSU_Player.MCSU_Players.add(new MCSU_Player(p));

        for ( MCSU_Team team : Team_Main.Teams ) {
            if (team.getMemberUUIDs().contains(p.getUniqueId().toString())) {
                assert MCSU_Player.getPlayerByBukkitPlayer(p) != null;
                MCSU_Player.getPlayerByBukkitPlayer(p).setTeam(team);
            }
        }

        reloadScoreboard();

        e.setJoinMessage(joinMessage);

    }

    @EventHandler public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!getRuleActive("PVP")) { e.setCancelled(true); }

        onRegisteredEvent(e);

    }

    @EventHandler public void onPlayerMove(PlayerMoveEvent e) {
        if (!getRuleActive("PlayerMovement")) { e.setCancelled(true); }

        onRegisteredEvent(e);

    }

    @EventHandler public void onServerListPing(ServerListPingEvent e) {
        e.setMotd(
                ChatColor.RED +""+ ChatColor.BOLD + "MCSU in development! \n" + ChatColor.RESET +""+ ChatColor.AQUA +""+ ChatColor.ITALIC +
                Pick.Random(
                        "wait whos candice?",
                        "the cake is " + ChatColor.STRIKETHROUGH + "a lie"
                                + ChatColor.RESET +""+ ChatColor.AQUA +""+ ChatColor.ITALIC + " tasty",
                        "mcsu?",
                        "hey ;)"
                )
        );

        onRegisteredEvent(e);
    }

    @EventHandler public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String pName = p.getDisplayName();
        String quitMessage;

        quitMessage = ChatColor.BLUE + pName + ChatColor.BLUE + " has left MCSU :(";
        e.setQuitMessage(quitMessage);
        reloadScoreboard();
        for(Player player : Bukkit.getOnlinePlayers()) {
            Tab.showTab(player,Bukkit.getOnlinePlayers().size()-1);
        }
    }


    //Events that just get passed to registered event with no other code
    @EventHandler public void onPlayerDeath(PlayerDeathEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameInitEvent(GameInitEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameCountdownEndEvent(GameCountdownEndEvent e) { onRegisteredEvent(e); }


    //Activity rules

    public static ArrayList<ActivityRule> activityRules = new ArrayList<>();

    public static void resetActivityRules() {

        for (ActivityRule activityRule : activityRules) {
            activityRule.resetActive();
        }

    }

    public static void registerActivityRules() {

        new ActivityRule("TileDrops", true);
        new ActivityRule("PVP", false);
        new ActivityRule("PlayerMovement", true);

    }

    public static ActivityRule getActivityRule(String name) {

        for (ActivityRule activityRule : activityRules) {

            if (Objects.equals(activityRule.getName(), name)) {

                return activityRule;

            }

        }

        return null;
    }

    public static Boolean getRuleActive(String name) {

        return Objects.requireNonNull(getActivityRule(name)).getActive();
    }

    public static void setActivityRule(String name, Boolean value) {

        Objects.requireNonNull(getActivityRule(name)).setActive(value);

    }

}
