package com.starseekstudios.mysticore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextUtil {

    public static @Nullable Component noItalic(@Nullable Component component) {
        if (component != null) {
            return component.decoration(TextDecoration.ITALIC, false);
        }
        return null;
    }

    public static Component noItalic(String text) {
        return noItalic(Component.text(text));
    }

    public static Component formatText(Component text) {
        return text.decoration(TextDecoration.ITALIC, false).color(Palette.PRIMARY.toTextColor());
    }

    public static Component formatText(String text) {
        return formatText(Component.text(text));
    }

    public static List<Component> formatTexts(Component... components) {
        return Arrays.stream(components).map(TextUtil::formatText).collect(Collectors.toList());
    }

    public static List<Component> formatTexts(String... text) {
        return Arrays.stream(text).map(TextUtil::formatText).collect(Collectors.toList());
    }

    public static String formatBlockType(Material material) {
        String name = material.name().toLowerCase();
        StringBuilder output = new StringBuilder();
        for (String word : name.split("_")) {
            output.append(StringUtils.capitalize(word)).append(' ');
        }
        return output.toString();
    }

    public static Component space(int space) {
        return Component.translatable(String.format("space.%d", space));
    }

    public static Component errorMessage(Component text) {
        return text.color(Palette.NEGATIVE.toTextColor());
    }

    public static Component errorMessage(String text) {
        return errorMessage(Component.text(text));
    }

}
