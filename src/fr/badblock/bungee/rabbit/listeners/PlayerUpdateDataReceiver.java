package fr.badblock.bungee.rabbit.listeners;

import com.google.gson.Gson;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeLocalManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.rabbit.BadBungeeQueues;
import fr.badblock.bungee.rabbit.datareceivers.PlayerDataUpdateReceiver;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListener;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListenerType;

public class PlayerUpdateDataReceiver extends RabbitListener
{

	public PlayerUpdateDataReceiver() 
	{
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_DATA_RECEIVERS_UPDATE, RabbitListenerType.SUBSCRIBER, false);
		load();
	}

	@Override
	public void onPacketReceiving(String body)
	{
		BadBungee badBungee = BadBungee.getInstance();
		Gson gson = badBungee.getGson();
		
		PlayerDataUpdateReceiver playerDataUpdateReceiver = gson.fromJson(body, PlayerDataUpdateReceiver.class);
		String playerName = playerDataUpdateReceiver.getPlayerName();
		
		BungeeLocalManager bungeeLocalManager = BungeeLocalManager.getInstance();
		
		// Is online?
		if (!bungeeLocalManager.isOnline(playerName))	
		{
			/**
			 * là, le joueur n'est pas co alors qu'on doit update ses données. faut voir comment on pourrait faire autrement..
			 */
			return;
		}
		
		BadPlayer badPlayer = bungeeLocalManager.getPlayer(playerName);
		// What??
		if (badPlayer == null)
		{
			return;
		}
		
		badPlayer.mergeData(badPlayer.getDbObject(), playerDataUpdateReceiver.getData(), true);
		badPlayer.sendDataToBukkit();
	}

}
