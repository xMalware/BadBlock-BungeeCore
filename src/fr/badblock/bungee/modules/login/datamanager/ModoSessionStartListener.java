package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.commands.modo.objects.ModoSession;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class ModoSessionStartListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (event.isCancelled())
		{
			return;
		}
		
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		
		if (!badPlayer.hasPermission("modo.sessions"))
		{
			return;
		}
		
		long time = System.currentTimeMillis();
		
		ModoSession modoSession = new ModoSession(badPlayer.getName(), badPlayer.getUniqueId().toString(), time, time, time, 0L, 0L, 0L);
		
		badPlayer.setModoSession(modoSession);
	}

}
