package io.github.mystievous.mysticore;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.YamlConfigurations;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Palette {

    public static Color PRIMARY = new Color(0x44b9ad);
    public static Color SECONDARY = new Color(0x6c8784);
    public static Color NEGATIVE_COLOR = new Color(NamedTextColor.DARK_RED.value());
    public static Color GRAYED_OUT = new Color(0x8e8e8e);

    public static void reloadColors() {
        Path configPath = Paths.get(MystiCore.getInstance().getDataFolder().getPath(), "palette.yml");
        PaletteConfiguration config = YamlConfigurations.update(configPath, PaletteConfiguration.class);

        Color primary = new Color(config.primary);
        Color secondary = new Color(config.secondary);
        Color negative = new Color(config.negative);
        Color grayedOut = new Color(config.grayedOut);

        PaletteReloadEvent event = new PaletteReloadEvent(primary, secondary, negative, grayedOut);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        PRIMARY = event.getPrimary();
        SECONDARY = event.getSecondary();
        NEGATIVE_COLOR = event.getNegative();
        GRAYED_OUT = event.getGrayedOut();
    }

    @Configuration
    public static class PaletteConfiguration {
        @Comment("Colors are hex strings.")
        public String primary = Integer.toHexString(0x44b9ad);
        public String secondary = Integer.toHexString(0x6c8784);
        public String negative = Integer.toHexString(0xAA0000);

        public String grayedOut = Integer.toHexString(0x8e8e8e);

    }

    public static class PaletteReloadEvent extends Event implements Cancellable {
        private static final HandlerList HANDLERS_LIST = new HandlerList();

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS_LIST;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS_LIST;
        }

        private boolean cancelled = false;

        private Color primary;
        private Color secondary;
        private Color negative;
        private Color grayedOut;

        public PaletteReloadEvent(Color primary, Color secondary, Color negative, Color grayedOut) {
            this.primary = primary;
            this.secondary = secondary;
            this.negative = negative;
            this.grayedOut = grayedOut;
        }

        public Color getPrimary() {
            return primary;
        }

        public void setPrimary(Color primary) {
            this.primary = primary;
        }

        public Color getSecondary() {
            return secondary;
        }

        public void setSecondary(Color secondary) {
            this.secondary = secondary;
        }

        public Color getNegative() {
            return negative;
        }

        public void setNegative(Color negative) {
            this.negative = negative;
        }

        public Color getGrayedOut() {
            return grayedOut;
        }

        public void setGrayedOut(Color grayedOut) {
            this.grayedOut = grayedOut;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

    }

}
