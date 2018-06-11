package fr.badblock.bungee.modules.commands.basic.party;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.api.common.utils.data.Callback;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.players.layer.BadPlayerSettings;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Party Manager
 * 
 * @author xMalware
 *
 */
public class PartyManager
{

	/**
	 * Party Messages
	 * @param Set new party messages instance
	 * @return Returns the current party messages instance
	 */
	@Getter@Setter private static PartyMessages messages = new PartyMessages();

	/**
	 * Follow
	 * @param sender
	 */
	public static void follow(ProxiedPlayer sender)
	{
		// Get the party
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			/**
			 * When it's done
			 */
			public void done(Party party, Throwable error)
			{
				// If the party is null
				if (party == null)
				{
					// Send a message
					messages.sendYouAreNotInParty(sender);
				}
				// If the party isn't null
				else
				{
					// If the player map is null
					if (party.getPlayers() == null)
					{
						// Send a message
						messages.sendErrorOccurred(sender, 1);
						// So we stop there
						return;
					}

					// Get the party player
					PartyPlayer partyPlayer = party.getPlayers().get(sender.getName());

					// If the party player is null
					if (partyPlayer == null)
					{
						// Send a message
						messages.sendErrorOccurred(sender, 2);
						// So we stop there
						return;
					}

					// Is following
					boolean follow = partyPlayer.isFollow();
					// Get the message
					String message = follow ? "disabled" : "enabled";

					// Send follow message
					messages.sendFollow(sender, message);
					// Set follow
					partyPlayer.setFollow(!follow);

					// Save party
					party.save();
				}
			}

		});
	}

	/**
	 * Invite
	 * @param sender
	 * @param args
	 */
	public static void invite(ProxiedPlayer sender, String[] args)
	{
		// Oooh :( 
		if (args.length != 2)
		{
			// Send invite usage 
			PartyManager.getMessages().sendInviteUsage(sender);
			// We stop there
			return;
		}

		// Get invited arg
		String invited = args[1];
		// Get Bungee Manager
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// Get the other player
		BadPlayer otherPlayer = bungeeManager.getBadPlayer(invited);
		// Get the current player
		BadPlayer currPlayer = BadPlayer.get(sender);

		// If he invites himself
		if (invited.equalsIgnoreCase(sender.getName()))
		{
			// Send the message
			PartyManager.getMessages().sendCantActOnYourself(sender);
			return;
		}
		
		// If they're not in the same server
		if (otherPlayer == null || !currPlayer.getCurrentServer().equals(otherPlayer.getCurrentServer()))
		{
			// Send the message
			PartyManager.getMessages().sendAcceptMustBeOnSameServer(sender);
			// So we stop there
			return;
		}
		
		// Set a flag name
		String flagName = "party_invite_" + invited;
		
		// If the flag exists
		if (currPlayer.getFlags().has(flagName))
		{
			// So we stop there
			return;
		}

		// Get the party
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			/**
			 * When you receive the party data
			 */
			@Override
			public void done(Party party, Throwable error)
			{
				// If party is null
				if (party == null)
				{
					// Create party
					party = new Party(sender.getName(), invited);
					// Insert into the database
					PartyManager.insert(party);
					// Send invite message
					_inviteMessage(currPlayer, otherPlayer);
				}
				// If party isn't null
				else
				{
					// Get the party player
					PartyPlayer partyPlayer = party.getPartyPlayer(otherPlayer.getName());

					// If the party player isn't null
					if (partyPlayer != null)
					{
						// If the state is accepted
						if (partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
						{
							// Send the message
							PartyManager.getMessages().sendInviteAlreadyInParty(sender);
						}
						// Else
						else
						{
							// Send the message
							PartyManager.getMessages().sendInviteAlreadyInvited(sender);
						}
					}
					// Null party player
					else
					{
						// Add flag
						currPlayer.getFlags().set(flagName, 60 * 2 * 1000);
						// Invite the party player
						party.invite(otherPlayer.getName(), PartyPlayerRole.DEFAULT);
						// Send the message
						PartyManager.getMessages().sendInviteYouInvited(currPlayer, otherPlayer.getName());
						// Send the message
						PartyManager.getMessages().sendInviteYouHaveBeenInvited(otherPlayer, currPlayer.getName());
					}
				}
			}

		});
	}

	/**
	 * Send the invitation message
	 * @param currentPlayer
	 * @param otherPlayer
	 */
	private static void _inviteMessage(BadPlayer currentPlayer, BadPlayer otherPlayer)
	{
		// Send 'you invited'
		messages.sendInviteYouInvited(currentPlayer, otherPlayer.getName());
		// Send 'you have been invited'
		messages.sendInviteYouHaveBeenInvited(otherPlayer, currentPlayer.getName());
	}

	/**
	 * Accept
	 * @param sender
	 * @param args
	 */
	public static void accept(ProxiedPlayer sender, String[] args)
	{
		// Ooh :-(
		if (args.length != 2)
		{
			// Send accept usage
			PartyManager.getMessages().sendAcceptUsage(sender);
			// So we stop there
			return;
		}

		// Get owner party arg
		String ownerParty = args[1];
		// Get BungeeManager
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// Get other player
		BadPlayer otherPlayer = bungeeManager.getBadPlayer(ownerParty);
		// Get current player
		BadPlayer currPlayer = BadPlayer.get(sender);

		// If he accepts himself
		if (ownerParty.equalsIgnoreCase(sender.getName()))
		{
			// Send the message
			PartyManager.getMessages().sendCantActOnYourself(sender);
			return;
		}

		// If they're not on the same server
		if (otherPlayer == null || !currPlayer.getCurrentServer().equals(otherPlayer.getCurrentServer()))
		{
			// Send the message
			PartyManager.getMessages().sendAcceptMustBeOnSameServer(sender);
			// So we stop there
			return;
		}

		// Get the party
		PartyManager.getParty(otherPlayer.getName(), new Callback<Party>()
		{

			/**
			 * When we receive the data
			 */
			@Override
			public void done(Party party, Throwable error)
			{
				// If the party is null
				if (party == null)
				{
					// Send expire message
					PartyManager.getMessages().sendAcceptExpired(sender, otherPlayer.getName());
					// Send another expire message..
					PartyManager.getMessages().sendAcceptExpired(sender, otherPlayer.getName());
					// So we stop there
					return;
				}

				// Get the party player
				PartyPlayer partyPlayer = party.getPartyPlayer(sender.getName());

				// If the party player is null
				if (partyPlayer == null)
				{
					// Send message
					PartyManager.getMessages().sendAcceptExpired(sender, otherPlayer.getName());
					// So we stop there
					return;
				}

				// If the state isn't in 'waiting'
				if (!partyPlayer.getState().equals(PartyPlayerState.WAITING))
				{
					// Send message
					PartyManager.getMessages().sendAcceptAlreadyInParty(sender, otherPlayer.getName());
					// So we stop there
					return;
				}

				// Accept party player
				party.accept(sender.getName());
				// Send accepted message
				PartyManager.getMessages().sendAcceptAccepted(sender, otherPlayer.getName());
			}

		});
	}

	/**
	 * Remove
	 * @param sender
	 * @param args
	 */
	public static void remove(ProxiedPlayer sender, String[] args)
	{
		// Ooh :(
		if (args.length != 2)
		{
			// Send message
			PartyManager.getMessages().sendRemoveUsage(sender);
			// So we stop there
			return;
		}

		// Get to remove arg
		String toRemove = args[1];

		// If he removes himself
		if (toRemove.equalsIgnoreCase(sender.getName()))
		{
			// Send the message
			PartyManager.getMessages().sendCantActOnYourself(sender);
			return;
		}

		// Get party
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			/**
			 * When we receive the data
			 */
			@Override
			public void done(Party party, Throwable error)
			{

				// If the party is null
				if (party == null)
				{
					// Send message
					PartyManager.getMessages().sendRemoveYouAreNotInGroup(sender);
					// So we stop there
					return;
				}

				// Get the party player
				PartyPlayer partyPlayer = party.getPartyPlayer(toRemove);

				// If the party player is null
				if (partyPlayer == null)
				{
					// Send message
					PartyManager.getMessages().sendRemovePlayerNotInGroup(sender, toRemove);
					// So we stop there
					return;
				}

				// Remove player
				party.remove(toRemove);

				// Send message
				if (partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
				{
					// Send cancelled message
					PartyManager.getMessages().sendRemoveCancelled(sender, partyPlayer.getName());
				}
				else
				{
					// Send removed message
					PartyManager.getMessages().sendRemoveRemoved(sender, partyPlayer.getName());
				}

			}

		});
	}

	/**
	 * Teleport
	 * @param sender
	 * @param args
	 */
	public static void tp(ProxiedPlayer sender, String[] args)
	{
		// Ooh :(
		if (args.length != 2)
		{
			// Send teleport usage
			PartyManager.getMessages().sendTpUsage(sender);
			// So we stop there
			return;
		}

		// To teleport arg
		String toTp = args[1];
		// Bungee manager
		BungeeManager bungeeManager = BungeeManager.getInstance();

		// Get the party
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			/**
			 * When we receive the data
			 */
			@Override
			public void done(Party party, Throwable error)
			{

				// If the party is null
				if (party == null)
				{
					// Send message
					PartyManager.getMessages().sendTpYouAreNotInGroup(sender);
					// So we stop there
					return;
				}

				// Get party player
				PartyPlayer partyPlayer = party.getPartyPlayer(toTp);

				// If the party player is null
				if (partyPlayer == null)
				{
					// Send message
					PartyManager.getMessages().sendTpPlayerNotInGroup(sender, toTp);
					// So we stop there
					return;
				}

				// Player not accepted yet in group
				if (!partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
				{
					// Send message
					PartyManager.getMessages().sendTpNotAccepted(sender, toTp);
					// So we stop there
					return;
				}

				// Get the BadPlayer object
				BadPlayer badPlayer = bungeeManager.getBadPlayer(toTp);

				// If the BadPlayer object is null
				if (badPlayer == null)
				{
					// Send message
					PartyManager.getMessages().sendTpNotConnected(sender, toTp);
					// So we stop there
					return;
				}

				// Get last server
				String lastServer = badPlayer.getLastServer();

				// Unknown server to teleport (null)
				if (lastServer == null)
				{
					// Send message
					PartyManager.getMessages().sendTpUnknownServer(sender);
					// So we stop there
					return;
				}

				// Unknown server to teleport (empty)
				if (lastServer.isEmpty())
				{
					// Send message
					PartyManager.getMessages().sendTpUnknownServer(sender);
					// So we stop there
					return;
				}

				// The player isn't logged
				if (!badPlayer.isLogged())
				{
					// Send message
					PartyManager.getMessages().sendTpNotLogged(sender, badPlayer.getName());
					// So we stop there
					return;
				}

				// Get server
				ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(lastServer);

				// Unknown server
				if (serverInfo == null)
				{
					// Send message
					PartyManager.getMessages().sendTpUnknownServer(sender);
					// So we stop there
					return;
				}

				// Teleport to server
				sender.connect(serverInfo);
				// Send message
				PartyManager.getMessages().sendTpTeleported(sender, badPlayer.getName());
			}

		});
	}

	/**
	 * Toggle
	 * @param sender
	 * @param args
	 */
	public static void toggle(ProxiedPlayer sender, String[] args)
	{
		// Get BadPlayer object
		BadPlayer badPlayer = BungeeManager.getInstance().getBadPlayer(sender);
		// Get settings
		BadPlayerSettings settings = badPlayer.getSettings();

		// Ooh :(
		if (args.length != 2)
		{
			// Send message
			PartyManager.getMessages().sendToggleUsage(badPlayer);
			// So we stop there
			return;
		}

		// Raw type
		String rawType = args[1]; 
		// Partyable
		Partyable partyable = Partyable.getByString(rawType);

		// If partyable is null
		if (partyable == null)
		{
			// Send message
			PartyManager.getMessages().sendToggleUnknownType(sender, rawType);
			// So we stop there
			return;
		}

		// If the partyable is the same
		if (settings.getPartyable().equals(partyable))
		{
			// Already toggle message
			PartyManager.getMessages().sendToggleAlready(sender, rawType);
			// So we stop there
			return;
		}

		// Set partyable
		badPlayer.getSettings().setPartyable(partyable);
		// Update settings
		badPlayer.updateSettings();
		// Send message
		PartyManager.getMessages().sendToggleWith(sender, rawType);
	}

	/**
	 * Get the party
	 * @param player
	 * @param callback
	 */
	public static void getParty(String player, Callback<Party> callback)
	{
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			/**
			 * Use asynchronously
			 */
			@Override
			public void run(MongoService mongoService)
			{
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("parties");
				// Create empty query
				BasicDBObject query = new BasicDBObject();
				// Append player map
				query.append("players", player.toLowerCase());
				// Cursor
				DBCursor cursor = collection.find(query);

				// If the cursor isn't null
				if (cursor != null && cursor.hasNext())
				{
					// Get the DBObject
					DBObject dbObject = cursor.next();
					// Done callback
					callback.done(new Party(dbObject), null);
				}
				else
				{
					// Empty callback
					callback.done(null, null);
				}
			}
		});
	}

	/**
	 * In group
	 * @param player
	 * @param callback
	 */
	public static void inGroup(String player, Callback<Boolean> callback)
	{
		// Set to lower case username
		player = player.toLowerCase();
		// Get party
		getParty(player, new Callback<Party>()
		{

			/**
			 * When we receive the party data
			 */
			@Override
			public void done(Party result, Throwable error)
			{
				// Callback
				callback.done(result != null, null);
			}

		});
	}

	/**
	 * Update party data
	 * @param party
	 */
	public static void update(Party party)
	{
		// Mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			/**
			 * Asynchronously
			 */
			@Override
			public void run(MongoService mongoService)
			{
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("parties");
				// New query
				BasicDBObject query = new BasicDBObject();

				// Set the id
				query.put("_id", party.getUuid());

				// Update in the collection
				collection.update(query, party.toObject());
			}
		});
	}

	/**
	 * Insert the party
	 * @param party
	 */
	public static void insert(Party party)
	{
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			/**
			 * Asynchronously
			 */
			@Override
			public void run(MongoService mongoService)
			{
				// Get database
				DB db = mongoService.getDb();
				// Get collection
				DBCollection collection = db.getCollection("parties");
				// Insert the collection
				collection.insert(party.toObject());
			}
		});
	}

}