package fr.badblock.bungee._plugins.listeners.permissions;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PermissionListener extends BadListener
{

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPermissionCheck(PermissionCheckEvent event)
	{
		if (event.getSender() instanceof ProxiedPlayer)
		{
			ProxiedPlayer bPlayer = (ProxiedPlayer) event.getSender();
			BadPlayer badPlayer = BadPlayer.get(bPlayer);
			if (badPlayer == null)
			{
				return;
			}
			event.setHasPermission(badPlayer.hasPermission(event.getPermission()));
		}
	}

}
