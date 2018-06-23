package fr.badblock.bungee.modules.login.datamanager;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.datamanager.connectionlogs.ConnectionLog;
import fr.badblock.bungee.modules.login.events.PlayerLoggedEvent;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class ConnectionLogListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogged(PlayerLoggedEvent event) {
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		// We get his/her username
		String username = badPlayer.getName();
		String lastIp = badPlayer.getLastIp();
		String date = DateUtils.getHourDate();
		long timestamp = System.currentTimeMillis();

		ConnectionLog connectionLog = new ConnectionLog(username, lastIp, date, timestamp);

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
				DBCollection collection = db.getCollection("connectionLogs");

				// Create query
				DBObject query = connectionLog.toDatabaseObject();

				// Insert
				collection.insert(query);
			}
		});
	}

}
