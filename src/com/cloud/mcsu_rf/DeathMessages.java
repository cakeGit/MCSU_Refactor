package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Objects.ConfigFile;

public class DeathMessages {
    public static ConfigFile messagesConfig;
    public static void init() {
        messagesConfig = new ConfigFile("DeathMessages", "dm", "deathMessages.yml");
    }
}
