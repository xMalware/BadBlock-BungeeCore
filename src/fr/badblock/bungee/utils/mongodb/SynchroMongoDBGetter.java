package fr.badblock.bungee.utils.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.Data;

/**
 * Synchronized way to get a mongodb object
 *
 * @author RedSpri
 */
@Data
public class SynchroMongoDBGetter
{

	/**
	 * Database object
	 * @param Set the new database obect
	 * @return Returns the current database object
	 */
	private DBObject 			dbObject				= null;

	/**
	 * Collection name
	 * @param Set the new collection name
	 * @return Returns the current collection name
	 */
	private String 				collectionName;

	/**
	 * Query
	 * @param Set the new query
	 * @return Returns the current query
	 */
	private BasicDBObject		query;

	/**
	 * Done
	 * @param Set if it's get!
	 * @return Returns if it's get
	 */
	private boolean				done;

	/**
	 * Thread
	 * @param Set the new thread
	 * @return Returns the current thread
	 */
	private	Thread				thread;

	/**
	 * Constructor
	 * @param Collection nmae
	 * @param Query
	 */
	public SynchroMongoDBGetter(String collectionname, BasicDBObject query)
	{
		// Set the collection name
		this.collectionName = collectionname;
		// Set the query
		this.query = query;
		// Set the thread
		this.thread = Thread.currentThread();
	}

	/**
	 * Start a thread if the value is not set and wait the thread's answer
	 * if the object is already found, return it.
	 *
	 * @see fr.badblock.bungee.utils.mongodb.GetterThread
	 * @return the object retrieved by the thread: the DBObject if it was found, or null otherwise
	 */
	public DBObject getDbObject()
	{
		// If the database object is null
		if (dbObject == null)
		{
			// Create a new getter thread
			new GetterThread(this).start();
		}
		
		// Synchronized with the requested thread
		synchronized (getThread())
		{
			// Try to
			try
			{
				// Wait
				getThread().wait();
			}
			// In case we can't
			catch (InterruptedException exception)
			{
				// So print a stacktrace
				exception.printStackTrace();
			}
		}
		
		// Returns the database object
		return dbObject;
	}

	/**
	 * Clear the current getted value. So when the SynchroMongoDBGetter#getDbObject()
	 * method is called, the object will be retrieved from the database.
	 */
	public void clearCache()
	{
		// DbObject => null
		dbObject = null;
	}

	void setDbObject(DBObject dbObject)
	{
		// Set the database object
		this.dbObject = dbObject;
		
		// Synchronize with the thread
		synchronized (getThread())
		{
			// Notify it
			getThread().notify();
		}
	}

}