package fr.badblock.bungee._plugins.objects.friendlist;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListRemoveEvent;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListRemoveEvent.FriendListRemoveStatus;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListRequestEvent;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListRequestEvent.FriendListRequestStatus;
import fr.badblock.bungee.api.events.objects.friendlist.FriendListableChangeEvent;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.Filter;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Main class for FriendLists
 *
 * @author RedSpri
 */
public final class FriendListManager {
    private static final String COLLECTION = "friendlist";
    @Getter
    private static FriendListMessage message = new FriendListMessage();

    private static synchronized FriendList getFriendList(UUID player) {
        BasicDBObject query = new BasicDBObject();
        query.append(FriendList.OWNER, player.toString());
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
                    player.updateSettings();
                    if (e.getNewStatus() == FriendListable.YES) message.NOW_ACCEPT(player);
                    else message.NOW_REFUSE(player);
                }
            }
        }
    }

    public static void request(String want, String wanted) {
        BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
        BadOfflinePlayer wantedBadPlayer = BungeeManager.getInstance().getBadOfflinePlayer(wanted);
        if (wantedBadPlayer == null) {
            message.UNKNOWN_PLAYER(wantBadPlayer, wanted);
            return;
        }
        FriendList wantedFriendList = getFriendList(wantBadPlayer.getUniqueId());
        FriendList wantFriendList = getFriendList(wantedBadPlayer.getUniqueId());
        FriendListRequestStatus status;
        if (want.equals(wanted)) status = FriendListRequestStatus.PLAYER_SCHIZOPHRENIA;
        else {
            if (wantedFriendList.isInList(wantBadPlayer.getUniqueId())) {
                if (wantedFriendList.isFriend(wantBadPlayer.getUniqueId()))
                    status = FriendListRequestStatus.PLAYERS_ALREADY_FRIENDS;
                else {
                    if (wantedFriendList.wantToBeFriendWith(wantBadPlayer.getUniqueId()))
                        status = FriendListRequestStatus.PLAYERS_NOW_FRIENDS;
                    else {
                        if (wantedFriendList.alreadyRequestedBy(wantBadPlayer.getUniqueId()))
                            status = FriendListRequestStatus.PLAYER_ALREADY_REQUESTED;
                        else status = FriendListRequestStatus.UNKNOWN_ERROR;
                    }
                }
            } else {
                if (wantedBadPlayer.getSettings().getFriendListable() == FriendListable.YES)
                    status = FriendListRequestStatus.PLAYER_RECEIVE_REQUEST;
                else status = FriendListRequestStatus.PLAYER_DO_NOT_ACCEPT_REQUEST;
            }
        }
        FriendListRequestEvent e = new FriendListRequestEvent(wantBadPlayer, wantedBadPlayer, status);
        e = ProxyServer.getInstance().getPluginManager().callEvent(e);
        if (e.isCancelled()) message.OPERATION_CANCELLED(wantBadPlayer, e.getCancelReason());
        else {
            switch (e.getStatus()) {
                case UNKNOWN_ERROR:
                    message.ERROR(wantBadPlayer);
                    break;
                case PLAYERS_NOW_FRIENDS:
                    wantedFriendList.accept(wantBadPlayer.getUniqueId());
                    message.REQUESTED_ACCEPT(wantedBadPlayer, wantBadPlayer);
                    wantFriendList.accept(wantedBadPlayer.getUniqueId());
                    message.ACCEPT_REQUESTER(wantBadPlayer, wantedBadPlayer);
                    break;
                case PLAYER_SCHIZOPHRENIA:
                    message.SCHIZOPHRENIA_IS_BAD(wantBadPlayer);
                    break;
                case PLAYER_RECEIVE_REQUEST:
                    wantedFriendList.request(wantBadPlayer.getUniqueId());
                    message.REQUEST(wantedBadPlayer, wantBadPlayer);
                    wantFriendList.requested(wantedBadPlayer.getUniqueId());
                    message.REQUEST_RECEIVED(wantBadPlayer, wantedBadPlayer);
                    break;
                case PLAYERS_ALREADY_FRIENDS:
                    message.ALREADY_FRIEND(wantBadPlayer, wantedBadPlayer);
                    break;
                case PLAYER_ALREADY_REQUESTED:
                    message.ALREADY_REQUESTED(wantBadPlayer);
                    break;
                case PLAYER_DO_NOT_ACCEPT_REQUEST:
                    message.DO_NOT_ACCEPT_REQUESTS(wantBadPlayer, wantedBadPlayer);
                default:
                    message.ERROR(wantBadPlayer);
                    break;
            }
        }
    }

    public static void remove(String want, String wanted) {
        BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
        BadOfflinePlayer wantedBadPlayer = BungeeManager.getInstance().getBadOfflinePlayer(wanted);
        if (wantedBadPlayer == null) {
            message.UNKNOWN_PLAYER(wantBadPlayer, wanted);
            return;
        }
        FriendList wantedFriendList = getFriendList(wantedBadPlayer.getUniqueId());
        FriendList wantFriendList = getFriendList(wantBadPlayer.getUniqueId());
        FriendListRemoveStatus status;
        if (want.equals(wanted)) status = FriendListRemoveStatus.PLAYER_SCHIZOPHRENIA;
        else {
            if (wantedFriendList.isInList(wantBadPlayer.getUniqueId()) || wantFriendList.isInList(wantedBadPlayer.getUniqueId())) {
                if (wantedFriendList.isFriend(wantBadPlayer.getUniqueId()) || wantFriendList.isFriend(wantedBadPlayer.getUniqueId()))
                    status = FriendListRemoveStatus.PLAYER_REMOVED_FROM_LIST;
                else {
                    if (wantedFriendList.wantToBeFriendWith(wantBadPlayer.getUniqueId()))
                        status = FriendListRemoveStatus.PLAYER_REQUEST_DECLINED;
                    else {
                        if (wantedFriendList.alreadyRequestedBy(wantBadPlayer.getUniqueId()))
                            status = FriendListRemoveStatus.REQUEST_TO_PLAYER_CANCELLED;
                        else status = FriendListRemoveStatus.UNKNOWN_ERROR;
                    }
                }
            } else status = FriendListRemoveStatus.NOT_REQUESTED_OR_FRIEND_WITH_PLAYER;
        }
        FriendListRemoveEvent e = new FriendListRemoveEvent(wantBadPlayer, wantedBadPlayer, status);
        e = ProxyServer.getInstance().getPluginManager().callEvent(e);
        if (e.isCancelled()) message.OPERATION_CANCELLED(wantBadPlayer, e.getCancelReason());
        else {
            switch (e.getStatus()) {
                case UNKNOWN_ERROR:
                    message.ERROR(wantBadPlayer);
                    break;
                case PLAYER_SCHIZOPHRENIA:
                    message.SCHIZOPHRENIA_IS_BAD(wantBadPlayer);
                    break;
                case PLAYER_REQUEST_DECLINED:
                    wantedFriendList.remove(wantBadPlayer.getUniqueId());
                    message.DECLINED_YOUR_REQUEST(wantedBadPlayer, wantBadPlayer);
                    wantFriendList.remove(wantedBadPlayer.getUniqueId());
                    message.REJECT_REQUEST_OF(wantBadPlayer, wantedBadPlayer);
                    break;
                case PLAYER_REMOVED_FROM_LIST:
                    wantedFriendList.remove(wantBadPlayer.getUniqueId());
                    message.REMOVED_YOU_FROM_FRIENDS(wantedBadPlayer, wantBadPlayer);
                    wantFriendList.remove(wantedBadPlayer.getUniqueId());
                    message.NOW_NO_LONGER_FRIEND(wantBadPlayer, wantedBadPlayer);
                    break;
                case REQUEST_TO_PLAYER_CANCELLED:
                    wantFriendList.remove(wantedBadPlayer.getUniqueId());
                    message.CANCEL_REQUEST_TO(wantBadPlayer, wantedBadPlayer);
                    wantedFriendList.remove(wantBadPlayer.getUniqueId());
                    message.CANCELLED_REQUEST(wantedBadPlayer, wantBadPlayer);
                    break;
                case NOT_REQUESTED_OR_FRIEND_WITH_PLAYER:
                    message.NO_RELATIONSHIP(wantBadPlayer, wantedBadPlayer);
                    break;
                default:
                    message.ERROR(wantBadPlayer);
                    break;
            }
        }
    }


    public static void showFriendList(BadPlayer p) {
        showFriendList(p, "1");
    }

    public static List<BadPlayer> getFriends(BadPlayer badPlayer) {
        Map<UUID, FriendListPlayer> players = getFriendList(badPlayer.getUniqueId()).getPlayers();
        List<BadPlayer> friends = new ArrayList<>();
        Filter.filterSetStatic(e -> players.get(e).getState() == FriendListPlayerState.ACCEPTED, players.keySet()).forEach(e -> friends.add(BungeeManager.getInstance().getBadPlayer(e)));
        return friends;
    }

    public static void showFriendList(BadPlayer badPlayer, String page) {
        int i;
        try {
            i = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            message.INCORRECT_PAGE_NUMBER(badPlayer, page);
            return;
        }
        List<BadPlayer> friends = getFriends(badPlayer);
        int pages = friends.size() / 10;
        if (pages > 1 && i > pages) {
            message.TOO_BIG_PAGE_NUMBER(badPlayer, i);
        } else {
            //TODO header
            for (int l = (i - 1) * 10; l < i * 10; l++) {
                if (friends.size() - 1 <= l) break;
                BadPlayer friend = friends.get(l);
                //TODO friend list value
            }
            //TODO footer
        }
    }


}
