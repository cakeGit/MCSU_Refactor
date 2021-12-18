package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Objects.ActivityRule;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameSpawnsActivatedEvent;
import com.cloud.mcsu_rf.Objects.EventListener;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.ShorthandClasses.Pick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.Objects;

import static com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main.reloadScoreboard;

public class EventListenerMain implements Listener {

    //Event handler stuff

    public static ArrayList<EventListener> eventListeners = new ArrayList<>();

    Location latestDeathPos;

    public static void addEventListener(EventListener eventListener) { eventListeners.add(eventListener); }
    public static void removeEventListener(EventListener eventListener) { eventListeners.remove(eventListener); }

    public static void onRegisteredEvent(Event event) {

        ArrayList<EventListener> safeEventListeners = (ArrayList<EventListener>) eventListeners.clone();

        safeEventListeners.stream().filter(listener -> Objects.equals(listener.eventName, event.getEventName())).forEach(eventListener -> {
            eventListener.getOnEvent().exec(event);
        });

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

        McsuPlayer.registerPlayer(p);
        //Scoreboard_Main.animateScoreboard(p);

        e.setJoinMessage(joinMessage);

    }

    @EventHandler public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(!getRuleActive("PVP"));

        onRegisteredEvent(e);

    }

    @EventHandler public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(!getRuleActive("FallDamage"));
        }

        onRegisteredEvent(e);

    }

    @EventHandler public void onPlayerMove(PlayerMoveEvent e) {
        e.setCancelled(!getRuleActive("PlayerMovement"));

        onRegisteredEvent(e);

    }

    @EventHandler public void onBlockBreak(BlockBreakEvent e) {
        e.setDropItems(getRuleActive("TileDrops"));
        e.setCancelled(!getRuleActive("TileBreaking"));

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
                        "hey ;)",
                        "Call me mojang because I can make your bedrock"
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

    @EventHandler public void onPlayerDeath(PlayerDeathEvent e) {

        latestDeathPos = e.getEntity().getPlayer().getLocation().clone();
        Bukkit.getLogger().info(latestDeathPos.toString() + " is wher diedings!!?1////???1/11/!1/!?!1");

        e.getEntity().getPlayer().setGameMode(GameMode.SPECTATOR);

        onRegisteredEvent(e);

    }

    @EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {

        e.setRespawnLocation(latestDeathPos);

        onRegisteredEvent(e);

    }


    //Events that just get passed to registered event with no other code

    @EventHandler public void onGameInitEvent(GameInitEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameCountdownEndEvent(GameCountdownEndEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameSpawnsActivatedEvent(GameSpawnsActivatedEvent e) { onRegisteredEvent(e); }


    //Activity rules

    public static ArrayList<ActivityRule> activityRules = new ArrayList<>();

    public static void resetActivityRules() {

        for (ActivityRule activityRule : activityRules) {
            activityRule.resetActive();
        }

    }

    public static void registerActivityRules() {

        new ActivityRule("TileDrops", false);
        new ActivityRule("TileBreaking", true);
        new ActivityRule("PVP", false);
        new ActivityRule("PlayerMovement", true);
        new ActivityRule("FallDamage", false);

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
