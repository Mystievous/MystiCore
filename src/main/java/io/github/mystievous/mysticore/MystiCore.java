package io.github.mystievous.mysticore;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class MystiCore extends JavaPlugin {

    private static MystiCore me;

    public static MystiCore getInstance() {
        if (me != null) {
            return me;
        } else {
            throw new NullPointerException("MystiCore instance accessed before it has been created!");
        }
    }

    @Override
    public void onEnable() {
        me = this;
        // Plugin startup logic
        NBTUtils nbtUtils = new NBTUtils();
        Bukkit.getPluginManager().registerEvents(nbtUtils, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
