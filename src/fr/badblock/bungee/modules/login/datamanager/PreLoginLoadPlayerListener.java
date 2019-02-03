package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.api.common.utils.flags.GlobalFlags;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.tasks.BungeeTask;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
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
	@EventHandler(priority = EventPriority.LOW)
	public void onPreLogin(PreLoginEvent event) {
		System.out.println(event.getConnection().getName() + " PreLoginLoadPlayerListener: onPreLogin (cancelled: " + event.isCancelled() + ")");
		// If the connection attempt is cancelled first
		if (event.isCancelled()) {
			// So we stop there
			return;
		}
		
		// Set a margin flag
		GlobalFlags.set(event.getConnection().getName().toLowerCase() + "_margin", 5000);

		// We create a BadPlayer object
		new BadPlayer(event.getConnection());

		// We synchronize all the other proxies
		BungeeTask.keepAlive();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreLoginHigh(PreLoginEvent event) {
		System.out.println(event.getConnection().getName() + " PreLoginLoadPlayerListener: onPreLoginHigh (cancelled: " + event.isCancelled() + ")");
		if (event.isCancelled())
		{
			BadBungee.log("§c[ERROR] Connection of " + event.getConnection().getName() + " was  (PreLogin-Highest): " + event.getCancelReason());
			return;
		}

		// We create a BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(event.getConnection().getName());
		
		if (badPlayer == null)
		{
			event.setCancelled(true);
			BadBungee.log("§4§l[ERROR] Unable to load player data for " + event.getConnection().getName() + " (code: NVZT33)");
			event.setCancelReason(
					I19n.getFullMessage(Locale.FRENCH_FRANCE, "bungee.errors.unabletoloadplayerdata", null, "NVTZ33"));
					return;
		}

		// We send an event that says the player tried to join the server
		ProxyServer.getInstance().getPluginManager().callEvent(new PlayerJoinEvent(badPlayer, event));
	}
	
}
