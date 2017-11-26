package fr.badblock.bungee.listeners;

import fr.badblock.bungee.listeners.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.event.EventHandler;

public class PermissionListener extends BadListener
{

	@EventHandler
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
			System.out.println(bPlayer.getName() + "(" + event.getPermission() + "): " + badPlayer.hasPermission(event.getPermission())); 
			event.setHasPermission(badPlayer.hasPermission(event.getPermission()));
		}
	}

}
