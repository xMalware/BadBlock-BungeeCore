package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerCreatedEvent;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.BungeeCord;
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		System.out.println(event.getBadPlayer().getName() + " ICanLogYouListener: PlayerJoinEvent (cancelled: " + event.isCancelled() + ")");
		if (event.isCancelled())
		{
			BadBungee.log("§c[ERROR] Connection of " + event.getBadPlayer().getName() + " was cancelled (PlayerJoin): " + event.getPreLoginEvent().getCancelReason());
			return;
		}
	}

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncLoadData(AsyncDataLoadRequest event) {
		System.out.println(event.getPlayer() + " ICanLogYouListener: AsyncDataLoadRequest (cancelled: " + event.isCancelled() + ")");
		if (event.isCancelled()) {
			return;
		}

		String playerName = event.getPlayer().toLowerCase();

		if (BadPlayer.has(playerName)) {
			BadPlayer badPlayer = BadPlayer.get(playerName);
			event.getDone().done(new Result(badPlayer.getSavedObject(), null), null);
			BungeeCord.getInstance().getPluginManager().callEvent(new PlayerCreatedEvent(badPlayer));
			return;
		}

		BadBungee.log("§4§l[ERROR] Unable to load player data for " + playerName + " (code: CBPAQ6)");
		Result result = new Result(null,
				I19n.getFullMessage(Locale.FRENCH_FRANCE, "bungee.errors.unabletoloadplayerdata", null, "CBPAQ6"));
		event.getDone().done(result, null);
	}

}
