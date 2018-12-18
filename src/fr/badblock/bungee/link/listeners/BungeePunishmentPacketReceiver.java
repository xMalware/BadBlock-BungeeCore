package fr.badblock.bungee.link.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.commands.modo.punishments.PunishmentPacket;
import fr.badblock.bungee.modules.commands.modo.punishments.PunishmentType;

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
		// Get the main class
		BadBungee badBungee = BadBungee.getInstance();
		// Get Gson object
		Gson gson = badBungee.getGson();
		// Get Punishment packet object
		PunishmentPacket punishmentPacket = gson.fromJson(body, PunishmentPacket.class);

		// If the packet is null
		if (punishmentPacket == null) {
			// Log the error
			System.out.println("Error: Received a null PunishmentPacket (" + body + ")");
			// We stop there
			return;
		}

		// Get the packet type
		PunishmentType punishmentType = punishmentPacket.getPunishmentType();

		// If the packet type is null
		if (punishmentType == null) {
			// Log the error
			System.out.println("Error: Working with a null PunishmentType (" + body + ")");
			// We stop there
			return;
		}

		// Process.
		punishmentPacket.process();
	}

}
