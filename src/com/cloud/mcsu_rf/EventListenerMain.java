package com.cloud.mcsu_rf;

import com.cak.what.ConfigApi.ConfigFile;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Pick;
import com.cloud.mcsu_rf.Definitions.ActivityRule;
import com.cloud.mcsu_rf.Definitions.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Definitions.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Definitions.CustomEvents.GameSpawnsActivatedEvent;
import com.cloud.mcsu_rf.Definitions.EventListener;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.McsuScoreboard;
import org.bukkit.*;
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
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventListenerMain implements Listener {

    //Event handler stuff

    public static ArrayList<EventListener> eventListeners = new ArrayList<>();

    Location latestDeathPos;

    public static ConfigFile joinMessages = new ConfigFile("joinMessages.yml");

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
        if ("BlockPlaceEvent".equals(event.getEventName())) {
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
        /*
        BossBar bar = Bukkit.createBossBar(ChatColor.RED+"§lMCSU", BarColor.RED, BarStyle.SOLID);
        bar.setVisible(true);
        bar.addPlayer(p);
         */
        Tab.showTab(p,Bukkit.getOnlinePlayers().size());

        McsuPlayer.registerPlayer(p);
        McsuScoreboard.defaultScoreboard.update();

        Bukkit.getLogger().info("[+] " + p.getName());

        String joinMessage;

        AtomicBoolean uuidLinked = new AtomicBoolean(false);

        joinMessage = (String) joinMessages.getWhereKey(x-> {
            uuidLinked.set(((String) x).endsWith(p.getUniqueId().toString()));
            return ((String) x).endsWith(p.getUniqueId().toString()) || ((String) x).startsWith(p.getName());
        });

        if (joinMessage!=null) {
            if (!uuidLinked.get()) {

                joinMessages.set(p.getName() + "-" + p.getUniqueId(), joinMessage);
                joinMessages.remove(p.getName());
                joinMessages.saveDat();
            }
        } else {
            joinMessage = Pick.Random((ArrayList<String>) joinMessages.getList("$Defaults"));
        }

        e.setJoinMessage(
                joinMessage
                        .replace("${player}", p.getDisplayName())
                        .replace("${tcol}", McsuPlayer.fromBukkit(p).getColour())
        );
    }

    @EventHandler
    public static void noDmgSpawn(EntityDamageEvent e) {
        int[] coords1 = {
                -165,
                3,
                120
        };
        int[] coords2 = {
                21,
                48,
                273
        };
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if(isPlayerInRegion(player,coords1,coords2)) {
                e.setCancelled(true);
            }
        }
    }

    public static boolean isPlayerInRegion(Player player, int[] coords1, int[] coords2) {

        Location pLoc = player.getLocation();
        int[] pCoords = { pLoc.getBlockX(), pLoc.getBlockY(), pLoc.getBlockZ() };

        for(int index = 0; index < pCoords.length; index++) {
            if(!isNumBetween(pCoords[index], coords1[index], coords2[index])) return false;
        }

        return true;
    }

    public static boolean isNumBetween(int targetNum, int min, int max) {
        if(min > max) {
            int i = min;
            min = max;
            max = i;
        }
        return ( (targetNum >= min) && (targetNum <= max) );
    }

    // Bouncy Glass for Trampoline/Slimekour

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        Location l = player.getLocation();
        l.add(0, -1, 0);
        Block b = l.getBlock();
        if (b.getType() == Material.BLACK_STAINED_GLASS || b.getType() == Material.RED_STAINED_GLASS || b.getType() == Material.BLUE_STAINED_GLASS) {
            player.setVelocity(new Vector(0, 2, 0));
            player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);
        } else {
            return;
        }
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        Player player = (Player) e.getEntity();
        World world = player.getWorld();
        Location l = player.getLocation();
        l.add(0, -1, 0);
        Block b = l.getBlock();
        if (b.getType() == Material.BLACK_STAINED_GLASS || b.getType() == Material.RED_STAINED_GLASS || b.getType() == Material.BLUE_STAINED_GLASS) {
            if (player.isSneaking()) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.setVelocity(new Vector(0, 2, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);
            }
        } else {
            return;
        }
    }

    @EventHandler public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(!getRuleActive("PVP"));
        onRegisteredEvent(e);
    }

    @EventHandler public void onEntityDamage(EntityDamageEvent e) {
        if ( e.getCause().equals(EntityDamageEvent.DamageCause.FALL) ) {
            e.setCancelled(!getRuleActive("FallDamage"));
        } else if (
                ( e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                        ||
                        e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) )
                && !getRuleActive("ExplosionDamage")
        ) {
                e.setDamage(0);
        } else if (
                e.getCause().equals(EntityDamageEvent.DamageCause.VOID)
                        &&
                        ((Player) e.getEntity()).getGameMode() == GameMode.SPECTATOR
        ) {
            e.setCancelled(true);
        } else if (
                e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)
        ) {
            e.setCancelled(!getRuleActive("Suffocation"));
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
            try {
                if(Objects.requireNonNull(e.getClickedBlock()).getType() == Material.SPRUCE_TRAPDOOR) {
                    e.setCancelled(true);
                }
            } catch (NullPointerException ignored) {}
        }

        if (!getRuleActive("PlaceFireworks")) {
            try {
                if(Objects.requireNonNull(e.getItem()).getType() == Material.FIREWORK_ROCKET &&
                        e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    e.setCancelled(true);
                }
            } catch (NullPointerException ignored) {}
        }
    }

    @EventHandler public void onServerListPing(ServerListPingEvent e) {
        e.setMotd(
                ChatColor.RED +""+ ChatColor.BOLD + "MCSU in development! \n" + ChatColor.RESET +""+ ChatColor.GRAY +""+ ChatColor.ITALIC +
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
        McsuPlayer.McsuPlayers.remove(McsuPlayer.fromBukkit(e.getPlayer()));
        for(Player player : Bukkit.getOnlinePlayers()) {
            Tab.showTab(player,Bukkit.getOnlinePlayers().size()-1);
        }
    }

    @EventHandler public void onPlayerDeath(PlayerDeathEvent e) {

        if(Objects.requireNonNull(e.getEntity().getPlayer()).getLocation().getY() < 0) {
            Location loc = e.getEntity().getPlayer().getLocation().clone();
            loc.setY(60);
            latestDeathPos = loc;
        } else {
            latestDeathPos = e.getEntity().getPlayer().getLocation().clone();
        }

        e.getEntity().getPlayer().setGameMode(GameMode.SPECTATOR);

        String deathMessage = DeathMessages.getMessage(e);
        if(deathMessage!=null) {e.setDeathMessage(deathMessage);}

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

    @EventHandler public void onInventoryClickEvent(InventoryClickEvent e) {
        if(getRuleActive("LockInvItems")) e.setCancelled(true);

        onRegisteredEvent(e);
    }

    @EventHandler public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        if(getRuleActive("LockInvItems")) e.setCancelled(true);

        onRegisteredEvent(e);
    }

    @EventHandler public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        if(!getRuleActive("Hunger")) e.setFoodLevel(20);

        onRegisteredEvent(e);
    }

    @EventHandler public void onRightClickSmiter(PlayerInteractEvent e) {
        try {
            if( ( e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) )) {
                if(Objects.requireNonNull(e.getItem()).getType().equals(Material.BLAZE_ROD)) {
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
        } catch (NullPointerException ignored) {}

    }


    //Events that just get passed to registered event with no other code

    @EventHandler public void onGameInitEvent(GameInitEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameCountdownEndEvent(GameCountdownEndEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onGameSpawnsActivatedEvent(GameSpawnsActivatedEvent e) { onRegisteredEvent(e); }

    @EventHandler public void onProjectileHitEvent(ProjectileHitEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onProjectileLaunchEvent(ProjectileLaunchEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onEntityShootBowEvent(EntityShootBowEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onPlayerInteractEvent(PlayerInteractEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onBlockPlaceEvent(BlockPlaceEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onEntityExplodeEvent(EntityExplodeEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onEntityDamageEvent(EntityDamageEvent e) { onRegisteredEvent(e); }
    @EventHandler public void onPlayerMoveEvent(PlayerMoveEvent e) { onRegisteredEvent(e); }

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
        new ActivityRule("Suffocation",true);
        new ActivityRule("PlaceFireworks",true);
        new ActivityRule("LockInvItems",false);

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
