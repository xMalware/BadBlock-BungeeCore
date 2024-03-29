package fr.badblock.bungee.link.listeners;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitRequestListener;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeLocalManager;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;

/**
 * 
 * @author xMalware
 *
 */
public class PlayerDataRequester extends RabbitRequestListener {

	/**
	 * Constructor
	 */
	public PlayerDataRequester() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_DATA_PLAYERS_RPC, false);
		// Load the listener
		load();
	}

	/**
	 * When we receive a packet
	 */
	@Override
	public String reply(String playerName) {
		// Get the local manager
		BungeeLocalManager bungeeLocalManager = BungeeLocalManager.getInstance();

		// If the player isn't online
		if (!bungeeLocalManager.isOnline(playerName)) {
			// Get offline player
			BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);
			if (badOfflinePlayer.getDbObject() == null) {
				return null;
			}
			// Get data
			return badOfflinePlayer.getDbObject().toJson();
		}

		// Get the BadPlayer
		BadPlayer badPlayer = bungeeLocalManager.getPlayer(playerName);

		// If the BadPlayer is null
		if (badPlayer == null) {
			// Get offline player
			BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);
			if (badOfflinePlayer.getDbObject() == null) {
				return null;
			}
			// Get data
			return badOfflinePlayer.getDbObject().toJson();
		}

		if (badPlayer.getDbObject() == null) {
			return null;
		}


		return badPlayer.getDbObject().toJson();
	}

}
