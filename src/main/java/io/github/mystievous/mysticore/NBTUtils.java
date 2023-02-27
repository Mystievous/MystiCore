package io.github.mystievous.mysticore;

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NBTUtils implements Listener {

    public static final String TEAM = "item_team";
    public static final String UNIQUE_ID = "unique_id";
    public static final String NO_STACK = "no_stack";

    private static byte boolToByte(boolean value) {
        return (byte) (value ? 1 : 0);
    }

    private static boolean byteToBool(byte value) {
        return value == (byte) 1;
    }

    public static ItemStack noStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return itemStack;
        NamespacedKey key = NamespacedKey.fromString(NO_STACK);
        if (key == null)
            return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, new UUIDDataType(), UUID.randomUUID());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack setUniqueID(String tag, ItemStack itemStack, UUID uuid) {
        if (itemStack == null || itemStack.getType().isAir())
            return itemStack;
        NamespacedKey key = NamespacedKey.fromString(tag);
        if (key == null)
            return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (uuid != null) {
            container.set(key, new UUIDDataType(), uuid);
        } else {
            container.remove(key);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack setUniqueID(ItemStack itemStack, UUID uuid) {
        return setUniqueID(UNIQUE_ID, itemStack, uuid);
    }

    public static boolean hasUniqueID(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return false;
        NamespacedKey key = NamespacedKey.fromString(UNIQUE_ID);
        if (key == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(key);
    }

    public static @Nullable UUID getUniqueID(String tag, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return null;
        NamespacedKey key = NamespacedKey.fromString(tag);
        if (key == null)
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.get(key, new UUIDDataType());
    }

    public static UUID getUniqueID(ItemStack itemStack) {
        return getUniqueID(UNIQUE_ID, itemStack);
    }

    /**
     * Sets and item's tag to the specified state
     * @param tag tag to set
     * @param itemStack item to set tag on
     * @param tagState state to set tag to
     * @return the item with the tag changed
     */
    public static ItemStack setBool(String tag, ItemStack itemStack, boolean tagState) {
        if (itemStack == null || itemStack.getType().isAir())
            return itemStack;
        NamespacedKey key = NamespacedKey.fromString(tag);
        if (key == null)
            return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.BYTE, boolToByte(tagState));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Sets and item's tag to true
     * @param tag tag to set
     * @param itemStack item to set tag on
     * @return the item with the tag changed
     */
    public static ItemStack setBool(String tag, ItemStack itemStack) {
        return setBool(tag, itemStack, true);
    }

    /**
     * Checks if a certain tag is true on an item
     * @param tag the tag to check
     * @param itemStack the item to check the tag on
     * @return the value of the boolean tag
     */
    public static boolean boolState(String tag, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return false;
        NamespacedKey key = NamespacedKey.fromString(tag);
        if (key == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return byteToBool(container.getOrDefault(key, PersistentDataType.BYTE, (byte) 0));
    }

    public static ItemStack setString(String tag, ItemStack itemStack, String string) {
        if (itemStack == null || itemStack.getType().isAir())
            return itemStack;
        NamespacedKey key = NamespacedKey.fromString(tag);
        if (key == null)
            return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, string);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static @Nullable String getString(String tag, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return null;
        NamespacedKey key = NamespacedKey.fromString(tag);
        if (key == null)
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.get(key, PersistentDataType.STRING);
    }

    public static final String NO_USE_TAG = "no-use";

    public static ItemStack setNoUse(ItemStack itemStack) {
        return setBool(NO_USE_TAG, itemStack);
    }

    public static boolean isNoUse(ItemStack itemStack) {
        return boolState(NO_USE_TAG, itemStack);
    }

    @EventHandler
    public void onCraft(final CraftItemEvent event) {
        if (event.isCancelled())
            return;
        CraftingInventory inventory = event.getInventory();
        for (ItemStack item : inventory.getMatrix()) {
            if (isNoUse(item)) {
                event.getWhoClicked().sendMessage(Component.text("You can't craft with that!"));
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerShoot(final EntityShootBowEvent event) {
        ItemStack item = event.getConsumable();
        if (isNoUse(item)) {
            event.getEntity().sendMessage(TextUtil.formatText("The arrow falls out of your bow as you try to shoot.").decoration(TextDecoration.ITALIC, true));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrossbowLoad(final EntityLoadCrossbowEvent event) {
        if (event.getEntity() instanceof InventoryHolder inventoryHolder) {
            for (ItemStack itemStack : inventoryHolder.getInventory().getContents()) {
                if (isNoUse(itemStack)) {
                    event.getEntity().sendMessage(TextUtil.formatText("The arrow falls out of your crossbow as you try to load it.").decoration(TextDecoration.ITALIC, true));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

}
