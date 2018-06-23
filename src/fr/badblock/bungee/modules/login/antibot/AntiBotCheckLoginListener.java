package fr.badblock.bungee.modules.login.antibot;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.antibot.checkers.AntiBotChecker;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class AntiBotCheckLoginListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPreLogin(PreLoginEvent event) {
		PendingConnection pendingConnection = event.getConnection();

		if (pendingConnection == null) {
			return;
		}

		InetSocketAddress inetSocketAddress = pendingConnection.getAddress();

		if (inetSocketAddress == null) {
			return;
		}

		InetAddress inetAddress = inetSocketAddress.getAddress();

		if (inetAddress == null) {
			return;
		}

		String address = inetAddress.getHostAddress();
		String username = pendingConnection.getName();

		if (AntiBotData.blockedAddresses.containsKey(address)) {
			if (AntiBotData.blockedAddresses.get(address) > TimeUtils.time()) {
				event.setCancelled(true);
				event.setCancelReason(I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blocked", null));
				return;
			}
		} else if (AntiBotData.blockedUsernames.containsKey(username)) {
			if (AntiBotData.blockedUsernames.get(username) > TimeUtils.time()) {
				event.setCancelled(true);
				event.setCancelReason(I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blocked", null));
				return;
			}
		}

		for (AntiBotChecker checker : AntiBotData.checkers) {
			if (!checker.accept(username, address)) {
				AntiBotData.blockedUsernames.put(username, System.currentTimeMillis() + 300_000L);
				AntiBotData.blockedAddresses.put(address, System.currentTimeMillis() + 300_000L);
				event.setCancelled(true);
				event.setCancelReason(
						I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blockeddetails", null, checker.getId()));
				BadBungee.log("§c[AntiBot] Rejected " + username + " from " + address + ".");
				return;
			}
		}

		BadBungee.log("§a[AntiBot] Accepted " + username + " from " + address + ".");
	}

}