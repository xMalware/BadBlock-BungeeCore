package fr.badblock.bungee.utils.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;

/**
 * Local package class - Thread for get the MongoDB object synchronously
 * @see SynchroMongoDBGetter
 *
 * @author RedSpri
 */
class GetterThread extends Thread
{

	SynchroMongoDBGetter getter;

	public GetterThread(SynchroMongoDBGetter synchroMongoDBGetter)
	{
		this.getter = synchroMongoDBGetter;
	}

	@Override
	public void run()
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCursor cursor = db.getCollection(getter.getCollectionName()).find(getter.getQuery());
				if (cursor != null && cursor.hasNext())
				{
					DBObject dbObject = cursor.next();
					getter.setDbObject(dbObject);
				}
				else getter.setDbObject(null);
			}
		});
	}
	
}
