package io.github.mystievous.mysticore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtil {

    public static ItemStack createItem(@Nullable Component name, Material material, int customModelData) {
        ItemStack item = new ItemStack(material);
        item.editMeta(itemMeta -> {
            if (name != null) {
                itemMeta.displayName(name.decoration(TextDecoration.ITALIC, false));
            }
            if (customModelData != 0) {
                itemMeta.setCustomModelData(customModelData);
            }
        });
        return item;
    }

    public static ItemStack createItem(@Nullable Component name, Material material) {
        return createItem(name, material, 0);
    }

    public static Component getItemName(@NotNull ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            return meta.hasDisplayName() ? meta.displayName() : Component.translatable(item.translationKey());
        } else {
            return Component.translatable(item.translationKey());
        }
    }

}
