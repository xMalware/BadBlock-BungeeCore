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
import lombok.Getter;

/**
 * Main class for FriendLists
 *
 * @author RedSpri
 */
public class FriendListManager {
    private static final String COLLECTION = "friendlist";
    @Getter
    private static FriendListMessage message = new FriendListMessage();

    private static synchronized FriendList getFriendList(String player) {
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

    static void update(FriendList friendlist) {
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

    private static void insert(FriendList friendList) {
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

    public static void showStatusSelector(BadPlayer player) {
        message.QUERY_SELECTOR(player);
    }

    public static void setQueryable(BadPlayer player, String status) {
        FriendListable friendListable = FriendListable.getByString(status);
        if (friendListable == null) message.UNKNOWN_STATUS(player);
        else {
            if (player.getSettings().getFriendListable() == friendListable) {
                if (player.getSettings().getFriendListable() == FriendListable.YES) message.ALREADY_ACCEPT(player);
                else message.ALREADY_REFUSE(player);
            } else {
                player.getSettings().setFriendListable(friendListable);
                if (player.getSettings().getFriendListable() == FriendListable.YES) message.NOW_ACCEPT(player);
                else message.NOW_REFUSE(player);
            }
        }
    }

    public static void request(String want, String wanted) {
        FriendList wantedFriendList = getFriendList(wanted);
        FriendList wantFriendList = getFriendList(want);
        BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
        BadPlayer wantedBadPlayer = BungeeManager.getInstance().getBadPlayer(wanted);
        if (want.equals(wanted)) {
            message.SCHIZOPHRENIA_IS_BAD(wantBadPlayer);
            return;
        }
        if (wantedFriendList.isInList(want)) {
            if (wantedFriendList.isFriend(want)) message.ALREADY_FRIEND(wantBadPlayer, wantBadPlayer);
            else {
                if (wantedFriendList.wantToBeFriendWith(want)) {
                    wantedFriendList.accept(want);
                    message.REQUESTED_ACCEPT(wantedBadPlayer, wantBadPlayer);
                    wantFriendList.accept(wanted);
                    message.ACCEPT_REQUESTER(wantBadPlayer, wantedBadPlayer);
                } else {
                    if (wantedFriendList.alreadyRequestedBy(want)) message.ALREADY_REQUESTED(wantBadPlayer);
                    else message.ERROR(wantBadPlayer);
                }
            }
        } else {
            if (wantedBadPlayer.getSettings().getFriendListable() == FriendListable.YES) {
                wantedFriendList.request(want);
                message.REQUEST(wantedBadPlayer, wantBadPlayer);
                wantFriendList.requested(wanted);
                message.REQUEST_RECEIVED(wantBadPlayer, wantedBadPlayer);
            } else {
                message.DONT_ACCEPT_REQUESTS(wantBadPlayer, wantedBadPlayer);
            }
        }
    }

    public static void remove(String want, String wanted) {
        FriendList wantedFriendList = getFriendList(wanted);
        FriendList wantFriendList = getFriendList(want);
        BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
        BadPlayer wantedBadPlayer = BungeeManager.getInstance().getBadPlayer(wanted);
        if (want.equals(wanted)) {
            message.SCHIZOPHRENIA_IS_BAD(wantBadPlayer);
            return;
        }
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
                        wantFriendList.remove(wanted);
                        //TODO send "You cancel [wanted] friend request" to want
                        wantedFriendList.remove(want);
                        //TODO send "[want] cancelled his friend request" to wanted
                    } else message.ERROR(wantBadPlayer);

                }
            }
        } else {
            //TODO send "You are not friend with or you haven't requested [wanted]" to want
        }
    }


}
