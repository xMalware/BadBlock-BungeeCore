package fr.badblock.bungee.packets.listener;

import static net.md_5.bungee.event.EventPriority.HIGHEST;

import fr.badblock.bungee.packets.pipeline.BungeePacketsDecoder;
import fr.badblock.bungee.packets.pipeline.BungeePacketsEncoder;
import fr.badblock.bungee.packets.util.PlayerUtils;
import fr.badblock.bungee.packets.window.WindowManager;
import io.netty.channel.Channel;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.netty.PipelineUtils;

/**
 * Listens to incoming connections and server switches to inject the encoder and decoder in the pipeline
 */
public class ConnectionListener implements Listener {

    @EventHandler(priority = HIGHEST)
    public void onServerConnected(ServerConnectedEvent evt) {
        Channel serverConnection = ((ServerConnection) evt.getServer()).getCh().getHandle();
        injectListenersInPipeline(evt.getPlayer(), serverConnection, true);

        WindowManager.instance.closeWindow(evt.getPlayer().getUniqueId());
    }

    @EventHandler(priority = HIGHEST)
    public void onPostLogin(PostLoginEvent evt) {
        try {
            Channel ch = PlayerUtils.getPlayerChannel(evt.getPlayer());
            injectListenersInPipeline(evt.getPlayer(), ch, false);
        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent evt) {
        WindowManager.instance.closeWindow(evt.getPlayer().getUniqueId());
    }

    /**
     * Injects the listeners in the pipeline of the provided player
     * @param player The player of the connection
     * @param channel The channel whose pipeline to inject into
     * @param connectedToServer Whether if the player is connected to a server or not
     */
    public static void injectListenersInPipeline(ProxiedPlayer player, Channel channel, boolean connectedToServer) {
        channel.pipeline().addAfter(PipelineUtils.PACKET_DECODER, "packet-decoder-listener", new BungeePacketsDecoder(player, connectedToServer));
        channel.pipeline().addAfter(PipelineUtils.PACKET_ENCODER, "packet-encoder-listener", new BungeePacketsEncoder(player, connectedToServer));
    }

}
