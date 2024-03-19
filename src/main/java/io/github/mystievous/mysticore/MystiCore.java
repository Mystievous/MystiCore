package io.github.mystievous.mysticore;

import io.github.mystievous.mysticore.commands.ReloadPalette;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;

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
        try {
            Palette.reloadColors();
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
        }

        PluginCommand command = Bukkit.getPluginCommand("paletteReload");
        if (command != null) {
            command.setExecutor(new ReloadPalette());
        }

        NBTUtils nbtUtils = new NBTUtils();
        Bukkit.getPluginManager().registerEvents(nbtUtils, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
