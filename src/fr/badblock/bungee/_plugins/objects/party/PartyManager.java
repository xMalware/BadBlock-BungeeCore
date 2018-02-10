package fr.badblock.bungee._plugins.objects.party;

import com.mongodb.*;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.players.layer.BadPlayerSettings;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import fr.toenga.common.utils.data.Callback;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * @author xMalware
 *
 */
public class PartyManager
{

	@Getter@Setter private static PartyMessages messages = new PartyMessages();
	
	public static void follow(ProxiedPlayer sender)
	{
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				if (party == null)
				{
					messages.YOU_RE_NOT_IN_PARTY(sender);
				}
				else
				{
					if (party.getPlayers() == null)
					{
						messages.ERROR_OCCURRED(sender, 1);
						return;
					}
					PartyPlayer partyPlayer = party.getPlayers().get(sender.getName());
					if (partyPlayer == null)
					{
						messages.ERROR_OCCURRED(sender, 2);
						return;
					}
					boolean follow = partyPlayer.isFollow();
					String message = follow ? "disabled" : "enabled";
					messages.FOLLOW(sender, message);
					partyPlayer.setFollow(!follow);
					party.save();
				}
			}

		});
	}
	
	public static void invite(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			PartyManager.getMessages().INVITE_USAGE(sender);
			return;
		}
		String invited = args[1];
		BungeeManager bungeeManager = BungeeManager.getInstance();
		BadPlayer otherPlayer = bungeeManager.getBadPlayer(invited);
		BadPlayer currPlayer = BadPlayer.get(sender);
		if (otherPlayer == null || !currPlayer.getLastServer().equals(otherPlayer.getLastServer()))
		{
			PartyManager.getMessages().ACCEPT_MUSTBEONSAMESERVER(sender);
			return;
		}
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				if (party == null)
				{
					// Create party
					party = new Party(sender.getName(), invited);
					PartyManager.insert(party);
					_inviteMessage(currPlayer, otherPlayer);
				}
				else
				{
					PartyPlayer partyPlayer = party.getPartyPlayer(otherPlayer.getName());
					if (partyPlayer != null)
					{
						if (partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
						{
							PartyManager.getMessages().INVITE_ALREADYINPARTY(sender);
						}
						else
						{
							PartyManager.getMessages().INVITE_ALREADYINVITED(sender);
						}
					}
					else
					{
						party.invite(otherPlayer.getName(), PartyPlayerRole.DEFAULT);
						PartyManager.getMessages().INVITE_YOUINVITED(currPlayer, otherPlayer.getName());
						PartyManager.getMessages().INVITE_YOUHAVEBEENINVITED(otherPlayer, currPlayer.getName());
					}
				}
			}

		});
	}
	
	private static void _inviteMessage(BadPlayer currentPlayer, BadPlayer otherPlayer)
	{
		messages.INVITE_YOUINVITED(currentPlayer, otherPlayer.getName());
		messages.INVITE_YOUHAVEBEENINVITED(otherPlayer, currentPlayer.getName());
	}
	
	public static void accept(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			PartyManager.getMessages().ACCEPT_USAGE(sender);
			return;
		}
		String ownerParty = args[1];
		BungeeManager bungeeManager = BungeeManager.getInstance();
		BadPlayer otherPlayer = bungeeManager.getBadPlayer(ownerParty);
		BadPlayer currPlayer = BadPlayer.get(sender);
		if (otherPlayer == null || !currPlayer.getLastServer().equals(otherPlayer.getLastServer()))
		{
			PartyManager.getMessages().ACCEPT_MUSTBEONSAMESERVER(sender);
			return;
		}
		PartyManager.getParty(otherPlayer.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				if (party == null)
				{
					PartyManager.getMessages().ACCEPT_EXPIRED(sender, otherPlayer.getName());
					PartyManager.getMessages().ACCEPT_EXPIRED(sender, otherPlayer.getName());
					return;
				}
				PartyPlayer partyPlayer = party.getPartyPlayer(sender.getName());
				if (partyPlayer == null)
				{
					PartyManager.getMessages().ACCEPT_EXPIRED(sender, otherPlayer.getName());
					return;
				}
				if (!partyPlayer.getState().equals(PartyPlayerState.WAITING))
				{
					PartyManager.getMessages().ACCEPT_ALREADYINPARTY(sender, otherPlayer.getName());
					return;
				}
				party.accept(sender.getName());
				PartyManager.getMessages().ACCEPT_ACCEPTED(sender, otherPlayer.getName());
			}

		});
	}
	
	public static void remove(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			PartyManager.getMessages().REMOVE_USAGE(sender);
			return;
		}
		String toRemove = args[1];
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {

				// Not in group
				if (party == null)
				{
					PartyManager.getMessages().REMOVE_YOURENOTINGROUP(sender);
					return;
				}

				// Player not in group
				PartyPlayer partyPlayer = party.getPartyPlayer(toRemove);
				if (partyPlayer == null)
				{
					PartyManager.getMessages().REMOVE_PLAYERNOTINGROUP(sender, toRemove);
					return;
				}

				// Remove player
				party.remove(toRemove);

				// Send message
				if (partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
				{
					PartyManager.getMessages().REMOVE_CANCELLED(sender, partyPlayer.getName());
				}
				else
				{
					PartyManager.getMessages().REMOVE_REMOVED(sender, partyPlayer.getName());
				}

			}

		});
	}
	
	public static void tp(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			PartyManager.getMessages().TP_USAGE(sender);
			return;
		}
		String toTp = args[1];
		BungeeManager bungeeManager = BungeeManager.getInstance();
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {

				// Sender not in group
				if (party == null)
				{
					PartyManager.getMessages().TP_YOURENOTINGROUP(sender);
					return;
				}

				// Player not in group
				PartyPlayer partyPlayer = party.getPartyPlayer(toTp);
				if (partyPlayer == null)
				{
					PartyManager.getMessages().TP_PLAYERNOTINGROUP(sender, toTp);
					return;
				}

				// Player not accepted yet in group
				if (!partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
				{
					PartyManager.getMessages().TP_NOTACCEPTED(sender, toTp);
					return;
				}

				// Not connected on the server
				BadPlayer badPlayer = bungeeManager.getBadPlayer(toTp);
				if (badPlayer == null)
				{
					PartyManager.getMessages().TP_NOTCONNECTED(sender, toTp);
					return;
				}

				// Get last server
				String lastServer = badPlayer.getLastServer();

				// Unknown server to teleport (null)
				if (lastServer == null)
				{
					PartyManager.getMessages().TP_UNKNOWNSERVER(sender);
					return;
				}

				// Unknown server to teleport (empty)
				if (lastServer.isEmpty())
				{
					PartyManager.getMessages().TP_UNKNOWNSERVER(sender);
					return;
				}

				// The player isn't logged
				if (!badPlayer.isLogged())
				{
					PartyManager.getMessages().TP_NOTLOGGED(sender, badPlayer.getName());
					return;
				}

				// Get server
				ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(lastServer);

				// Unknown server
				if (serverInfo == null)
				{
					PartyManager.getMessages().TP_UNKNOWNSERVER(sender);
					return;
				}

				// Teleport to server
				sender.connect(serverInfo);
				PartyManager.getMessages().TP_TELEPORTED(sender, badPlayer.getName());

			}

		});
	}
	
	public static void toggle(ProxiedPlayer sender, String[] args)
	{
		BadPlayer badPlayer = BungeeManager.getInstance().getBadPlayer(sender);
		BadPlayerSettings settings = badPlayer.getSettings();
		if (args.length != 2)
		{

			return;
		}
		String rawType = args[1]; 
		Partyable partyable = Partyable.getByString(rawType);
		if (partyable == null)
		{
			PartyManager.getMessages().TOGGLE_UNKNOWNTYPE(sender, rawType);
			return;
		}
		if (settings.getPartyable().equals(partyable))
		{
			PartyManager.getMessages().TOGGLE_ALREADY(sender, rawType);
			return;
		}
		badPlayer.getSettings().setPartyable(partyable);
        badPlayer.updateSettings();
		PartyManager.getMessages().TOGGLE_WITH(sender, rawType);
	}
	
	public static void getParty(String player, Callback<Party> callback)
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("parties");
				BasicDBObject query = new BasicDBObject();
				query.append("players", player.toLowerCase());
				DBCursor cursor = collection.find(query);
				if (cursor != null && cursor.hasNext())
				{
					DBObject dbObject = cursor.next();
					callback.done(new Party(dbObject), null);
				}
				else
				{
					callback.done(null, null);
				}
			}
		});
	}

	public static void inGroup(String player, Callback<Boolean> callback)
	{
		player = player.toLowerCase();
		getParty(player, new Callback<Party>()
		{

			@Override
			public void done(Party result, Throwable error) {
				callback.done(result != null, null);
			}

		});
	}

	public static void update(Party party)
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("parties");
				BasicDBObject query = new BasicDBObject();

				query.put("_id", party.getUuid());

				collection.update(query, party.toObject());
			}
		});
	}

	public static void insert(Party party)
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("parties");
				collection.insert(party.toObject());
			}
		});
	}

}