package fr.badblock.bungee.packets.packets.protocol.game.client;

import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.packets.Packet;
import fr.badblock.bungee.packets.util.BufferUtils;
import fr.badblock.bungee.packets.window.Window;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * Sets the contents of a window
 *
 * @see <a href="http://wiki.vg/Protocol#Window_Items">http://wiki.vg/Protocol#Window_Items</a>
 */
public class SetWindowContentsPacket extends Packet {

    /**
     * The window to send
     */
    @Getter @Setter private Window window;

    /**
     * The window ID
     */
    @Getter @Setter private byte windowID;

    public SetWindowContentsPacket() {}

    public SetWindowContentsPacket(Window window) {
        this.window = window;
    }

    @Override
    public void read(ByteBuf buf) {
        //TODO Properly implement
        windowID = (byte) buf.readUnsignedByte();
        short size = buf.readShort();
        for(int i=0; i<size; i++)
            BufferUtils.readItemStack(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(window.getId());
        buf.writeShort(window.getSlots());
        for (ItemStack stack : window.getContents()) {
            BufferUtils.writeItemStack(buf, stack);
        }
    }

}
