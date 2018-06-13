package fr.badblock.bungee.rabbit.claimants;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.bungee.BadBungee;

/**
 * 
 * Token claimant
 * 
 * @author xMalware
 *
 */
public class BungeeTokenQuery extends RabbitClaimant {

	/**
	 * Constructor
	 */
	public BungeeTokenQuery() {
		super("bungeeTokenQuery", 500, 500);
	}

	/**
	 * Run
	 */
	@Override
	public void run() {
		// Get the main class
		BadBungee badBungee = BadBungee.getInstance();
		// Get the rabbit service
		RabbitService rabbitService = badBungee.getRabbitService();

		// Create a new rabbit packet message
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(-1, "");
		// Create a rabbit packet
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, BadBungeeQueues.BUNGEE_TOKEN_QUERY, false,
				RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER);

		// Send the packet
		rabbitService.sendPacket(rabbitPacket);
	}

}