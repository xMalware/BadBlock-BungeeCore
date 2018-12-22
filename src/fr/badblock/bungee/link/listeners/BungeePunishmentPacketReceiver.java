package fr.badblock.bungee.link.listeners;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.modo.commands.punishments.PunishmentManager;

/**
 * 
 * The purpose of this class is to listen to all the data that is sent between
 * all the BungeeCord nodes to synchronize.
 * 
 * @author xMalware
 *
 */
public class BungeePunishmentPacketReceiver extends RabbitListener {

	/**
	 * Constructor
	 */
	public BungeePunishmentPacketReceiver() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_PUNISHMENT,
				RabbitListenerType.MESSAGE_BROKER, false);
		// Load the listener
		load();
	}

	/**
	 * When we receive a packet
	 */
	@Override
	public void onPacketReceiving(String body) {
		PunishmentManager.handle(body);
	}

}
