package fr.badblock.bungee.modules.login.datamanager;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.commands.modo.objects.ModoSession;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class ModoSessionStopListener extends BadListener {

	/**
	 * When the player disconnects from the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		// We get the player
		ProxiedPlayer player = event.getPlayer();
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(player);

		// If the BadPlayer object is null
		if (badPlayer == null) {
			// So we stop there
			return;
		}

		// Get the modo session
		ModoSession modoSession = badPlayer.getModoSession();

		// If the modo session object is null
		if (modoSession == null) {
			// So we stop there
			return;
		}

		long time = System.currentTimeMillis();

		modoSession.setEndTime(time);
		modoSession.setTimestamp(time);

		long punishmentTime = modoSession.getPunishments() * (60 * 5);
		if (punishmentTime > modoSession.getTotalTime()) {
			punishmentTime = modoSession.getTotalTime();
		}

		modoSession.setPunishmentTime(punishmentTime);
		modoSession.setTotalTime((modoSession.getEndTime() - modoSession.getStartTime()) / 1000);

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
				DBCollection collection = db.getCollection("modoSessions");
				// Create query
				DBObject query = modoSession.toDatabaseObject();

				collection.insert(query);
			}
		});
	}

}
