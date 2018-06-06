package fr.badblock.bungee.rabbit.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacket;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacketType;
import fr.badblock.bungee.link.processing.bungee.abstracts._BungeeProcessing;
import fr.badblock.bungee.rabbit.BadBungeeQueues;

/**
 * 
 * The purpose of this class is to listen to all BungeePacket sent to process them.
 * 
 * @author xMalware
 *
 */
public class BungeeLinkReceiver extends RabbitListener
{

	/**
	 * Constructor
	 */
	public BungeeLinkReceiver() 
	{
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_PROCESSING, RabbitListenerType.SUBSCRIBER, false);
		// Load the listener
		load();
	}

	/**
	 * When we receive a packet
	 */
	@Override
	public void onPacketReceiving(String body)
	{
		// Get the main class
		BadBungee badBungee = BadBungee.getInstance();
		// Get gson
		Gson gson = badBungee.getGson();
		// Get the bungee packet
		BungeePacket bungeePacket = gson.fromJson(body, BungeePacket.class);
		
		// If the packet is null
		if (bungeePacket == null)
		{
			// Log the error
			System.out.println("Error: Received a null PlayerPacket (" + body + ")");
			// We stop there
			return;
		}
		
		// Get the packet type
		BungeePacketType bungeePacketType = bungeePacket.getType();
		
		// If the packet type is null
		if (bungeePacketType == null)
		{
			// Log the error
			System.out.println("Error: Working with a null PlayerPacketType (" + body + ")");	
			// We stop there
			return;
		}
		
		// Get the packet processing
		_BungeeProcessing processing = bungeePacketType.getProcess();
		
		// If the processing is null
		if (processing == null)
		{
			// Log the error
			System.out.println("Error: Working with a null Processing (" + body + ")");	
			// We stop there
			return;
		}
		
		// Process.
		processing.done(bungeePacket.getContent());
	}

}
