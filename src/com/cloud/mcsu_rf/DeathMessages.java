package com.cloud.mcsu_rf;

import com.cak.what.ConfigApi.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Random;

public class DeathMessages {

    public static ConfigFile messagesConfig;

    public static void init() {
        messagesConfig = new ConfigFile("deathMessages.yml");
    }

    public static String getMessage(PlayerDeathEvent deathEvent) {
        try{

            String cause = deathEvent.getEntity().getLastDamageCause().getCause().toString();
            Entity killer = deathEvent.getEntity().getKiller();

            try {
                Bukkit.getLogger().info("Breakpoint 1");
                killer = ((EntityDamageByEntityEvent) deathEvent.getEntity().getLastDamageCause()).getDamager();
                Bukkit.getLogger().info("Breakpoint 2");
            }catch (Exception e){Bukkit.getLogger().info(e.getMessage());}

            if (killer != null) {
                Bukkit.getLogger().info(killer.toString());
            } else {
                Bukkit.getLogger().info("Killer is null");
            }

            Bukkit.getLogger().info(deathEvent.getEntity() + " died due to " + cause);

                List<?> messages = messagesConfig.getConfig().getList(cause);
                int mesIndex = new Random().nextInt(messages.size());
                String message = messages.get(mesIndex).toString();

                message = message.replace("${player}", deathEvent.getEntity().getDisplayName());
                if(killer != null) {
                    message = message.replace("${killer}", deathEvent.getEntity().getKiller().getDisplayName());
                }

                return message;

        }catch(Exception ignored){}

        return deathEvent.getDeathMessage().replace(deathEvent.getEntity().getName(), deathEvent.getEntity().getDisplayName());
    }

}
