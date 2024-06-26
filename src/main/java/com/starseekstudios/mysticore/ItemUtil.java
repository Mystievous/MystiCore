package com.starseekstudios.mysticore;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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

    public static void hideAttributes(ItemMeta itemMeta) {
        NamespacedKey key = NamespacedKey.fromString("hide-attributes-luck", MystiCore.getInstance());
        if (key == null) {
            return;
        }
        AttributeModifier modifier = new AttributeModifier(key, 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
        if (itemMeta.hasAttributeModifiers() && itemMeta.getAttributeModifiers(Attribute.GENERIC_LUCK).contains(modifier)) {
            return;
        }

        itemMeta.addAttributeModifier(Attribute.GENERIC_LUCK, new AttributeModifier(key, 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    public static void hideExtraTooltip(ItemMeta itemMeta) {
        hideAttributes(itemMeta);
        itemMeta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_STORED_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE
        );
    }

}
