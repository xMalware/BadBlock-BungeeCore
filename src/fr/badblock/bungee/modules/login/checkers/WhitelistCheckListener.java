package fr.badblock.bungee.modules.login.checkers;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to check if the player is already connected to
 * the server.
 * 
 * @author xMalware
 *
 */
public class WhitelistCheckListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();

		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();

		// Get the database
		DB db = mongoService.getDb();
		// Get the collection
		DBCollection collection = db.getCollection("whitelist");
		// Create query
		DBObject query = new BasicDBObject();

		// Get results
		DBCursor cursor = collection.find(query);

		try
		{
			if (!cursor.hasNext())
			{
				return;
			}

			// Create checker
			DBObject checker = new BasicDBObject();

			checker.put("name", badPlayer.getName().toLowerCase());

			cursor = collection.find(checker);

			if (!cursor.hasNext())
			{
				event.cancel(badPlayer.getTranslatedMessage("bungee.whitelist.notinwhitelist", null));
			}

		}
		catch (Exception error)
		{
			error.printStackTrace();
		}

		// Close the cursor
		cursor.close();
	}

}