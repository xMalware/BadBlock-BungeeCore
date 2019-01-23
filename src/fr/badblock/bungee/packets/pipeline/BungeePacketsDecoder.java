package fr.badblock.bungee.packets.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.PacketWrapper;

import java.util.List;

import fr.badblock.bungee.packets.channel.BungeecordConnection;
import fr.badblock.bungee.packets.event.PacketEvent;

/**
 * Handles packets in the decoder channel to redirect all packets to the event handlers
 */
public class BungeePacketsDecoder extends MessageToMessageDecoder<PacketWrapper> {

    /**
     * The player associated with the decoder
     */
    private ProxiedPlayer player;

    /**
     * Whether the player is connected to a server
     */
    private boolean connectedToServer;

    public BungeePacketsDecoder(ProxiedPlayer player, boolean connectedToServer) {
        this.player = player;
        this.connectedToServer = connectedToServer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, PacketWrapper msg, List<Object> out) {
        if (msg.packet == null) {
            out.add(msg);
            return;
        }

        Connection sender = connectedToServer ? player.getServer() : player;
        PacketEvent evt = new PacketEvent(msg.packet, player, sender, new BungeecordConnection(ctx));

        ProxyServer.getInstance().getPluginManager().callEvent(evt);
        if (!evt.isCancelled()) {
            out.add(msg);
        }
    }

}
