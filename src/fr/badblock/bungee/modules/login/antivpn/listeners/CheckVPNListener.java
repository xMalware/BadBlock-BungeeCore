package fr.badblock.bungee.modules.login.antivpn.listeners;

import java.net.UnknownHostException;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.antivpn.AntiVPN;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * Check VPN listener
 * 
 * @author xMalware
 *
 */
public class CheckVPNListener extends BadListener {

	@EventHandler(priority = EventPriority.LOW)
	/**
	 * When the player joins the server
	 * 
	 * @param event
	 */
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		// Get the connection
		Connection connection = event.getPreLoginEvent().getConnection();

		// Get the IP
		String ip = connection.getAddress().getAddress().getHostAddress();
		// Get the AntiVPN instance
		AntiVPN antiVpn = AntiVPN.getInstance();
		// Check the IP
		try {
			antiVpn.work(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}