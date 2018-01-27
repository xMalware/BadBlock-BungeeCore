package fr.badblock.bungee._plugins.objects.friendlist;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;

public class FriendListManager
{
    private static FriendListMessage message = new FriendListMessage();
	
    public static final String COLLECTION = "friendlist";
    
    public static synchronized FriendList getFriendList(String player)
    {
        BasicDBObject query = new BasicDBObject();
        query.append(FriendList.OWNER, player.toLowerCase());
        DBObject obj = new SynchroMongoDBGetter(COLLECTION, query).getDbObject();
        if (obj != null) return new FriendList(obj);
        else {
            FriendList f = new FriendList(player);
            insert(f);
            return f;
        }
    }

    public static void update(FriendList friendlist)
    {
        MongoService mongoService = BadBungee.getInstance().getMongoService();
        mongoService.useAsyncMongo(new MongoMethod(mongoService)
        {
            @Override
            public void run(MongoService mongoService)
            {
                DB db = mongoService.getDb();
                DBCollection collection = db.getCollection(COLLECTION);
                BasicDBObject query = new BasicDBObject();
                query.put(FriendList.OWNER, friendlist.getOwner());
                collection.update(query, friendlist.toObject());
            }
        });
    }

    public static void insert(FriendList friendList)
    {
        MongoService mongoService = BadBungee.getInstance().getMongoService();
        mongoService.useAsyncMongo(new MongoMethod(mongoService)
        {
            @Override
            public void run(MongoService mongoService)
            {
                DB db = mongoService.getDb();
                DBCollection collection = db.getCollection(COLLECTION);
                collection.insert(friendList.toObject());
            }
        });
    }

    public static boolean isFriends(String player, String otherplayer) {
        return getFriendList(player).isFriend(otherplayer);
    }

    public static void setQueryable(String player, boolean status) {
        FriendList friendList = getFriendList(player);
        BadPlayer badPlayer = BungeeManager.getInstance().getBadPlayer(player);
        if (friendList.isQueryable() == status) {
            if (status) {
                //TODO send "You already accept friend requests" to player
            } else {
                //TODO send "You already refuse friend requests" to player
            }
        } else {
            friendList.setQueryable(status);
            if (status) {
                //TODO send "You now accept friend requests" to player
            } else {
                //TODO send "You now refuse friend requests" to player
            }
        }
    }

    public static void request(String want, String wanted) {
        FriendList wantedFriendList = getFriendList(wanted);
        FriendList wantFriendList = getFriendList(want);
        BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
        BadPlayer wantedBadPlayer = BungeeManager.getInstance().getBadPlayer(wanted);
        if (wantedFriendList.isInList(want)) {
            if (wantedFriendList.isFriend(want)) {
                //TODO send "You are already friend with [wanted]" to want
            } else {
                if (wantedFriendList.wantToBeFriendWith(want)) {
                    wantedFriendList.accept(want);
                    //TODO send "[want] accepted your friend request. You are now friend with [want]" to wanted
                    wantFriendList.accept(wanted);
                    //TODO send "You accepted [wanted]'s friend request. "You are now friend with [wanted]" to want
                } else {
                    if (wantedFriendList.alreadyRequestedBy(want)) {
                        //TODO send "You already requested [wanted]" to want
                    } else {
                        //TODO send "Unknown error" to want
                    }
                }
            }
        } else {
            if (wantedFriendList.isQueryable()) {
                wantedFriendList.request(want);
                //TODO send "[want] want to be your friend" to wanted
                wantFriendList.requested(wanted);
                //TODO send "[wanted] received your friend request" to want
            } else {
                //TODO send "[wanted] doesn't accept friend requests." to want
            }
        }
    }

    public static void remove(String want, String wanted) {
        FriendList wantedFriendList = getFriendList(wanted);
        FriendList wantFriendList = getFriendList(want);
        BadPlayer wantPlayer = BungeeManager.getInstance().getBadPlayer(want);
        BadPlayer wantedPlayer = BungeeManager.getInstance().getBadPlayer(wanted);
        if (wantedFriendList.isInList(want) || wantFriendList.isInList(wanted)) {
            if (wantedFriendList.isFriend(want) || wantFriendList.isFriend(wanted)) {
                wantedFriendList.remove(want);
                //TODO send "[want] removed you from his friend list." to wanted
                wantFriendList.remove(wanted);
                //TODO send "You are no longer friend with [wanted]." to want
            } else {
                if (wantedFriendList.wantToBeFriendWith(want)) {
                    wantedFriendList.remove(want);
                    //TODO send "[want] declined your friend request" to wanted
                    wantFriendList.remove(wanted);
                    //TODO send "You decline [wanted] friend request" to want
                } else {
                    if (wantedFriendList.alreadyRequestedBy(want)) {

                    }
                }
            }
        } else {
            //TODO send "You are not friend with or you haven't requested [wanted]" to want
        }
    }


}
