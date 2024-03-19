package io.github.mystievous.mysticore;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.YamlConfigurations;
import net.kyori.adventure.text.format.NamedTextColor;

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

        PRIMARY = new Color(config.primary);
        SECONDARY = new Color(config.secondary);
        NEGATIVE_COLOR = new Color(config.negative);
        GRAYED_OUT = new Color(config.grayedOut);
    }

    @Configuration
    public static class PaletteConfiguration {
        @Comment("Colors are hex strings.")
        public String primary = Integer.toHexString(0x44b9ad);
        public String secondary = Integer.toHexString(0x6c8784);
        public String negative = Integer.toHexString(0xAA0000);

        public String grayedOut = Integer.toHexString(0x8e8e8e);


    }

}
