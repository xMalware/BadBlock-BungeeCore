package fr.badblock.bungee._plugins.listeners.permissions;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to link BungeeCord permissions to the network permission system.
 * 
 * @author xMalware
 *
 */
public class PermissionListener extends BadListener
{

	/**
	 * When a permission is checked
	 * @param event
	 */
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPermissionCheck(PermissionCheckEvent event)
	{
		// If the permission checker is a player
		if (event.getSender() instanceof ProxiedPlayer)
		{
			// We get the player
			ProxiedPlayer bPlayer = (ProxiedPlayer) event.getSender();
			// We get the BadPlayer object
			BadPlayer badPlayer = BadPlayer.get(bPlayer);
			// If the BadPlayer object does not exist
			if (badPlayer == null)
			{
				// We stop there
				return;
			}
			// Link permission to system
			event.setHasPermission(badPlayer.hasPermission(event.getPermission()));
		}
	}

}
