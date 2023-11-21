package io.github.mystievous.mysticore.interact;

import io.github.mystievous.mysticore.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a wand that players can use to perform actions upon interaction.
 */
public class UsableItem implements Listener {

    /**
     * The label used for identifying the wand type in NBT data.
     */
    public static final String TYPE_TAG = "usable-type";

    private final String type;
    private final NamespacedKey typeKey;

    private ItemStack template;
    private Consumer<PlayerInteractEvent> useAction;
    private String permission;

    /**
     * Creates a new Wand instance with the given plugin, type, template, and interaction consumer.
     *
     * @param plugin    The plugin that owns the wand.
     * @param type       The string used to identify the wand type.
     * @param template  The template ItemStack representing the wand.
     * @param useAction The consumer that handles the interaction event.
     */
    public UsableItem(Plugin plugin, String type, ItemStack template, Consumer<PlayerInteractEvent> useAction) {
        this.type = type;
        this.typeKey = NamespacedKey.fromString(TYPE_TAG, plugin);
        this.useAction = useAction;
        this.template = template;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Gets the tag associated with this wand.
     *
     * @return The wand's tag.
     */
    public String getType() {
        return type;
    }

    public void setUseAction(Consumer<PlayerInteractEvent> useAction) {
        this.useAction = useAction;
    }

    /**
     * Sets the permission required to use this item
     *
     * @param permission The permission string
     */
    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    /**
     * Gets the ItemStack representation of this wand with the proper type tag in NBT.
     *
     * @return The ItemStack representing the wand.
     */
    public ItemStack getItem() {
        ItemStack itemStack = this.template.clone();
        if (!itemStack.hasItemMeta())
            return itemStack;
        return NBTUtils.applyToItemMeta(itemStack, itemMeta -> NBTUtils.setString(typeKey, itemMeta, type));
    }

    /**
     * Checks if the given ItemStack matches the wand's type.
     *
     * @param item The ItemStack to check.
     * @return True if the ItemStack matches the wand's type, false otherwise.
     */
    private boolean matchItem(ItemStack item) {
        if (!item.hasItemMeta())
            return false;
        String itemType = NBTUtils.getString(typeKey, item.getItemMeta());
        return type.equals(itemType);
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
        if (useAction != null && matchItem(item) &&
                (permission == null || player.hasPermission(permission))) {
            useAction.accept(event);
        }
    }

}
