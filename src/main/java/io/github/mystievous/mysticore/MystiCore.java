package io.github.mystievous.mysticore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MystiCore extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        NBTUtils nbtUtils = new NBTUtils();
        Bukkit.getPluginManager().registerEvents(nbtUtils, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
