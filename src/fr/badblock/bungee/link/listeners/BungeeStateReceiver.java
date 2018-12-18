package fr.badblock.bungee.link.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.sync.bungee.states.BungeeStatePacket;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.tasks.BungeeTask;

/**
 * 
 * @author xMalware
 *
 */
public class BungeeStateReceiver extends RabbitListener {

	/**
	 * Constructor
	 */
	public BungeeStateReceiver() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_STATE, RabbitListenerType.SUBSCRIBER,
				false);
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
		// Get Gson object
		Gson gson = badBungee.getGson();
		// Get Bungee state packet object
		BungeeStatePacket bungeeStatePacket = gson.fromJson(body, BungeeStatePacket.class);

		// If the packet is null
		if (bungeeStatePacket == null) {
			// Log the error
			System.out.println("Error: Received a null BungeeStatePacket (" + body + ")");
			// We stop there
			return;
		}

		if (!BungeeTask.bungeeObject.getName().equalsIgnoreCase(bungeeStatePacket.getName())) {
			return;
		}

		// Process.
		BungeeTask.bungeeObject.setState(bungeeStatePacket.getState());
	}

}
