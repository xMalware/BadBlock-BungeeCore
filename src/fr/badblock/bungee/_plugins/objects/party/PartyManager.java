package fr.badblock.bungee._plugins.objects.party;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.bungee.BadBungee;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import fr.toenga.common.utils.data.Callback;

public class PartyManager
{

	public static void getParty(String player, Callback<Party> callback)
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("parties");
				BasicDBObject query = new BasicDBObject();
				query.append("players", player.toLowerCase());
				DBCursor cursor = collection.find(query);
				if (cursor != null && cursor.hasNext())
				{
					DBObject dbObject = cursor.next();
					callback.done(new Party(dbObject), null);
				}
				else
				{
					callback.done(null, null);
				}
			}
		});
	}

	public static void inGroup(String player, Callback<Boolean> callback)
	{
		player = player.toLowerCase();
		getParty(player, new Callback<Party>()
		{

			@Override
			public void done(Party result, Throwable error) {
				callback.done(result != null, null);
			}

		});
	}

	public static void update(Party party)
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("parties");
				BasicDBObject query = new BasicDBObject();

				query.put("_id", party.getUuid());

				collection.update(query, party.toObject());
			}
		});
	}

	public static void insert(Party party)
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("parties");
				collection.insert(party.toObject());
			}
		});
	}

}
