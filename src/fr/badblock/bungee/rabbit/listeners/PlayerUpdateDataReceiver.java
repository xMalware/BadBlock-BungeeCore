package fr.badblock.bungee.rabbit.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeLocalManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.rabbit.datareceivers.PlayerDataUpdateReceiver;

/**
 * 
 * The purpose of this class is to listen for updates of the external player's
 * data to synchronize.
 * 
 * @author xMalware
 *
 */
public class PlayerUpdateDataReceiver extends RabbitListener {

	/**
	 * Constructor
	 */
	public PlayerUpdateDataReceiver() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_DATA_RECEIVERS_UPDATE,
				RabbitListenerType.MESSAGE_BROKER, false);
		// Load the listener
		load();
	}

	/**
	 * When we receive a packet
	 */
	@Override
	public void onPacketReceiving(String body) {
		// Get the main class
		BadBungee badBungee = BadBungee.getInstance();
		// Get gson
		Gson gson = badBungee.getGson();

		// Get the receiver object
		PlayerDataUpdateReceiver playerDataUpdateReceiver = gson.fromJson(body, PlayerDataUpdateReceiver.class);
		// Get the player name
		String playerName = playerDataUpdateReceiver.getPlayerName();

		// Get the local manager
		BungeeLocalManager bungeeLocalManager = BungeeLocalManager.getInstance();

		// If the player isn't online
		if (!bungeeLocalManager.isOnline(playerName)) {
			// Mmh.. How can we work?
			return;
		}

		// Get the BadPlayer
		BadPlayer badPlayer = bungeeLocalManager.getPlayer(playerName);

		// If the BadPlayer is null
		if (badPlayer == null) {
			// So we stop there
			return;
		}

		// Merge the data
		badPlayer.mergeData(badPlayer.getDbObject(), playerDataUpdateReceiver.getData(), true);
	}

}
