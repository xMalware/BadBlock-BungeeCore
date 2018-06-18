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
public class BanReasons {

	/**
	 * Instance
	 * 
	 * @param Set
	 *            the new instance
	 * @return Returns the current instance
	 */
	@Getter
	@Setter
	private static BanReasons instance = new BanReasons();

	/**
	 * Ban reasons
	 * 
	 * @param Set
	 *            the new ban reasons
	 * @return Returns the current ban reasons
	 */
	private Map<String, BanReason> banReasons;

	/**
	 * Constructor
	 */
	public BanReasons() {
		// Create a map!
		banReasons = new HashMap<>();
		// Load
		load();
	}

	/**
	 * Load
	 */
	public void load() {
		// Get the mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get database collection
		DBCollection dbCollection = mongoService.getDb().getCollection("punishTable");
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
				BanReason banReason = new BanReason(banObject);
				// Add ban reasons
				banReasons.put(banReason.getName(), banReason);
			}
		}
		// Close the cursor
		cursor.close();
	}

}
