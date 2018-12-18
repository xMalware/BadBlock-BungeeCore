package fr.badblock.bungee.link.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.packets.BungeePacket;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.BungeeAddServerProcessing;
import fr.badblock.bungee.link.processing.bungee.BungeeBroadcastProcessing;
import fr.badblock.bungee.link.processing.bungee.BungeeForceKickProcessing;
import fr.badblock.bungee.link.processing.bungee.BungeeLogProcessing;
import fr.badblock.bungee.link.processing.bungee.BungeeRemoveServerProcessing;
import fr.badblock.bungee.link.processing.bungee.BungeeServerBroadcastProcessing;

/**
 * 
 * The purpose of this class is to listen to all BungeePacket sent to process
 * them.
 * 
 * @author xMalware
 *
 */
public class BungeeLinkReceiver extends RabbitListener {

	/**
	 * Constructor
	 */
	public BungeeLinkReceiver() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_PROCESSING,
				RabbitListenerType.SUBSCRIBER, false);
		// Load the listener
		load();
		BungeePacketType.ADD_SERVER.setProcess(new BungeeAddServerProcessing());
		BungeePacketType.BROADCAST.setProcess(new BungeeBroadcastProcessing());
		BungeePacketType.FORCE_KICK.setProcess(new BungeeForceKickProcessing());
		BungeePacketType.LOG.setProcess(new BungeeLogProcessing());
		BungeePacketType.REMOVE_SERVER.setProcess(new BungeeRemoveServerProcessing());
		BungeePacketType.SERVER_BROADCAST.setProcess(new BungeeServerBroadcastProcessing());
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
		// Get the bungee packet
		BungeePacket bungeePacket = gson.fromJson(body, BungeePacket.class);

		// If the packet is null
		if (bungeePacket == null) {
			// Log the error
			System.out.println("Error: Received a null PlayerPacket (" + body + ")");
			// We stop there
			return;
		}

		// Get the packet type
		BungeePacketType bungeePacketType = bungeePacket.getType();

		// If the packet type is null
		if (bungeePacketType == null) {
			// Log the error
			System.out.println("Error: Working with a null PlayerPacketType (" + body + ")");
			// We stop there
			return;
		}

		// Get the packet processing
		_BungeeProcessing processing = bungeePacketType.getProcess();

		// If the processing is null
		if (processing == null) {
			// Log the error
			System.out.println("Error: Working with a null Processing (" + body + ")");
			// We stop there
			return;
		}

		// Process.
		processing.done(bungeePacket.getContent());
	}

}
