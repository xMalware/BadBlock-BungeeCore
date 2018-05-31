package fr.badblock.bungee.modules.login.antivpn.listeners;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.antivpn.AntiVPN;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.utils.time.ThreadRunnable;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class CheckVPNListener extends BadListener
{

	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		ThreadRunnable.run(() -> 
		{
			Connection connection = event.getPreLoginEvent().getConnection();
			String ip = connection.getAddress().getAddress().getHostAddress();
			AntiVPN antiVpn = AntiVPN.getInstance();
			antiVpn.addToCheck(ip);
		});
	}

}