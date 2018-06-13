package fr.badblock.bungee.modules.ping;

import fr.badblock.api.common.utils.i18n.I18n;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to manage the server motd
 * 
 * @author xMalware
 * 
 */
public class ProxyPingListener extends BadListener {

	/**
	 * When someone ping the server
	 * 
	 * @param event
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onProxyPing(ProxyPingEvent event) {
		// We get the bungee manager
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// Generate a ping
		ServerPing serverPing = bungeeManager.generatePing();
		// If the server ping is null
		if (serverPing == null) {
			// We set a basic response
			serverPing = event.getResponse();
			serverPing.getPlayers().setMax(0);
			serverPing.getPlayers().setOnline(0);
			serverPing.setDescription(I18n.getInstance().get("bungee.errors.motd")[0]);
		}
		// We send him the response generated by our network synchronization system
		event.setResponse(serverPing);
	}

}
