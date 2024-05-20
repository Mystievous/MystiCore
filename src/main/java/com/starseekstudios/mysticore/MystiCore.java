package com.starseekstudios.mysticore;

import com.starseekstudios.mysticore.commands.PaletteCommand;
import com.starseekstudios.mysticore.interact.UsableItemManager;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPIConfig;
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
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).usePluginNamespace());
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        me = this;
        // Plugin startup logic
        try {
            Palette.reloadColors();
        } catch (Exception e) {
            getComponentLogger().warn(e.getMessage());
            e.printStackTrace();
        }

        CommandAPI.registerCommand(PaletteCommand.class);

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
