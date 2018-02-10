package fr.badblock.bungee._plugins.objects.friendlist;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListRequestEvent;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListRequestEvent.FriendListRequestStatus;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListableChangeEvent;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

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
            FriendListableChangeEvent e = new FriendListableChangeEvent(player, player.getSettings().getFriendListable(), friendListable);
            e = ProxyServer.getInstance().getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                message.OPERATION_CANCELLED(player, e.getCancelReason());
            } else {
                if (e.getOldStatus() == e.getNewStatus()) {
                    if (e.getNewStatus() == FriendListable.YES) message.ALREADY_ACCEPT(player);
                    else message.ALREADY_REFUSE(player);
                } else {
                    player.getSettings().setFriendListable(e.getNewStatus());
                    if (e.getNewStatus() == FriendListable.YES) message.NOW_ACCEPT(player);
                    else message.NOW_REFUSE(player);
                }
            }
        }
    }

    public static void request(String want, String wanted) {
        FriendList wantedFriendList = getFriendList(wanted);
        FriendList wantFriendList = getFriendList(want);
        BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
        BadPlayer wantedBadPlayer = BungeeManager.getInstance().getBadPlayer(wanted);
        FriendListRequestStatus status;
        if (want.equals(wanted)) status = FriendListRequestStatus.PLAYER_SCHIZOPHRENIA;
        else {
            if (wantedFriendList.isInList(want)) {
                if (wantedFriendList.isFriend(want)) status = FriendListRequestStatus.PLAYERS_ALREADY_FRIENDS;
                else {
                    if (wantedFriendList.wantToBeFriendWith(want)) status = FriendListRequestStatus.PLAYERS_NOW_FRIENDS;
                    else {
                        if (wantedFriendList.alreadyRequestedBy(want))
                            status = FriendListRequestStatus.PLAYER_ALREADY_REQUESTED;
                        else status = FriendListRequestStatus.UNKNOWN_ERROR;
                    }
                }
            } else {
                if (wantedBadPlayer.getSettings().getFriendListable() == FriendListable.YES)
                    status = FriendListRequestStatus.PLAYER_RECEIVE_REQUEST;
                else status = FriendListRequestStatus.PLAYER_DONT_ACCEPT_REQUEST;
            }
        }
        FriendListRequestEvent e = new FriendListRequestEvent(wantBadPlayer, wantedBadPlayer, status);
        e = ProxyServer.getInstance().getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            message.OPERATION_CANCELLED(wantBadPlayer, e.getCancelReason());
        } else {
            switch (e.getStatus()) {
                case UNKNOWN_ERROR:
                    message.ERROR(wantBadPlayer);
                    break;
                case PLAYERS_NOW_FRIENDS:
                    wantedFriendList.accept(want);
                    message.REQUESTED_ACCEPT(wantedBadPlayer, wantBadPlayer);
                    wantFriendList.accept(wanted);
                    message.ACCEPT_REQUESTER(wantBadPlayer, wantedBadPlayer);
                    break;
                case PLAYER_SCHIZOPHRENIA:
                    message.SCHIZOPHRENIA_IS_BAD(wantBadPlayer);
                    break;
                case PLAYER_RECEIVE_REQUEST:
                    wantedFriendList.request(want);
                    message.REQUEST(wantedBadPlayer, wantBadPlayer);
                    wantFriendList.requested(wanted);
                    message.REQUEST_RECEIVED(wantBadPlayer, wantedBadPlayer);
                    break;
                case PLAYERS_ALREADY_FRIENDS:
                    message.ALREADY_FRIEND(wantBadPlayer, wantedBadPlayer);
                    break;
                case PLAYER_ALREADY_REQUESTED:
                    message.ALREADY_REQUESTED(wantBadPlayer);
                    break;
                case PLAYER_DONT_ACCEPT_REQUEST:
                    message.DONT_ACCEPT_REQUESTS(wantBadPlayer, wantedBadPlayer);
                default:
                    message.ERROR(wantBadPlayer);
                    break;
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
