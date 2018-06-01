package fr.badblock.bungee.rabbit.listeners;

import com.google.gson.Gson;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.rabbit.BadBungeeQueues;

public class PlayerLinkReceiver extends RabbitListener
{

	public PlayerLinkReceiver() 
	{
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.PLAYER_PROCESSING, RabbitListenerType.SUBSCRIBER, true);
		load();
	}

	@Override
	public void onPacketReceiving(String body) {
		BadBungee badBungee = BadBungee.getInstance();
		Gson gson = badBungee.getGson();
		PlayerPacket playerPacket = gson.fromJson(body, PlayerPacket.class);
		if (playerPacket == null)
		{
			System.out.println("Error: Received a null PlayerPacket (" + body + ")");
			return;
		}
		PlayerPacketType playerPacketType = playerPacket.getType();
		if (playerPacketType == null)
		{
			System.out.println("Error: Working with a null PlayerPacketType (" + body + ")");	
			return;
		}
		_PlayerProcessing processing = playerPacketType.getProcess();
		if (processing == null)
		{
			System.out.println("Error: Working with a null Processing (" + body + ")");	
			return;
		}
		processing.work(playerPacket);
	}

}
