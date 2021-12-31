package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Objects.ActivityRule;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Objects.CustomEvents.GameSpawnsActivatedEvent;
import com.cloud.mcsu_rf.Objects.EventListener;
import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.Scoreboard_Element;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Pick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
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

        safeEventListeners.stream().filter(listener -> event.getEventName().equals(listener.getEventName())).forEach(eventListener -> {
            eventListener.getOnEvent().exec(event);
        });

        postListenersHandler(event);

    }

    public static void postListenersHandler(Event event) {
        switch(event.getEventName()) {
            case "BlockPlaceEvent":
                BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
                if (!placeEvent.isCancelled()) {
                    Block blockPlaced = placeEvent.getBlockPlaced();
                    if (getRuleActive("AutoIgniteTNT") && blockPlaced.getBlockData().getMaterial() == Material.TNT) {
                        blockPlaced.setType(Material.AIR);
                        TNTPrimed tnt = (TNTPrimed) blockPlaced.getWorld().spawnEntity(blockPlaced.getLocation().add(new Location(blockPlaced.getWorld(), 0.5, 0, 0.5)), EntityType.PRIMED_TNT);
                        tnt.setFuseTicks(40);
                    }
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
                joinMessage = ChatColor.BLUE + pName + ChatColor.WHITE + " has joined mCSU!!11!11! \nhol up is candice tho? "; // OG join message :)
                break;
            case "goshroom":
                p.getInventory().setHelmet(new ItemStack(Material.RED_MUSHROOM,1));
            case "JackyWackers":
                p.getInventory().setHelmet(new ItemStack(Material.RED_MUSHROOM,1));
            default:
                joinMessage= ChatColor.WHITE + pName + " has joined mCSU!!11!11!";
                break;

        }

        McsuPlayer.registerPlayer(p);
        //Scoreboard_Main.animateScoreboard(p);

        e.setJoinMessage(joinMessage);
        MCSU_Scoreboard sb = new MCSU_Scoreboard(new Scoreboard_Element[] { new Scoreboard_Element("Bottom_Line_Break"), new Scoreboard_Element("Team_Totals"), new Scoreboard_Element("Line_Break"), new Scoreboard_Element("Online_Players"), new Scoreboard_Element("Top_Line_Break") });
        Scoreboard_Main.Current_Scoreboard = sb;
        reloadScoreboard();

    }

    @EventHandler public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(!getRuleActive("PVP"));
        onRegisteredEvent(e);
    }

    @EventHandler public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            if (!getRuleActive("FallDamage")) e.setCancelled(true);
        } else if (
                e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                        ||
                        e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
        ) {
            if (!getRuleActive("ExplosionDamage")) {
                e.setDamage(0);
            }
        } else if (
                e.getCause().equals(EntityDamageEvent.DamageCause.VOID)
                        &&
                        ((Player) e.getEntity()).getGameMode() == GameMode.SPECTATOR
        ) {
            e.setCancelled(true);
        }

        onRegisteredEvent(e);
    }

    @EventHandler public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && !getRuleActive("PearlDamage")){
            e.getPlayer().setNoDamageTicks(1);
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

    @EventHandler public void onBlockExplode(BlockExplodeEvent e) {
        if (!getRuleActive("TileDrops")) e.setYield(0);
        e.setCancelled(!getRuleActive("TileBreaking"));

        onRegisteredEvent(e);
    }

    @EventHandler public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!getRuleActive("EntityDamage")) e.setDamage(0);

        onRegisteredEvent(e);
    }

    @EventHandler public void playerInteractEvent(PlayerInteractEvent e) {
        if (!getRuleActive("PlayerInteract")) {
            if(e.getClickedBlock().getType() == Material.SPRUCE_TRAPDOOR) {
                e.setCancelled(true);
            }
        }
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
        MCSU_Scoreboard sb = new MCSU_Scoreboard(new Scoreboard_Element[] { new Scoreboard_Element("Bottom_Line_Break"), new Scoreboard_Element("Team_Totals"), new Scoreboard_Element("Line_Break"), new Scoreboard_Element("Online_Players_Leave"), new Scoreboard_Element("Top_Line_Break") });
        Scoreboard_Main.Current_Scoreboard = sb;
        reloadScoreboard();
        McsuPlayer.McsuPlayers.remove(McsuPlayer.fromBukkit(e.getPlayer()));
        for(Player player : Bukkit.getOnlinePlayers()) {
            Tab.showTab(player,Bukkit.getOnlinePlayers().size()-1);
        }
    }

    @EventHandler public void onPlayerDeath(PlayerDeathEvent e) {

        if(e.getEntity().getPlayer().getLocation().getY() < 0) {
            Location loc = e.getEntity().getPlayer().getLocation().clone();
            loc.setY(60);
            latestDeathPos = loc;
        } else {
            latestDeathPos = e.getEntity().getPlayer().getLocation().clone();
        }

        e.getEntity().getPlayer().setGameMode(GameMode.SPECTATOR);

        onRegisteredEvent(e);

    }

    @EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {

        e.setRespawnLocation(latestDeathPos);

        onRegisteredEvent(e);

    }

    @EventHandler public void onPrepareItemCraftEvent(PrepareItemCraftEvent e) {
        if(!getRuleActive("Crafting")) e.getInventory().setResult( new ItemStack(Material.AIR));

        onRegisteredEvent(e);
    }

    @EventHandler public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        if(!getRuleActive("Hunger")) e.setFoodLevel(20);

        onRegisteredEvent(e);
    }

    @EventHandler public void onRightClickSmiter(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(e.getItem().getType().equals(Material.BLAZE_ROD)) {
                Player p = e.getPlayer();
                boolean found = false;
                for (int i = 0; i < 200; i++) {
                    List<Entity> entities = p.getNearbyEntities(i,64,i);
                    for (Entity en : entities) {
                        if (en.getType().equals(EntityType.PLAYER)) {
                            p.getWorld().spawnEntity(en.getLocation(),EntityType.LIGHTNING);
                            p.sendMessage(ChatColor.GOLD+"Smiting "+en.getName()+"!");
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            }
        }
    }


    //Events that just get passed to registered event with no other code

    @EventHandler public void onGameInitEvent(GameInitEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameCountdownEndEvent(GameCountdownEndEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameSpawnsActivatedEvent(GameSpawnsActivatedEvent e) { onRegisteredEvent(e); Bukkit.broadcastMessage("Game Spawns Activated"); }

    @EventHandler public void onProjectileHitEvent(ProjectileHitEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onEntityShootBowEvent(EntityShootBowEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onPlayerInteractEvent(PlayerInteractEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onInventoryClickEvent(InventoryClickEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onBlockPlaceEvent(BlockPlaceEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onEntityExplodeEvent(EntityExplodeEvent e) { onRegisteredEvent(e); }

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
        new ActivityRule("EntityDamage",false);
        new ActivityRule("PlayerMovement", true);
        new ActivityRule("FallDamage", false);
        new ActivityRule("ExplosionDamage", true);
        new ActivityRule("Crafting", false);
        new ActivityRule("AutoIgniteTNT", true);
        new ActivityRule("Hunger", false);
        new ActivityRule("PearlDamage", false);
        new ActivityRule("PlayerInteract",true);
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
