package io.github.mystievous.mysticore;

import io.github.mystievous.mysticore.commands.ReloadPalette;
import io.github.mystievous.mysticore.interact.UsableItemManager;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
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

    public static ComponentLogger getPluginLogger() {
        return me.getComponentLogger();
    }

    @Override
    public void onEnable() {
        me = this;
        // Plugin startup logic
        try {
            Palette.reloadColors();
        } catch (Exception e) {
            getComponentLogger().warn(e.getMessage());
            e.printStackTrace();
        }

        PluginCommand command = Bukkit.getPluginCommand("paletteReload");
        if (command != null) {
            command.setExecutor(new ReloadPalette());
        }

        NBTUtils nbtUtils = new NBTUtils();
        Bukkit.getPluginManager().registerEvents(nbtUtils, this);

        UsableItemManager usableItemManager = new UsableItemManager();
        Bukkit.getPluginManager().registerEvents(usableItemManager, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
