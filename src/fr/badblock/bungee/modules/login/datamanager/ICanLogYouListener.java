package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.event.AsyncDataLoadRequest;
import net.md_5.bungee.api.event.AsyncDataLoadRequest.Result;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class ICanLogYouListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncLoadData(AsyncDataLoadRequest event) {
		if (event.isCancelled())
		{
			return;
		}
		
		String playerName = event.getPlayer();
		
		System.out.println("AsyncDataLoadRequest CALL!");
		if (BadPlayer.has(playerName))
		{
			event.getDone().done(new Result(null, "§aBadPlayer OK"), null);
		}
		else
		{
			event.getDone().done(new Result(null, "§cBadPlayer NO-OK"), null);
		}
	}

}
