package fr.badblock.bungee.link.processing.bungee;

import java.net.InetSocketAddress;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.objects.ServerObject;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.bungee.BadBungee;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * 
 * Add server BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeAddServerProcessing extends _BungeeProcessing {

	@Override
	/**
	 * 
	 * Server object
	 * 
	 * @param Messages
	 *
	 */
	public void done(String message)
	{
		// Get the server object
		ServerObject serverObject = GsonUtils.getGson().fromJson(message, ServerObject.class);
		// If the server object is null
		if (serverObject == null)
		{
			// So we stop there
			return;
		}
		// Create a server info
		ServerInfo server = BungeeCord.getInstance().constructServerInfo(serverObject.getName(),
				new InetSocketAddress(serverObject.getIp(), serverObject.getPort()), serverObject.getName(), false);
		// Add the server
		BungeeCord.getInstance().getServers().put(serverObject.getName(), server);
		// Log
		BadBungee.log("Added server: " + message);
	}

	@Override
	/**
	 * Get packet type
	 */
	public BungeePacketType getPacketType() {
		// Returns the packet type
		return BungeePacketType.ADD_SERVER;
	}

}
