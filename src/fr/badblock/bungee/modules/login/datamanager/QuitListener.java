package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.link.bungee.BungeeTask;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to remove the player when he disconnects and 
 * to resynchronize with all the proxies.
 * 
 * @author xMalware
 *
 */
public class QuitListener extends BadListener
{

	/**
	 * When the player disconnects from the server
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerDisconnectEvent event)
	{
		// We get the player
		ProxiedPlayer player = event.getPlayer();
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(player);
		
		// If the BadPlayer object is null
		if (badPlayer == null)
		{
			// So we stop there
			return;
		}
		
		// We remove the BadPlayer object
		badPlayer.remove();
		
		// We synchronize all the other proxies
		BungeeTask.keepAlive();
	}
	
}
