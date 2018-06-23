package fr.badblock.bungee.modules.login.antibot;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.antibot.checkers.AntiBotChecker;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class AntiBotCheckPingListener extends BadListener {

	private static ServerPing bannedPing = new ServerPing(new Protocol("Banni", 0), new Players(0, 0, null),
			I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.motd", null), "");

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onProxyPing(ProxyPingEvent event) {
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
				event.setResponse(bannedPing);
				return;
			}
		} else if (AntiBotData.blockedUsernames.containsKey(username)) {
			if (AntiBotData.blockedUsernames.get(username) > TimeUtils.time()) {
				event.setResponse(bannedPing);
				return;
			}
		}

		for (AntiBotChecker checker : AntiBotData.checkers) {
			if (!checker.accept(username, address)) {
				AntiBotData.reject(address, username);
				event.setResponse(bannedPing);
				BadBungee.log("§c[AntiBot] Rejected " + username + " from " + address + ".");
				return;
			}
		}

	}

}