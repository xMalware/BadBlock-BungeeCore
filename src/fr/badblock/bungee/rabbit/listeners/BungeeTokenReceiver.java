package fr.badblock.bungee.rabbit.listeners;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.bungee.CachedPlayerToken;
import fr.badblock.bungee.rabbit.BadBungeeQueues;

/**
 * 
 * The purpose of this class is to listen to BungeeCord tokens to synchronize
 * players' calculations.
 * 
 * @author xMalware
 *
 */
public class BungeeTokenReceiver extends RabbitListener {

	/**
	 * Constructor
	 */
	public BungeeTokenReceiver() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_TOKEN_RESULT,
				RabbitListenerType.SUBSCRIBER, true);
		// Load the listener
		load();
	}

	/**
	 * When we receive a packet
	 */
	@Override
	public void onPacketReceiving(String body) {
		// Get cached player token
		CachedPlayerToken cachedPlayerToken = GsonUtils.getGson().fromJson(body, CachedPlayerToken.class);
		// Set the token
		BungeeManager.getInstance().setToken(cachedPlayerToken);
	}

}
