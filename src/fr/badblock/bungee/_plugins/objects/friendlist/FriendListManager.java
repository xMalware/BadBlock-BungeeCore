package fr.badblock.bungee._plugins.objects.friendlist;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee._plugins.objects.party.Party;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;

public class FriendListManager {
    public static final String COLLECTION = "friendlists";
    public static synchronized FriendList getFriendList(String player) {
        BasicDBObject query = new BasicDBObject();
        query.append(FriendList.OWNER, player.toLowerCase());
        return new FriendList(new SynchroMongoDBGetter(COLLECTION, query).getDbObject());
    }

    public static void update(FriendList friendlist) {
        MongoService mongoService = BadBungee.getInstance().getMongoService();
        mongoService.useAsyncMongo(new MongoMethod(mongoService) {
            @Override
            public void run(MongoService mongoService) {
                DB db = mongoService.getDb();
                DBCollection collection = db.getCollection(COLLECTION);
                BasicDBObject query = new BasicDBObject();
                query.put(FriendList.OWNER, friendlist.getOwner());
                collection.update(query, friendlist.toObject());
            }
        });
    }

    public static void insert(Party party) {
        MongoService mongoService = BadBungee.getInstance().getMongoService();
        mongoService.useAsyncMongo(new MongoMethod(mongoService) {
            @Override
            public void run(MongoService mongoService) {
                DB db = mongoService.getDb();
                DBCollection collection = db.getCollection(COLLECTION);
                collection.insert(party.toObject());
            }
        });
    }
}
