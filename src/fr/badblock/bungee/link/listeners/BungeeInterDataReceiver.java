package fr.badblock.bungee.link.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.bungee.BungeeObject;

/**
 * 
 * The purpose of this class is to listen to all the data that is sent between
 * all the BungeeCord nodes to synchronize.
 * 
 * @author xMalware
 *
 */
public class BungeeInterDataReceiver extends RabbitListener {

	/**
	 * Constructor
	 */
	public BungeeInterDataReceiver() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_DATA, RabbitListenerType.SUBSCRIBER,
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
		// Get Bungee object
		BungeeObject bungeeObject = gson.fromJson(body, BungeeObject.class);
		// Add the Bungee object
		BungeeManager.getInstance().add(bungeeObject);

	}

}
