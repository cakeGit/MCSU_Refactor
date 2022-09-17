package com.cak.what.Util;

import org.bukkit.Bukkit;

/**
 * Shortcut for Bukkit.getLogger().info()
 */
public class Out {
    public static void log(Object o) {
        Bukkit.getLogger().info(o.toString());
    }
}
