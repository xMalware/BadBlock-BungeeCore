package fr.badblock.bungee.link.bungee.tasks;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.DateUtils;

public class PeakTask extends Thread
{

	public static int	totalPeak;
	public static int	todayPeak;

	public PeakTask()
	{
		start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			int onlinePlayers = BungeeManager.getInstance().getRealTimeOnlinePlayers();
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
					DBCollection collection = db.getCollection("customData");
					// Create query
					DBObject query = new BasicDBObject();

					query.put("dataIdentifier", "peak");

					// Get results
					DBCursor cursor = collection.find(query);

					try
					{
						while (cursor.hasNext())
						{
							BasicDBObject data = (BasicDBObject) cursor.next();

							totalPeak = data.getInt("allTimePeak");

							if (onlinePlayers > totalPeak)
							{
								// Update query
								BasicDBObject update = new BasicDBObject("allTimePeak", onlinePlayers);

								// Set the updater as a setter
								BasicDBObject updater = new BasicDBObject("$set", update);

								// Update the collection with the query & updater
								collection.update(query, updater);
								
								totalPeak = onlinePlayers;
							}
							
							todayPeak = data.getInt("todayPeak");
							String date = data.getString("date");
							
							String currentDate = DateUtils.getDate();
							
							if (!date.equalsIgnoreCase(currentDate) || onlinePlayers > todayPeak)
							{
								// Update query
								BasicDBObject update = new BasicDBObject();
								
								update.put("todayPeak", onlinePlayers);
								update.put("date", currentDate);

								// Set the updater as a setter
								BasicDBObject updater = new BasicDBObject("$set", update);

								// Update the collection with the query & updater
								collection.update(query, updater);
								
								todayPeak = onlinePlayers;
							}
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
			TimeUtils.sleepInSeconds(60);
		}
	}

}
