package fr.badblock.bungee.modules.login.antibot;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import fr.badblock.api.common.utils.i18n.Locale;
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
public class AntiBotCheckListener extends BadListener
{

	public static AntiBotChecker[] checkers	= new AntiBotChecker[] {};

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPreLogin(PreLoginEvent event)
	{
		PendingConnection pendingConnection = event.getConnection();

		if (pendingConnection == null)
		{
			return;
		}

		InetSocketAddress inetSocketAddress = pendingConnection.getAddress();

		if (inetSocketAddress == null)
		{
			return;
		}

		InetAddress inetAddress = inetSocketAddress.getAddress();

		if (inetAddress == null)
		{
			return;
		}

		String address = inetAddress.getHostAddress();
		String username = pendingConnection.getName();

		for (AntiBotChecker checker : checkers)
		{
			if (!checker.accept(username, address))
			{
				event.setCancelled(true);
				event.setCancelReason(I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blocked", null));
				break;
			}
		}
	}

}