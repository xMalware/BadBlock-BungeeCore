package fr.badblock.bungee.modules.commands.modo.objects;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.bungee.BadBungee;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Ban Reasons
 * 
 * @author xMalware
 *
 */
@Getter
public class PunishmentReasons {

	/**
	 * Instance
	 * 
	 * @param Set
	 *            the new instance
	 * @return Returns the current instance
	 */
	@Getter
	@Setter
	private static PunishmentReasons instance = new PunishmentReasons();

	/**
	 * Ban reasons
	 * 
	 * @param Set
	 *            the new ban reasons
	 * @return Returns the current ban reasons
	 */
	private Map<String, PunishmentReason> banReasons;

	/**
	 * Mute reasons
	 * 
	 * @param Set
	 *            the new mute reasons
	 * @return Returns the current mute reasons
	 */
	private Map<String, PunishmentReason> muteReasons;

	/**
	 * Constructor
	 */
	public PunishmentReasons() {
		// Create a map!
		banReasons = new HashMap<>();
		// Load
		load();
	}

	public void load()
	{
		// Get the mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get database collection
		DBCollection dbCollection = mongoService.getDb().getCollection("punishTable");
		loadBans(dbCollection);
		loadMutes(dbCollection);
	}
	
	/**
	 * Load bans
	 */
	public void loadBans(DBCollection dbCollection) {
		// Create database query
		DBObject query = new BasicDBObject();
		// Add ban type
		query.put("type", "ban");
		// Get data
		DBCursor cursor = dbCollection.find(query);
		// If there's data
		while (cursor.hasNext()) {
			// Get the database object
			DBObject dbObject = cursor.next();
			// Get table
			BasicDBList dbList = (BasicDBList) dbObject.get("table");
			// Get database array
			BasicDBObject[] dbArray = dbList.toArray(new BasicDBObject[] {});
			// For each ban object
			for (BasicDBObject banObject : dbArray) {
				// Create a ban reason
				PunishmentReason banReason = new PunishmentReason(banObject);
				// Add ban reasons
				banReasons.put(banReason.getName(), banReason);
			}
		}
		// Close the cursor
		cursor.close();
	}
	
	/**
	 * Load mutes
	 */
	public void loadMutes(DBCollection dbCollection) {
		// Create database query
		DBObject query = new BasicDBObject();
		// Add ban type
		query.put("type", "mute");
		// Get data
		DBCursor cursor = dbCollection.find(query);
		// If there's data
		while (cursor.hasNext()) {
			// Get the database object
			DBObject dbObject = cursor.next();
			// Get table
			BasicDBList dbList = (BasicDBList) dbObject.get("table");
			// Get database array
			BasicDBObject[] dbArray = dbList.toArray(new BasicDBObject[] {});
			// For each mute object
			for (BasicDBObject banObject : dbArray) {
				// Create a mute reason
				PunishmentReason banReason = new PunishmentReason(banObject);
				// Add mute reasons
				muteReasons.put(banReason.getName(), banReason);
			}
		}
		// Close the cursor
		cursor.close();
	}

}
