package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Objects.ConfigFile;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessages {

    public static ConfigFile messagesConfig;

    public static void init() {
        messagesConfig = new ConfigFile("DeathMessages", "dm", "deathMessages.yml");
    }

    public static String getMessage(PlayerDeathEvent deathEvent) {
        return deathEvent.getEntity().getLastDamageCause().getCause().toString();
    }

}
