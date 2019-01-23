package fr.badblock.bungee.link.processing.bungee;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * 
 * Broadcast BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeBroadcastComponentProcessing extends _BungeeProcessing {

	@Override
	/**
	 * Bungee message processing
	 * 
	 * @param Messages
	 *            to broadcast
	 */
	public void done(String message) {
		BaseComponent[] baseComponents = ComponentSerializer.parse(message);
		ProxyServer.getInstance().broadcast(baseComponents);
	}

	@Override
	public BungeePacketType getPacketType() {
		return BungeePacketType.BROADCAST_COMPONENT;
	}

}
