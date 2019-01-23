package fr.badblock.bungee.packets.packets.protocol.game.server;

import fr.badblock.bungee.packets.enums.ClickType;
import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.packets.Packet;
import fr.badblock.bungee.packets.util.BufferUtils;
import fr.badblock.bungee.packets.window.Window;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * A packet to notify the server the player clicked on a window (component)
 *
 * @see <a href="http://wiki.vg/Protocol#Click_Window">http://wiki.vg/Protocol#Click_Window</a>
 */
public class ClickWindowPacket extends Packet {

    /**
     * The clicked window
     */
    @Getter @Setter private Window window;

    /**
     * The clicked window's ID
     */
    @Getter @Setter private byte windowID;

    /**
     * The clicked slot ID
     */
    @Getter @Setter private short slot;

    /**
     * The ID of the action
     */
    @Getter @Setter private short actionNumber;

    /**
     * The clicked item
     */
    @Getter @Setter private ItemStack clickedItem;

    /**
     * The click type
     */
    @Getter @Setter private ClickType clickType;

    @Override
    public void read(ByteBuf buf) {
        windowID = (byte) buf.readUnsignedByte();
        slot = buf.readShort();
        byte button = buf.readByte();
        actionNumber = buf.readShort();
        byte mode = buf.readByte();
        clickedItem = BufferUtils.readItemStack(buf);
        clickType = ClickType.of(mode, button, slot);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(windowID);
        buf.writeShort(slot);
        buf.writeByte(clickType.getButton());
        buf.writeShort(actionNumber);
        buf.writeByte(clickType.getMode());
        BufferUtils.writeItemStack(buf, clickedItem);
    }

}
