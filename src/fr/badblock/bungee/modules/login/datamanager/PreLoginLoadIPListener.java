package fr.badblock.bungee.modules.login.datamanager;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadIP;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class PreLoginLoadIPListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPreLogin(PreLoginEvent event) {
		// If the connection attempt is cancelled first
		if (event.isCancelled()) {
			// So we stop there
			return;
		}

		// Get bungee manager
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// If the server ping null
		if (bungeeManager.getServerPing() == null) {
			// Generate the server ping to init slots/players values
			// to avoid "this server is fucking full!"
			bungeeManager.generatePing();
		}

		// Get pending connection
		PendingConnection pendingConnection = event.getConnection();

		if (pendingConnection == null)
		{
			return;
		}

		InetSocketAddress address = pendingConnection.getAddress();

		if (address == null)
		{
			return;
		}

		InetAddress inetAddress = address.getAddress();

		if (inetAddress == null)
		{
			return;
		}

		System.out.println("cc");
		// We create a BadIP object
		new BadIP(inetAddress.getHostAddress(), true);
	}

}
