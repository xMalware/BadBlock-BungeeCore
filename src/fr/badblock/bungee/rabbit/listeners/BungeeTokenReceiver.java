package fr.badblock.bungee.rabbit.listeners;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.rabbit.BadBungeeQueues;

/**
 * 
 * The purpose of this class is to listen to BungeeCord tokens to synchronize players' calculations.
 * 
 * @author xMalware
 *
 */
public class BungeeTokenReceiver extends RabbitListener
{

	/**
	 * Constructor
	 */
	public BungeeTokenReceiver() 
	{
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_TOKEN_RESULT, RabbitListenerType.SUBSCRIBER, true);
		// Load the listener
		load();
	}

	/**
	 * When we receive a packet
	 */
	@Override
	public void onPacketReceiving(String body)
	{
		// TODO
	}

}
