package fr.badblock.bungee.modules.commands.nick;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class NickConnectListener extends BadListener {

	/**
	 * When someone connects to a server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnected(ServerConnectEvent event) {
		// We get the player
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		UserConnection userConnection = (UserConnection) proxiedPlayer;

		if (badPlayer.getNickname() != null && !badPlayer.getNickname().isEmpty())
		{
			userConnection.getPendingConnection().getLoginRequest().setData(badPlayer.getNickname());
		}
		else
		{
			userConnection.getPendingConnection().getLoginRequest().setData(proxiedPlayer.getName());
		}
	}

}