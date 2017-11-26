package fr.badblock.bungee._plugins.listeners.motd;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.listeners.abstracts.BadListener;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyPingListener extends BadListener
{

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onProxyPing(ProxyPingEvent event)
	{
		event.setResponse(BungeeManager.getInstance().generatePing());
	}

}
