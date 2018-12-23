package fr.badblock.bungee.link.bungee.tasks;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.flags.GlobalFlags;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.bungee.MaintenanceObject;
import net.md_5.bungee.api.ServerPing;

public class PingUpdaterTask extends Thread {

	public PingUpdaterTask() {
		start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (true) {
			try
			{
				// Get main class
				BadBungee bungee = BadBungee.getInstance();
				// Bungee manager
				BungeeManager bungeeManager = BungeeManager.getInstance();
				// Get Mongo service by using the main class
				MongoService mongoService = bungee.getMongoService();
				// Get database object
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("serverInfo");
				// New query
				BasicDBObject query = new BasicDBObject();
				// Find all data with an empty query
				DBCursor cursor = collection.find(query);
				// For each data
				while (cursor.hasNext()) {
					// Get it
					BasicDBObject dbObject = (BasicDBObject) cursor.next();
					// Get deserialize object
					String json = dbObject.toString();
					// Serialize as a ServerPing object
					ServerPing serverPing = bungee.getGson().fromJson(json, ServerPing.class);
					// Maintenance
					BasicDBObject maintenanceObject = (BasicDBObject) dbObject.get("maintenance");
					MaintenanceObject maintenance = bungee.getGson().fromJson(maintenanceObject.toString(), MaintenanceObject.class);
					bungeeManager.setMaintenance(maintenance);
				
					if (maintenance != null && maintenance.isState())
					{
						// Replace {} to \n => escape lines
						serverPing.setDescription(maintenance.getDescription().replace("{}", "\n"));
					}
					else
					{
						// Replace {} to \n => escape lines
						serverPing.setDescription(serverPing.getDescription().replace("{}", "\n"));
					}
					
					// Set slots
					bungeeManager.setSlots(serverPing.getPlayers().getMax());
					// Add a flag for the server ping object to avoid spam
					GlobalFlags.set(serverPing, 1000);
					// Set online players
					int onlinePlayers = bungeeManager.getOnlinePlayers();
					serverPing.getPlayers().setOnline(onlinePlayers);
					// Returns the server ping object
					bungeeManager.setServerPing(serverPing);

					BasicDBObject queryUpdater = new BasicDBObject("_id", dbObject.getObjectId("_id"));

					BasicDBObject updater = (BasicDBObject) dbObject.get("players");
					updater.put("online", (double) onlinePlayers);
					dbObject.put("players", updater);

					collection.update(queryUpdater, dbObject);
				}
				cursor.close();
			}
			catch (Exception error)
			{
				BadBungee.log("§cError while updating server ping.");
				error.printStackTrace();
			}
			TimeUtils.sleepInSeconds(1);
		}
	}

}
