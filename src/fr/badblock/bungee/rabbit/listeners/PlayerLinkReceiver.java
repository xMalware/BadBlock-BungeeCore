package fr.badblock.bungee.rabbit.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;

/**
 * 
 * The purpose of this class is to listen to all players' packets on BungeeCord
 * to process them
 * 
 * @author xMalware
 *
 */
public class PlayerLinkReceiver extends RabbitListener {

	/**
	 * Constructor
	 */
	public PlayerLinkReceiver() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.PLAYER_PROCESSING,
				RabbitListenerType.SUBSCRIBER, true);
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
		// Get Gson
		Gson gson = badBungee.getGson();

		// Get the player packet
		PlayerPacket playerPacket = gson.fromJson(body, PlayerPacket.class);

		// If the player packet is null
		if (playerPacket == null) {
			// Log the error
			System.out.println("Error: Received a null PlayerPacket (" + body + ")");
			// So we stop there
			return;
		}

		// Get the packet type
		PlayerPacketType playerPacketType = playerPacket.getType();

		// If the packet type is null
		if (playerPacketType == null) {
			// Log the error
			System.out.println("Error: Working with a null PlayerPacketType (" + body + ")");
			// We stop there
			return;
		}

		// Get the processing
		_PlayerProcessing processing = playerPacketType.getProcess();

		// If the processing is null
		if (processing == null) {
			// Log the error
			System.out.println("Error: Working with a null Processing (" + body + ")");
			// We stop there
			return;
		}

		System.out.println("A : " + playerPacket.getPlayerName());
		// Process.
		processing.work(playerPacket);
	}

}
