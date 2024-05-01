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
    @Deprecated
    public static final String UNIQUE_ID = "unique_id";

    public static final String NO_STACK = "no_stack";

    @Deprecated
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
        itemStack.editMeta(itemMeta -> {
            setUUID(key, itemMeta, UUID.randomUUID());
        });
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
        itemStack.editMeta(itemMeta -> {
            setBool(NO_USE_KEY, itemMeta);
        });
        return itemStack;
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
                event.getWhoClicked().sendMessage(Component.text("You can't craft with ").append(ItemUtil.getItemName(item)).append(Component.text(".")).color(Palette.NEGATIVE_COLOR.toTextColor()));
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerShoot(final EntityShootBowEvent event) {
        if (event.isCancelled())
            return;
        ItemStack item = event.getConsumable();
        if (item != null && isNoUse(item)) {
            event.getEntity().sendMessage(TextUtil.formatText("The arrow falls out of your bow as you try to shoot.").decoration(TextDecoration.ITALIC, true));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrossbowLoad(final EntityLoadCrossbowEvent event) {
        if (event.isCancelled())
            return;
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
        if (event.isCancelled())
            return;
        ItemStack item = event.getItem();
        if (isNoUse(item)) {
            event.getPlayer().sendMessage(TextUtil.formatText("You can't use that."));
            event.setCancelled(true);
        }
    }

}
