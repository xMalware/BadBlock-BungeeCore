package fr.badblock.bungee.rabbit.listeners;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitRequestListener;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeLocalManager;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;

/**
 * 
 * @author xMalware
 *
 */
public class PlayerDataRequester extends RabbitRequestListener {

	/**
	 * Constructor
	 */
	public PlayerDataRequester() {
		// Super!
		super(BadBungee.getInstance().getRabbitService(), BadBungeeQueues.BUNGEE_DATA_PLAYERS_RPC, true);
		// Load the listener
		load();
	}

	/**
	 * When we receive a packet
	 */
	@Override
	public String reply(String playerName)
	{
		System.out.println("Requester A");
		// Get the local manager
		BungeeLocalManager bungeeLocalManager = BungeeLocalManager.getInstance();

		// If the player isn't online
		if (!bungeeLocalManager.isOnline(playerName)) {
			System.out.println("Requester B");
			// Get offline player
			BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);
			System.out.println("Requester C");
			if (badOfflinePlayer.getDbObject() == null)
			{
				System.out.println("Requester D");
				return null;
			}
			System.out.println("Requester E");
			// Get data
			return badOfflinePlayer.getDbObject().toJson();
		}
		System.out.println("Requester F");

		// Get the BadPlayer
		BadPlayer badPlayer = bungeeLocalManager.getPlayer(playerName);

		System.out.println("Requester G");
		// If the BadPlayer is null
		if (badPlayer == null) {
			System.out.println("Requester H");
			// Get offline player
			BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);
			System.out.println("Requester I");
			if (badOfflinePlayer.getDbObject() == null)
			{
				System.out.println("Requester J");
				return null;
			}
			System.out.println("Requester K");
			// Get data
			return badOfflinePlayer.getDbObject().toJson();
		}


		if (badPlayer.getDbObject() == null)
		{
			System.out.println("Requester L");
			return null;
		}
		
		System.out.println("Requester M");
		
		return badPlayer.getDbObject().toJson();
	}

}
