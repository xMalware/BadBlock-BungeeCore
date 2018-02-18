package fr.badblock.bungee.utils.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Synchronized way to get a mongodb object
 *
 * @author RedSpri
 */
public class SynchroMongoDBGetter {
    private DBObject dbObject = null;
    private String collectionName;
    private BasicDBObject query;

    BasicDBObject getQuery() {
        return query;
    }

    public SynchroMongoDBGetter(String collectionname, BasicDBObject query) {
        this.collectionName = collectionname;
        this.query = query;
    }

    String getCollectionName() {
        return collectionName;
    }

    /**
     * Start a thread if the value is not setted and wait the thread's answer
     * if the object is already finded, return it.
     *
     * @see fr.badblock.bungee.utils.mongodb.GetterThread
     * @return the object retrieved by the thread: the DBObject if it was found, or null otherwise
     */
    public synchronized DBObject getDbObject() {
        if (dbObject == null) new GetterThread(this).start();
        try {
            wait(); //put the thread to sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dbObject;
    }

    /**
     * Clear the current getted value. So when the SynchroMongoDBGetter#getDbObject()
     * method is called, the object will be retrieved from the database.
     */
    public void clearCache() {
        dbObject = null;
    }

    synchronized void setDbObject(DBObject dbObject) {
        this.dbObject = dbObject;
        notify(); //wake up the thread
    }
}
