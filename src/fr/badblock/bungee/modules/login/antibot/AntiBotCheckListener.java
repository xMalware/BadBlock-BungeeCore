package fr.badblock.bungee.modules.login.antibot;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.antibot.checkers.ASNChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.AntiBotChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.ForeignCountryChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.LastConnectionChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.TooManyAccountsMemoryChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.UsernameLengthChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.UsernameSyllablesChecker;
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

	public static AntiBotChecker[] checkers		= new AntiBotChecker[] {
			new ASNChecker(),
			new ForeignCountryChecker(),
			new LastConnectionChecker(),
			new TooManyAccountsMemoryChecker(),
			new UsernameLengthChecker(),
			new UsernameSyllablesChecker()
	};

	private Map<String, Long> blockedAddresses	= new HashMap<>();
	private Map<String, Long> blockedUsernames	= new HashMap<>();

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
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
		
		if (blockedAddresses.containsKey(address))
		{
			if (blockedAddresses.get(address) > TimeUtils.time())
			{
				event.setCancelled(true);
				event.setCancelReason(I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blocked", null));
				return;
			}
		}
		else if (blockedUsernames.containsKey(username))
		{
			if (blockedUsernames.get(username) > TimeUtils.time())
			{
				event.setCancelled(true);
				event.setCancelReason(I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blocked", null));
				return;
			}
		}

		for (AntiBotChecker checker : checkers)
		{
			if (!checker.accept(username, address))
			{
				blockedUsernames.put(username, System.currentTimeMillis() + 300_000L);
				blockedAddresses.put(address, System.currentTimeMillis() + 300_000L);
				event.setCancelled(true);
				event.setCancelReason(I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blocked", null));
				break;
			}
		}
	}

}