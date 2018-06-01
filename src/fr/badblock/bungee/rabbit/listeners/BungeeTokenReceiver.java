package fr.badblock.bungee.rabbit.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacket;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacketType;
import fr.badblock.bungee.link.processing.bungee.abstracts._BungeeProcessing;
import fr.badblock.bungee.rabbit.BadBungeeQueues;

public class BungeeTokenReceiver extends RabbitListener
{

	public BungeeTokenReceiver() 
	{
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_TOKEN_RESULT, RabbitListenerType.SUBSCRIBER, true);
		load();
	}

	@Override
	public void onPacketReceiving(String body) {
		BadBungee badBungee = BadBungee.getInstance();
		Gson gson = badBungee.getGson();
		BungeePacket bungeePacket = gson.fromJson(body, BungeePacket.class);
		if (bungeePacket == null)
		{
			System.out.println("Error: Received a null PlayerPacket (" + body + ")");
			return;
		}
		BungeePacketType bungeePacketType = bungeePacket.getType();
		if (bungeePacketType == null)
		{
			System.out.println("Error: Working with a null PlayerPacketType (" + body + ")");	
			return;
		}
		_BungeeProcessing processing = bungeePacketType.getProcess();
		if (processing == null)
		{
			System.out.println("Error: Working with a null Processing (" + body + ")");	
			return;
		}
		processing.done(bungeePacket.getContent());
	}

}
