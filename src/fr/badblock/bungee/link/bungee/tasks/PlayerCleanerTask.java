package fr.badblock.bungee.link.bungee.tasks;

import java.util.Iterator;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.flags.GlobalFlags;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerCleanerTask extends Thread {

	public PlayerCleanerTask() {
		start();
	}

	@Override
	public void run() {
		while (true) {
			int removed = 0;
			Iterator<BadPlayer> iterator = BadPlayer.getPlayers().iterator();
			BungeeCord bungeeCord = BungeeCord.getInstance();
			while (iterator.hasNext()) {
				BadPlayer badPlayer = iterator.next();
				ProxiedPlayer proxiedPlayer = bungeeCord.getPlayer(badPlayer.getName());

				if (proxiedPlayer == null) {
					if (!GlobalFlags.has(badPlayer.getName().toLowerCase() + "_margin") && badPlayer.getLoginTimestamp() + 10000 > System.currentTimeMillis()) {
						iterator.remove();
						removed++;
					}
				}
				else
				{
					badPlayer.setPing(proxiedPlayer.getPing());
				}
			}

			if (removed > 0) {
				BungeeManager.getInstance().log("§e[BadBungee] Cleaned " + removed + " players. Bungee name: "
						+ BungeeTask.bungeeObject.getName());
			}
			TimeUtils.sleepInSeconds(1);
		}
	}

}
