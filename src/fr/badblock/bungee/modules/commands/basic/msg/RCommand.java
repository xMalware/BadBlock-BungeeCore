package fr.badblock.bungee.modules.commands.basic.msg;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.modules.commands.basic.friends.FriendList;
import fr.badblock.bungee.modules.commands.basic.friends.FriendListManager;
import fr.badblock.bungee.modules.commands.basic.friends.FriendListPlayer;
import fr.badblock.bungee.modules.commands.basic.friends.FriendListPlayerState;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Send a message
 *
 * No permission is required to execute this command.
 * 
 * @author xMalware
 *
 */
public class RCommand extends BadCommand
{

	// I18n key prefix
	private String prefix = "bungee.commands.msg.";

	/**
	 * Command constructor
	 */
	public RCommand()
	{
		super("r", null, "reply");
		// Allow access to the command for players only
		this.setForPlayersOnly(true);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// We get the player from the sender
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		// If no argument has been entered
		if (args.length < 1)
		{
			// We give him help.
			help(sender);
			// We stop there.
			return;
		}

		// Send a message
		msg(proxiedPlayer, args);
	}

	/**
	 * Sending help to the player
	 * @param sender
	 */
	private void help(CommandSender sender)
	{
		// Send help
		I19n.sendMessage(sender, prefix + "help", null);
	}

	/**
	 * Send a message
	 * @param sender
	 * @param args
	 */
	private void msg(ProxiedPlayer sender, String[] args)
	{
		// Get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(sender);

		// If BadPlayer is null
		if (badPlayer == null)
		{
			// So we stop there
			return;
		}

		// If the player is null
		if (badPlayer.getTmpLastMessagePlayer() == null)
		{
			// Send a message
			badPlayer.sendTranslatedOutgoingMessage(prefix + "nobodytoreply", null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = badPlayer.getTmpLastMessagePlayer();

		// If he's sending a message to himself
		if (sender.getName().equalsIgnoreCase(playerName))
		{
			// Send a message
			badPlayer.sendTranslatedOutgoingMessage(prefix + "cantsendtoyourself", null);
			// So we stop there
			return;
		}

		// Get the message
		String message = getMessage(args);

		// If the punished object isn't null
		if (badPlayer.getPunished() != null)
		{
			// If the player is muted
			if (badPlayer.getPunished().isMute())
			{
				// Send mute message
				badPlayer.sendTranslatedOutgoingMessage(prefix + "youaremute", null);
				// So we stop there
				return;
			}
		}

		// Get BungeeManager sync object
		BungeeManager bungeeManager = BungeeManager.getInstance();

		// Get BadOfflinePlayer object
		BadOfflinePlayer badOfflinePlayer = bungeeManager.getBadOfflinePlayer(playerName);

		// If the object is null or isn't loaded
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded())
		{
			// Send the unknown player message
			badPlayer.sendTranslatedOutgoingMessage(prefix + "unknownplayer", null, playerName);
			// So we stop there
			return;
		}

		// Set flag key
		String globalFlagKey = "mp";

		// If the flag exists
		if (badPlayer.getFlags().has(globalFlagKey))
		{
			// Send message
			badPlayer.sendTranslatedOutgoingMessage(prefix + "pleasewait", null);
			// So we stop there
			return;
		}

		// Set flag
		badPlayer.getFlags().set(globalFlagKey, 500);

		// If the setting object isn't null
		if (badOfflinePlayer.getSettings() != null)
		{
			// Get the pm privacy
			PMPrivacy pmPrivacy = badOfflinePlayer.getSettings().getPmPrivacy();
			// If the pm privacy isn't null and if the player doesn't have the permission
			if (pmPrivacy != null && !badPlayer.hasPermission("bungee.command.msg.bypass"))
			{
				// If the pm privacy is set to nobody
				if (pmPrivacy.equals(PMPrivacy.WITH_NOBODY))
				{
					// Send a message
					badPlayer.sendTranslatedOutgoingMessage(prefix + "dontacceptpm", null, badOfflinePlayer.getName());
					// So we stop there
					return;
				}
				// If the pm privacy is set to friends only
				if (pmPrivacy.equals(PMPrivacy.WITH_ONLY_HIS_FRIENDS))
				{
					// Create a friend boolean
					boolean friends = false;
					// Get the friend list
					FriendList friendList = FriendListManager.getFriendList(badOfflinePlayer.getUniqueId());
					// If the friend list isn't null
					if (friendList != null)
					{
						// If the player is in the list
						if (friendList.getPlayers().containsKey(badPlayer.getUniqueId()))
						{
							// Get the friend list player object
							FriendListPlayer friendListPlayer = friendList.getPlayers().get(badPlayer.getUniqueId());
							// If the friendship state is set to ACCEPTED
							if (friendListPlayer != null && friendListPlayer.getState().equals(FriendListPlayerState.ACCEPTED))
							{
								// So they're friends
								friends = true;
							}
						}
					}
					// If they're not friends
					if (!friends)
					{
						// Send a message
						badPlayer.sendTranslatedOutgoingMessage(prefix + "dontacceptpmfriends", null, badOfflinePlayer.getName());
						// So we stop there
						return;
					}
				}
			}
		}

		// If the punished object isn't null
		if (badOfflinePlayer.getPunished() != null)
		{
			// If the receiver is muted
			if (badOfflinePlayer.getPunished().isMute())
			{
				// Send a message
				badPlayer.sendTranslatedOutgoingMessage(prefix + "heismuted", null, badOfflinePlayer.getName());
				// So we stop there
				return;
			}
		}

		// Get the colored message
		String color = ChatColor.translateAlternateColorCodes('&', message);

		// Check if the colored message isn't the same
		if (!color.equals(message))
		{
			// Send a message
			badPlayer.sendTranslatedOutgoingMessage(prefix + "antihackcolor", null, badOfflinePlayer.getName());
			// So we stop there
			return;
		}

		// Boolean => if the receiver is online or not
		final boolean online = bungeeManager.hasUsername(playerName);

		// Get the intro message
		String Dintro = badPlayer.getTranslatedMessage(prefix + "send.intro", null);
		// Get the message
		String Dmessage = badPlayer.getTranslatedMessage(prefix + "send.message", new int[] { 0, 2}, badPlayer.getRawChatPrefix(), badPlayer.getName(),
				badPlayer.getRawChatSuffix(), message);
		// Get the message hover
		String Dmessage_hover = badPlayer.getTranslatedMessage(prefix + "send.message_hover", new int[] { 0, 2}, 
				badOfflinePlayer.getRawChatPrefix(), badOfflinePlayer.getName(), badOfflinePlayer.getRawChatSuffix(), message);

		// Get the McJson
		McJson json = new McJsonFactory(Dintro).finaliseComponent().initNewComponent(Dmessage).setHoverText(Dmessage_hover)
				.setClickSuggest("/msg " + badOfflinePlayer.getName() + " ").build();

		// Send the message
		badPlayer.sendTranslatedOutgoingMCJson(json);

		// If the receiver is online
		if (online)
		{
			// Get the selected BadPlayer
			BadPlayer selectedBadPlayer = badOfflinePlayer.getOnlineBadPlayer();

			// If the selected BadPlayer object is null
			if (selectedBadPlayer == null)
			{
				// Send the unknown player message
				badPlayer.sendTranslatedOutgoingMessage(prefix + "unknownplayer", null, badOfflinePlayer.getName());
				// So we stop there
				return;
			}

			// Get the intro message
			Dintro = selectedBadPlayer.getTranslatedMessage(prefix + "receive.intro", null);
			// Get the message
			Dmessage = selectedBadPlayer.getTranslatedMessage(prefix + "receive.message", new int[] { 0, 2},
					badPlayer.getRawChatPrefix(), badPlayer.getName(), badPlayer.getRawChatSuffix(), message);
			// Get the message hover
			Dmessage_hover = selectedBadPlayer.getTranslatedMessage(prefix + "receive.message_hover", new int[] { 0, 2},
					badPlayer.getRawChatPrefix(), badPlayer.getName(), badPlayer.getRawChatSuffix(), message);

			// Get the McJson
			json = new McJsonFactory(Dintro).finaliseComponent().initNewComponent(Dmessage).setHoverText(Dmessage_hover)
					.setClickSuggest("/msg " + sender.getName() + " ").build();

			// Send the message
			selectedBadPlayer.sendTranslatedOutgoingMCJson(json);
		}
		else
		{
			// Send offline warn message
			badPlayer.sendTranslatedOutgoingMessage(prefix + "offline", null, badOfflinePlayer.getName());
		}

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
				DBCollection collection = db.getCollection("pm");
				// Create query
				DBObject query = new BasicDBObject();

				// Put the sender name
				query.put("sender", sender.getName());
				// Put the sender unique id
				query.put("senderUuid", badPlayer.getUniqueId().toString());
				// Put the sender server
				query.put("senderServer", badPlayer.getCurrentServer());
				// Put the sender command
				query.put("senderCommand", "/msg");
				// Put the receiver name
				query.put("receiver", playerName);
				// Put the receiver unique id
				query.put("receiverUuid", badOfflinePlayer.getUniqueId().toString());
				// Put the receiver message
				query.put("message", message);
				// Put the received boolean
				query.put("received", online);
				// Put the timestamp
				query.put("timestamp", System.currentTimeMillis());
				// Put the date
				query.put("date", new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()));

				// Insert into the collection
				collection.insert(query);
			}
		});

	}

	/**
	 * Get a message with args
	 * @param args
	 * @return
	 */
	private String getMessage(String[] args)
	{
		// Create a StringBuilder
		StringBuilder stringBuilder = new StringBuilder();
		// Index
		int i = 0;
		// For each arg
		for (String arg : args)
		{
			// Increment index
			i++;
			// Spacer
			String spacer = " ";
			// If the args are the same
			if (args.length == i)
			{
				// Empty spacer
				spacer = "";
			}
			// Append to the stringBuilder
			stringBuilder.append(arg + spacer);
		}

		// Returns the result
		return stringBuilder.toString();
	}

}