package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
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
			BadPlayer badPlayer = BadPlayer.get(playerName);
			event.getDone().done(new Result(badPlayer.getSavedJsonObject(), null), null);
		}
		else
		{
			Result result = new Result(null, I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.errors.unabletoloadplayerdata", null));
			event.getDone().done(result, null);
		}
	}

}
