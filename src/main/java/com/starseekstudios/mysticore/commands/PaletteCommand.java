package com.starseekstudios.mysticore.commands;

import com.starseekstudios.mysticore.Palette;
import com.starseekstudios.mysticore.TextUtil;
import dev.jorel.commandapi.annotations.*;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

@Command("palette")
@Permission("mysticore.palette")
@Help("Manage palette colors for MystiCore dependant plugins.")
public class PaletteCommand {

    @Default
    public static void palette(CommandSender sender) {
        sender.sendMessage(TextUtil.formatText("--- Palette Help ---"));
        sender.sendMessage(TextUtil.formatText("/palette -- Show the help menu."));
        sender.sendMessage(TextUtil.formatText("/palette reload -- Reload palette from the config file."));
    }

    @Subcommand("reload")
    public static void reload(CommandSender sender) {
        Palette.reloadColors();
        sender.sendMessage(TextUtil.formatText("Reloaded plugin colors."));
        sender.sendMessage(Component.text("- Primary", Palette.PRIMARY.toTextColor()));
        sender.sendMessage(Component.text("- Secondary", Palette.SECONDARY.toTextColor()));
        sender.sendMessage(Component.text("- Negative", Palette.NEGATIVE.toTextColor()));
        sender.sendMessage(Component.text("- Disabled", Palette.DISABLED.toTextColor()));
    }

}


