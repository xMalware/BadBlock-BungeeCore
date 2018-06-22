package fr.badblock.bungee.modules.commands.basic.msg;

import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerLoggedEvent;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class MessageReconnectListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerLoggedEvent event) {
		// Getting the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();

		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Use asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("pm");
				// Create query
				DBObject query = new BasicDBObject();

				// Put the receiver unique id
				query.put("receiverUuid", badPlayer.getUniqueId().toString());
				// Put the received boolean
				query.put("received", false);

				// Get results
				DBCursor cursor = collection.find(query);

				try
				{
					while (cursor.hasNext())
					{
						DBObject data = cursor.next();
						
						// Raw message
						String message = data.get("message").toString();
						
						// Sender
						String senderRawUuid = data.get("senderUuid").toString();
						
						UUID senderUuid = UUID.fromString(senderRawUuid);
						
						if (senderUuid == null)
						{
							continue;
						}
						
						BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(senderUuid);
						
						// Get the intro message
						String Dintro = badPlayer.getTranslatedMessage(MsgCommand.prefix + "receive.intro", null);
						// Get the message
						String Dmessage = badPlayer.getTranslatedMessage(MsgCommand.prefix + "receive.message", new int[] { 0, 2 },
								badOfflinePlayer.getRawChatPrefix(), badOfflinePlayer.getName(), badOfflinePlayer.getRawChatSuffix(), message);
						// Get the message hover
						String Dmessage_hover = badPlayer.getTranslatedMessage(MsgCommand.prefix + "receive.message_hover",
								new int[] { 0, 2 }, badOfflinePlayer.getRawChatPrefix(), badOfflinePlayer.getName(), badOfflinePlayer.getRawChatSuffix(),
								message);

						// Get the McJson
						McJson json = new McJsonFactory(Dintro).finaliseComponent().initNewComponent(Dmessage).setHoverText(Dmessage_hover)
								.setClickSuggest("/msg " + badOfflinePlayer.getName() + " ").build();

						// Send the message
						badPlayer.sendTranslatedOutgoingMCJson(json);

						// Set reply
						badPlayer.setTmpLastMessagePlayer(badOfflinePlayer.getName());
						
						// New query
						BasicDBObject newQuery = new BasicDBObject();
						// Update query
						BasicDBObject update = new BasicDBObject("received", true);

						// Query name
						newQuery.put("_id", data.get("_id"));
						// Set the updater as a setter
						BasicDBObject updater = new BasicDBObject("$set", update);

						// Update the collection with the query & updater
						collection.update(newQuery, updater);
					}
				}
				catch (Exception error)
				{
					error.printStackTrace();
				}

				// Close the cursor
				cursor.close();
			}
		});
	}

}