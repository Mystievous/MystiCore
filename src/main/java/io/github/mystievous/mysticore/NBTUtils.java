package io.github.mystievous.mysticore;

import com.jeff_media.morepersistentdatatypes.DataType;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class NBTUtils implements Listener {

    /**
     * Key name for the UUID in the persistent data
     */
    public static final String UNIQUE_ID = "unique_id";

    public static final String NO_STACK = "no_stack";

    public static ItemStack applyToItemMeta(ItemStack itemStack, Function<ItemMeta, ItemMeta> consumer) {
        if (itemStack == null || !itemStack.hasItemMeta())
            return itemStack;
        ItemMeta meta = consumer.apply(itemStack.getItemMeta());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Sets an item to not stack with others,
     * using a generated UUID tag.
     *
     * @param itemStack The item to set.
     * @return The same item.
     */
    public static ItemStack noStack(ItemStack itemStack) {
        NamespacedKey key = MYSTICORE_KEY(NO_STACK);
        applyToItemMeta(itemStack, (meta) -> setUUID(key, meta, UUID.randomUUID()));
        return itemStack;
    }

    public static <T extends PersistentDataHolder> T setUUID(NamespacedKey key, T dataHolder, UUID uuid) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        if (uuid != null) {
            container.set(key, DataType.UUID, uuid);
        } else {
            container.remove(key);
        }
        return dataHolder;
    }

    public static @Nullable <T extends PersistentDataHolder> UUID getUUID(NamespacedKey key, T dataHolder) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.get(key, DataType.UUID);
    }

    public static <T extends PersistentDataHolder> T setUUIDSet(NamespacedKey key, T dataHolder, @Nullable Set<UUID> uuids) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        if (uuids != null) {
            container.set(key, DataType.asSet(DataType.UUID), uuids);
        } else {
            container.remove(key);
        }
        return dataHolder;
    }

    public static <T extends PersistentDataHolder> Set<UUID> getUUIDSet(NamespacedKey key, T dataHolder) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.get(key, DataType.asSet(DataType.UUID));
    }

    public static <T extends PersistentDataHolder> T setLocation(NamespacedKey key, T dataHolder, Location location) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        container.set(key, DataType.LOCATION, location);
        return dataHolder;
    }

    public static <T extends PersistentDataHolder> Location getLocation(NamespacedKey key, T dataHolder) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.get(key, DataType.LOCATION);
    }

    public static <T extends PersistentDataHolder> T setBool(NamespacedKey key, T dataHolder, boolean tagState) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        container.set(key, PersistentDataType.BOOLEAN, tagState);
        return dataHolder;
    }

    public static <T extends PersistentDataHolder> T setBool(NamespacedKey key, T dataHolder) {
        return setBool(key, dataHolder, true);
    }

    public static <T extends PersistentDataHolder> boolean getBool(NamespacedKey key, T dataHolder) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    public static <T extends PersistentDataHolder> T setString(NamespacedKey key, T dataHolder, String string) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, string);
        return dataHolder;
    }

    public static @Nullable <T extends PersistentDataHolder> String getString(NamespacedKey key, T dataHolder) {
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.get(key, PersistentDataType.STRING);
    }

    public static <T extends PersistentDataHolder> boolean hasTag(NamespacedKey key, T dataholder) {
        PersistentDataContainer container = dataholder.getPersistentDataContainer();
        return container.has(key);
    }

    public static NamespacedKey MYSTICORE_KEY(String tag) {
        return NamespacedKey.fromString(tag, MystiCore.getInstance());
    }

    public static final String NO_USE_TAG = "no-use";
    public static final NamespacedKey NO_USE_KEY = MYSTICORE_KEY(NO_USE_TAG);

    public static ItemStack setNoUse(ItemStack itemStack) {
        return applyToItemMeta(itemStack, itemMeta -> setBool(NO_USE_KEY, itemMeta));
    }

    public static ItemMeta setNoUse(ItemMeta itemMeta) {
        return setBool(NO_USE_KEY, itemMeta, true);
    }

    public static boolean isNoUse(ItemStack itemStack) {
        if (!itemStack.hasItemMeta())
            return false;
        return getBool(NO_USE_KEY, itemStack.getItemMeta());
    }

    @EventHandler
    public void onCraft(final CraftItemEvent event) {
        if (event.isCancelled())
            return;
        CraftingInventory inventory = event.getInventory();
        for (ItemStack item : inventory.getMatrix()) {
            if (item != null && isNoUse(item)) {
                event.getWhoClicked().sendMessage(Component.text("You can't craft with ").append(TextUtil.getItemName(item)).append(Component.text(".")).color(Palette.NEGATIVE_COLOR.toTextColor()));
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerShoot(final EntityShootBowEvent event) {
        ItemStack item = event.getConsumable();
        if (item != null && isNoUse(item)) {
            event.getEntity().sendMessage(TextUtil.formatText("The arrow falls out of your bow as you try to shoot.").decoration(TextDecoration.ITALIC, true));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrossbowLoad(final EntityLoadCrossbowEvent event) {
        if (event.getEntity() instanceof InventoryHolder inventoryHolder) {
            for (ItemStack itemStack : inventoryHolder.getInventory().getContents()) {
                if (itemStack != null && isNoUse(itemStack)) {
                    event.getEntity().sendMessage(TextUtil.formatText("The arrow falls out of your crossbow as you try to load it.").decoration(TextDecoration.ITALIC, true));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPotionDrink(final PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (isNoUse(item)) {
            event.getPlayer().sendMessage(TextUtil.formatText("You can't use that."));
            event.setCancelled(true);
        }
    }

    /**
     * Converts a boolean into a byte value
     *
     * @param value The boolean to convert
     * @return The byte equivalent
     */
    @Deprecated
    private static byte boolToByte(boolean value) {
        return (byte) (value ? 1 : 0);
    }

    /**
     * Converts a byte value into a boolean
     *
     * @param value The byte to convert
     * @return The boolean equivalent
     */
    @Deprecated
    private static boolean byteToBool(byte value) {
        return value == (byte) 1;
    }

    /**
     * Sets an item to not stack with others,
     * using a generated UUID tag.
     *
     * @param plugin    The plugin to use for the data.
     * @param itemStack The item to set.
     * @return The same item.
     */
    @Deprecated
    public static ItemStack noStack(Plugin plugin, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return itemStack;
        NamespacedKey key = NamespacedKey.fromString(NO_STACK, plugin);
        if (key == null)
            return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, DataType.UUID, UUID.randomUUID());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Deprecated
    public static <T extends PersistentDataHolder> T setUniqueID(Plugin plugin, String tag, T dataHolder, UUID uuid) {
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return dataHolder;
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        if (uuid != null) {
            container.set(key, DataType.UUID, uuid);
        } else {
            container.remove(key);
        }
        return dataHolder;
    }

    @Deprecated
    public static ItemStack setUniqueID(Plugin plugin, String tag, ItemStack itemStack, UUID uuid) {
        itemStack.setItemMeta(setUniqueID(plugin, tag, itemStack.getItemMeta(), uuid));
        return itemStack;
    }

    @Deprecated
    public static ItemStack setUniqueID(Plugin plugin, ItemStack itemStack, UUID uuid) {
        return setUniqueID(plugin, UNIQUE_ID, itemStack, uuid);
    }

    @Deprecated
    public static <T extends PersistentDataHolder> boolean hasUniqueID(Plugin plugin, T dataHolder) {
        NamespacedKey key = NamespacedKey.fromString(UNIQUE_ID, plugin);
        if (key == null)
            return false;
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.has(key);
    }

    @Deprecated
    public static boolean hasUniqueID(Plugin plugin, ItemStack itemStack) {
        return hasUniqueID(plugin, itemStack.getItemMeta());
    }

    @Deprecated
    public static @Nullable <T extends PersistentDataHolder> UUID getUniqueID(Plugin plugin, String tag, T dataHolder) {
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return null;
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.get(key, DataType.UUID);
    }

    @Deprecated
    public static @Nullable UUID getUniqueID(Plugin plugin, String tag, ItemStack itemStack) {
        return getUniqueID(plugin, tag, itemStack.getItemMeta());
    }

    @Deprecated
    public static @Nullable UUID getUniqueID(Plugin plugin, ItemStack itemStack) {
        return getUniqueID(plugin, UNIQUE_ID, itemStack);
    }

    @Deprecated
    public static <T extends PersistentDataHolder> T setLocation(Plugin plugin, String tag, T dataHolder, Location location) {
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return dataHolder;
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        container.set(key, DataType.LOCATION, location);
        return dataHolder;
    }

    @Deprecated
    public static <T extends PersistentDataHolder> Location getLocation(Plugin plugin, String tag, T dataHolder) {
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return null;
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return container.get(key, DataType.LOCATION);
    }

    @Deprecated
    public static <T extends PersistentDataHolder> T setBool(Plugin plugin, String tag, T dataHolder, boolean tagState) {
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return dataHolder;
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        container.set(key, PersistentDataType.BYTE, boolToByte(tagState));
        return dataHolder;
    }

    /**
     * Sets and item's tag to the specified state
     *
     * @param tag       tag to set
     * @param itemStack item to set tag on
     * @param tagState  state to set tag to
     * @return the item with the tag changed
     */
    @Deprecated
    public static ItemStack setBool(Plugin plugin, String tag, @NotNull ItemStack itemStack, boolean tagState) {
        itemStack.setItemMeta(setBool(plugin, tag, itemStack.getItemMeta(), tagState));
        return itemStack;
    }

    /**
     * Sets and item's tag to true
     *
     * @param tag       tag to set
     * @param itemStack item to set tag on
     * @return the item with the tag changed
     */
    @Deprecated
    public static ItemStack setBool(Plugin plugin, String tag, @NotNull ItemStack itemStack) {
        return setBool(plugin, tag, itemStack, true);
    }

    @Deprecated
    public static <T extends PersistentDataHolder> boolean getBool(Plugin plugin, String tag, T dataHolder) {
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return false;
        PersistentDataContainer container = dataHolder.getPersistentDataContainer();
        return byteToBool(container.getOrDefault(key, PersistentDataType.BYTE, (byte) 0));
    }

    /**
     * Checks if a certain tag is true on an item
     *
     * @param tag       the tag to check
     * @param itemStack the item to check the tag on
     * @return the value of the boolean tag
     */
    @Deprecated
    public static boolean getBool(Plugin plugin, String tag, ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            return getBool(plugin, tag, itemStack.getItemMeta());
        } else {
            return false;
        }
    }

    @Deprecated
    public static @Nullable String getString(Plugin plugin, String tag, ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta())
            return null;
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.get(key, PersistentDataType.STRING);
    }

    @Deprecated
    public static ItemStack setString(Plugin plugin, String tag, ItemStack itemStack, String string) {
        if (itemStack == null || itemStack.getType().isAir())
            return itemStack;
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, string);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Deprecated
    public static boolean hasTag(Plugin plugin, String tag, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir())
            return false;
        NamespacedKey key = NamespacedKey.fromString(tag, plugin);
        if (key == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(key);
    }

}
