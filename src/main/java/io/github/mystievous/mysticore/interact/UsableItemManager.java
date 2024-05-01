package io.github.mystievous.mysticore.interact;

import io.github.mystievous.mysticore.MystiCore;
import io.github.mystievous.mysticore.NBTUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents an item that players can use to perform actions upon interaction.
 */
public class UsableItemManager implements Listener {

    /**
     * The label used for identifying the wand type in NBT data.
     */
    public static final String TYPE_TAG = "usable-type";
    public static final NamespacedKey TYPE_KEY = NamespacedKey.fromString(TYPE_TAG, MystiCore.getInstance());

    private final static Map<String, Consumer<PlayerInteractEvent>> actions = new HashMap<>();
    private final static Map<String, String> permissions = new HashMap<>();

    public static UsableItem createItem(String type, ItemStack template, Consumer<PlayerInteractEvent> onUse) {
        return new UsableItem(type, template, onUse);
    }

    /**
     * Handles the PlayerInteractEvent and invokes the consumer if applicable.
     *
     * @param event The PlayerInteractEvent.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (event.getAction() == Action.PHYSICAL
                || event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK
                || item == null)
            return;
        Player player = event.getPlayer();
        String type = NBTUtils.getString(TYPE_KEY, item.getItemMeta());
        var action = actions.get(type);
        if (action != null && (!permissions.containsKey(type) || player.hasPermission(permissions.get(type)))) {
            action.accept(event);
        }
    }

    public static class UsableItem implements Listener {

        private final String type;
        private final ItemStack template;

        /**
         * Creates a new Wand instance with the given plugin, type, template, and interaction consumer.
         *
         * @param type     The string used to identify the wand type.
         * @param template The template ItemStack representing the wand.
         * @param onUse    The consumer that handles the interaction event.
         */
        private UsableItem(String type, ItemStack template, Consumer<PlayerInteractEvent> onUse) {
            this.type = type;
            this.template = template;
            setOnUse(onUse);
        }

        public void setOnUse(Consumer<PlayerInteractEvent> onUse) {
            if (actions.containsKey(type)) {
                MystiCore.getPluginLogger().warn(Component.text("Usable Item Action [" + type + "] is already defined."));
            }
            actions.put(type, onUse);
        }

        /**
         * Sets the permission required to use this item
         *
         * @param permission The permission string
         */
        public void setPermission(@Nullable String permission) {
            permissions.put(type, permission);
        }

        /**
         * Gets the ItemStack representation of this wand with the proper type tag in NBT.
         *
         * @return The ItemStack representing the wand.
         */
        public ItemStack getItem() {
            ItemStack itemStack = this.template.clone();
            itemStack.editMeta(itemMeta -> {
                NBTUtils.setString(TYPE_KEY, itemMeta, type);
            });
            return itemStack;
        }

    }

}
