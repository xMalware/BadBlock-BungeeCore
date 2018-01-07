package fr.badblock.bungee._plugins.objects.friendlist;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;

public class FriendListManager {
    public static synchronized FriendList getFriendList(String player) {
        BasicDBObject query = new BasicDBObject();
        query.append("owner", player.toLowerCase());
        return new FriendList(new SynchroMongoDBGetter("parties", query).getDbObject());
    }
}
