package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerLoggedEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class SaveNewDataListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogged(PlayerLoggedEvent event) {
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		
		if (!badPlayer.isNew())
		{
			return;
		}

		badPlayer.insert();
		badPlayer.setNew(false);
	}

}
