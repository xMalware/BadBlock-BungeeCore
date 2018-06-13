package fr.badblock.bungee.utils.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;

/**
 * Local package class - Thread to get the MongoDB object synchronously
 * 
 * @see SynchroMongoDBGetter
 *
 * @author RedSpri
 */
class GetterThread extends Thread {

	/**
	 * Getter
	 * 
	 * @param Set
	 *            the new getter
	 * @return Returns the current getter
	 */
	SynchroMongoDBGetter getter;

	/**
	 * Constructor of GetterThread class
	 * 
	 * @param synchroMongoDBGetter
	 */
	public GetterThread(SynchroMongoDBGetter synchroMongoDBGetter) {
		this.getter = synchroMongoDBGetter;
	}

	/**
	 * Run
	 */
	@Override
	public void run() {
		// Get Mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();

		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Run
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();

				// Get results
				DBCursor cursor = db.getCollection(getter.getCollectionName()).find(getter.getQuery());

				// If there is a result
				if (cursor != null && cursor.hasNext()) {
					// Get the database object
					DBObject dbObject = cursor.next();

					// Set the database object
					getter.setDbObject(dbObject);
				} else {
					// Set a null object
					getter.setDbObject(null);
				}
			}
		});
	}

}