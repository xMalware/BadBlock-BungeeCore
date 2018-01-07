package fr.badblock.bungee.utils.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class SynchroMongoDBGetter {
    private DBObject dbObject = null;
    private DBCollection collection;
    private BasicDBObject query;

    public BasicDBObject getQuery() {
        return query;
    }

    public DBCollection getCollection() {
        return collection;
    }

    public SynchroMongoDBGetter(DBCollection collection, BasicDBObject query) {
        this.collection = collection;
        this.query = query;
    }

    public synchronized DBObject getDbObject() {
        if (dbObject == null) {
            new GetterThread(this).start();
        }
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dbObject;
    }

    public void clearCache() {
        dbObject = null;
    }

    synchronized void setDbObject(DBObject dbObject) {
        this.dbObject = dbObject;
        notify();
    }
}
