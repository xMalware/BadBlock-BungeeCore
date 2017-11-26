package fr.badblock.bungee.rabbit.listeners;

import com.google.gson.Gson;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.bungee.BungeeObject;
import fr.badblock.bungee.rabbit.BadBungeeQueues;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListener;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListenerType;

public class BungeeInterDataReceiver extends RabbitListener
{

	public BungeeInterDataReceiver() 
	{
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_DATA, RabbitListenerType.SUBSCRIBER, false);
		load();
	}

	@Override
	public void onPacketReceiving(String body)
	{
		BadBungee badBungee = BadBungee.getInstance();
		Gson gson = badBungee.getGson();
		BungeeObject bungeeObject = gson.fromJson(body, BungeeObject.class);
		BungeeManager.getInstance().add(bungeeObject);
	}

}
