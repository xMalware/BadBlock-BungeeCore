package fr.badblock.bungee.packets.event;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.util.Optional;

import fr.badblock.bungee.packets.enums.ClickType;
import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.window.Window;

/**
 * An event called when interacting with a certain window
 */
public class InteractWindowEvent extends Event {

    /**
     * The clicked window
     */
    @Getter private final Window window;

    /**
     * The click type
     */
    @Getter private final ClickType clickType;

    /**
     * The clicked slot
     */
    @Getter private final short slot;

    /**
     * The player who clicked
     */
    @Getter private final ProxiedPlayer player;

    public InteractWindowEvent(Window window, ProxiedPlayer player, ClickType click, short slot) {
        this.window = window;
        this.player = player;
        this.clickType = click;
        this.slot = slot;
    }

    /**
     * Retrieves the clicked item
     * @return An empty optional if there was no item clicked, otherwise an optional containing the item
     */
    public Optional<ItemStack> getClickedItem() {
        ItemStack stack = window.get(slot);

        if (stack == null || stack.equals(ItemStack.EMPTY()))
            return Optional.empty();
        return Optional.of(stack);
    }

}
