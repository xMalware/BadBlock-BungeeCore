package fr.badblock.bungee.link.bungee.tasks;

import java.util.Iterator;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerKeepAliveTask extends Thread {

	public PlayerKeepAliveTask() {
		start();
	}

	@Override
	public void run() {
		while (true) {
			try
			{
				Iterator<BadPlayer> iterator = BadPlayer.getPlayers().iterator();
				BungeeCord bungeeCord = BungeeCord.getInstance();
				while (iterator.hasNext()) {
					BadPlayer badPlayer = iterator.next();
					ProxiedPlayer proxiedPlayer = bungeeCord.getPlayer(badPlayer.getName());

					if (proxiedPlayer != null)
					{
						badPlayer.keepAlive();
					}
				}
			}
			catch (Exception error)
			{
				error.printStackTrace();
			}
			
			TimeUtils.sleepInSeconds(60);
		}
	}

}
