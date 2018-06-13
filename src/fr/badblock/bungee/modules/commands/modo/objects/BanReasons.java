package fr.badblock.bungee.modules.commands.modo.objects;

import java.util.HashMap;
import java.util.Map;

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
 * 
 * 
 * @author root
 *
 */
public class BanReasons
{

	@Getter @Setter
	private static BanReasons instance = new BanReasons();
	
	private Map<String, BanReason>	banReasons;
	
	public BanReasons()
	{
		banReasons = new HashMap<>();
		load();
	}
	
	public void load()
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get database collection
		DBCollection dbCollection = mongoService.getDb().getCollection("players");
		// Create database query
		DBObject query = new BasicDBObject();
		// Get data
		DBCursor cursor = dbCollection.find(query);
		// If there's data
		while (cursor.hasNext()) {
			// Get the database object
			DBObject dbObject = cursor.next();
			// Create a ban reason
			BanReason banReason = new BanReason(dbObject);
			// Add ban reasons
			banReasons.put(banReason.getName(), banReason);
		}
		// Close the cursor
		cursor.close();
	}
	
}
