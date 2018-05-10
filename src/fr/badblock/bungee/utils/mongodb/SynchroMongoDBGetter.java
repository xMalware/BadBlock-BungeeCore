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
	
    private DBObject 			dbObject				= null;
    private String 				collectionName;
    private BasicDBObject		query;
    private boolean				done;
    private	Thread				thread;

    public SynchroMongoDBGetter(String collectionname, BasicDBObject query)
    {
        this.collectionName = collectionname;
        this.query = query;
        this.thread = Thread.currentThread();
    }
    
    /**
     * Start a thread if the value is not setted and wait the thread's answer
     * if the object is already finded, return it.
     *
     * @see fr.badblock.bungee.utils.mongodb.GetterThread
     * @return the object retrieved by the thread: the DBObject if it was found, or null otherwise
     */
    public DBObject getDbObject()
    {
        if (dbObject == null) new GetterThread(this).start();
        synchronized (getThread())
        {
        	try
        	{
        		getThread().wait();
			}
        	catch (InterruptedException exception)
        	{
				exception.printStackTrace();
			}
        }
        return dbObject;
    }

    /**
     * Clear the current getted value. So when the SynchroMongoDBGetter#getDbObject()
     * method is called, the object will be retrieved from the database.
     */
    public void clearCache()
    {
        dbObject = null;
    }

    void setDbObject(DBObject dbObject)
    {
        this.dbObject = dbObject;
        synchronized (getThread())
        {
        	getThread().notify();
        }
    }
    
}
