package fr.badblock.bungee.utils.mongodb;

import com.mongodb.*;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee._plugins.objects.party.Party;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;

class GetterThread extends Thread {
    SynchroMongoDBGetter getter;
    public GetterThread(SynchroMongoDBGetter synchroMongoDBGetter) {
        this.getter = synchroMongoDBGetter;
    }

    @Override
    public void run() {
        super.run();
        MongoService mongoService = BadBungee.getInstance().getMongoService();
        mongoService.useAsyncMongo(new MongoMethod(mongoService) {
            @Override
            public void run(MongoService mongoService) {
                DB db = mongoService.getDb();
                DBCursor cursor = getter.getCollection().find(getter.getQuery());
                if (cursor != null && cursor.hasNext()) {
                    DBObject dbObject = cursor.next();
                    getter.setDbObject(dbObject);
                }
                else getter.setDbObject(null);
            }
        });
    }
}
