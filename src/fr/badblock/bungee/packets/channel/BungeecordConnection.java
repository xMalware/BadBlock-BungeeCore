package fr.badblock.bungee.packets.channel;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.Connection;

/**
 * A connection representing the Bungeecord connection
 */
public class BungeecordConnection implements Connection {

    /**
     * The channel of the connection
     */
    @SuppressWarnings("unused")
	private final Channel channel;

    /**
     * Creates a connection representing Bungeecord
     * @param ctx The ChannelHandlerContext of the Bungeecord server
     */
    public BungeecordConnection(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }

    @Override
    public void disconnect(String reason) {

    }

    @Override
    public void disconnect(BaseComponent... reason) {

    }

    @Override
    public void disconnect(BaseComponent reason) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public Unsafe unsafe() {
        return null;
    }

}
