package com.jewelexx.membrane;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public final class Membrane extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        String disablePhantoms = this.getConfig().getString("disable-phantoms");

        Bukkit.getPluginManager().registerEvents(new MobHandler(disablePhantoms), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
