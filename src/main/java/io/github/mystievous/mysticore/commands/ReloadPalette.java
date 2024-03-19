package io.github.mystievous.mysticore.commands;

import io.github.mystievous.mysticore.Palette;
import io.github.mystievous.mysticore.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadPalette implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        try {
            Palette.reloadColors();
        } catch (Exception e) {
            commandSender.sendMessage(TextUtil.errorMessage(e.getMessage()));
            return true;
        }
        commandSender.sendMessage(TextUtil.formatText("Reloaded plugin colors."));
        return true;
    }

}
