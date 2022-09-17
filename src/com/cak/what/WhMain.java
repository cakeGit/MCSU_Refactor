package com.cak.what;

import com.cak.what.MenuAPI.InventoryMenuEvents;
import com.cak.what.RegisterAPI.Register;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhMain extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {

        plugin = this;
        Debug.enable(plugin);

        Register.eventHandler(WhMain.plugin, new InventoryMenuEvents());

        Commands.init();

    }

    @Override
    public void onDisable() { }

}
