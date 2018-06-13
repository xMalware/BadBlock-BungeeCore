package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.bungee.BungeeTask;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to load the player's data to the connection and
 * warn other proxies to synchronize the data. It also sends a special event
 * when the player logs in to add actions when a player tries to log in.
 * 
 * @author xMalware
 *
 */
public class PreLoginLoadPlayerListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
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

		// We create a BadPlayer object
		BadPlayer badPlayer = new BadPlayer(event.getConnection());

		// We synchronize all the other proxies
		BungeeTask.keepAlive();

		// We send an event that says the player tried to join the server
		ProxyServer.getInstance().getPluginManager().callEvent(new PlayerJoinEvent(badPlayer, event));
	}

}
