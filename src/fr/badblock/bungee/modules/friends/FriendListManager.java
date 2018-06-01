package fr.badblock.bungee.modules.friends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.friends.events.FriendListRemoveEvent;
import fr.badblock.bungee.modules.friends.events.FriendListRequestEvent;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.Filter;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

/**
 * Main class for FriendLists
 *
 * @author RedSpri
 */
public final class FriendListManager
{

	private static final String COLLECTION = "friendlist";
	@Getter
	private static FriendListMessage message = new FriendListMessage();

	private static synchronized FriendList getFriendList(UUID player)
	{
		BasicDBObject query = new BasicDBObject();
		query.append(FriendList.OWNER, player.toString());
		DBObject obj = new SynchroMongoDBGetter(COLLECTION, query).getDbObject();
		if (obj != null)
		{
			return new FriendList(obj);
		}
		FriendList f = new FriendList(player);
		insert(f);
		return f;
	}

	static void update(FriendList friendlist)
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

	private static void insert(FriendList friendList)
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

	public static void showStatusSelector(BadPlayer player)
	{
		message.sendQuerySelector(player);
	}

	public static void setQueryable(BadPlayer player, String status)
	{
		FriendListable friendListable = FriendListable.getByString(status);
		if (friendListable == null)
		{
			message.sendUnknownStatus(player);
			return;
		}

		FriendListableChangeEvent event = new FriendListableChangeEvent(player, player.getSettings().getFriendListable(), friendListable);
		event = ProxyServer.getInstance().getPluginManager().callEvent(event);

		if (event.isCancelled())
		{
			message.sendOperationCancelled(player, event.getCancelReason());
			return;
		}

		if (event.getOldStatus() == event.getNewStatus())
		{
			if (event.getNewStatus() == FriendListable.YES)
			{
				message.sendAlreadyAccepted(player);
				return;
			}
			message.sendAlreadyRefused(player);
			return;
		}

		player.getSettings().setFriendListable(event.getNewStatus());
		player.updateSettings();

		if (event.getNewStatus() == FriendListable.YES)
		{
			message.sendNowAccepted(player);
		}
		else
		{
			message.sendNowRefused(player);
		}
	}

	public static void request(String want, String wanted)
	{
		BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
		BadOfflinePlayer wantedBadPlayer = BungeeManager.getInstance().getBadOfflinePlayer(wanted);

		if (wantedBadPlayer == null)
		{
			message.sendUnknownPlayer(wantBadPlayer, wanted);
			return;
		}

		FriendList wantedFriendList = getFriendList(wantBadPlayer.getUniqueId());
		FriendList wantFriendList = getFriendList(wantedBadPlayer.getUniqueId());
		FriendListRequestStatus status;

		if (want.equals(wanted))
		{
			status = FriendListRequestStatus.PLAYER_SCHIZOPHRENIA;
		}
		else
		{
			if (wantedFriendList.isInList(wantBadPlayer.getUniqueId()))
			{
				if (wantedFriendList.isFriend(wantBadPlayer.getUniqueId()))
				{
					status = FriendListRequestStatus.PLAYERS_ALREADY_FRIENDS;
				}
				else
				{
					if (wantedFriendList.wantToBeFriendWith(wantBadPlayer.getUniqueId()))
					{
						status = FriendListRequestStatus.PLAYERS_NOW_FRIENDS;
					}
					else
					{
						if (wantedFriendList.alreadyRequestedBy(wantBadPlayer.getUniqueId()))
						{
							status = FriendListRequestStatus.PLAYER_ALREADY_REQUESTED;
						}
						else
						{
							status = FriendListRequestStatus.UNKNOWN_ERROR;
						}
					}
				}
			}
			else
			{
				if (wantedBadPlayer.getSettings().getFriendListable() == FriendListable.YES)
				{
					status = FriendListRequestStatus.PLAYER_RECEIVE_REQUEST;
				}
				else
				{
					status = FriendListRequestStatus.PLAYER_DO_NOT_ACCEPT_REQUEST;
				}
			}
		}

		FriendListRequestEvent event = new FriendListRequestEvent(wantBadPlayer, wantedBadPlayer, status);
		event = ProxyServer.getInstance().getPluginManager().callEvent(event);

		if (event.isCancelled())
		{
			message.sendOperationCancelled(wantBadPlayer, event.getCancelReason());
			return;
		}

		switch (event.getStatus())
		{
		case UNKNOWN_ERROR:
			message.sendError(wantBadPlayer);
			break;

		case PLAYERS_NOW_FRIENDS:
			wantedFriendList.accept(wantBadPlayer.getUniqueId());
			message.sendRequestAccepted(wantedBadPlayer, wantBadPlayer);
			wantFriendList.accept(wantedBadPlayer.getUniqueId());
			message.sendAcceptRequester(wantBadPlayer, wantedBadPlayer);
			break;

		case PLAYER_SCHIZOPHRENIA:
			message.sendCantActOnYourself(wantBadPlayer);
			break;

		case PLAYER_RECEIVE_REQUEST:
			wantedFriendList.request(wantBadPlayer.getUniqueId());
			message.sendRequest(wantedBadPlayer, wantBadPlayer);
			wantFriendList.requested(wantedBadPlayer.getUniqueId());
			message.sendRequestReceived(wantBadPlayer, wantedBadPlayer);
			break;

		case PLAYERS_ALREADY_FRIENDS:
			message.sendAlreadyFriend(wantBadPlayer, wantedBadPlayer);
			break;

		case PLAYER_ALREADY_REQUESTED:
			message.sendAlreadyRequested(wantBadPlayer);
			break;

		case PLAYER_DO_NOT_ACCEPT_REQUEST:
			message.sendDoNotAcceptRequests(wantBadPlayer, wantedBadPlayer);

		default:
			message.sendError(wantBadPlayer);
			break;
		}
	}

	public static void remove(String want, String wanted)
	{
		BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
		BadOfflinePlayer wantedBadPlayer = BungeeManager.getInstance().getBadOfflinePlayer(wanted);

		if (wantedBadPlayer == null)
		{
			message.sendUnknownPlayer(wantBadPlayer, wanted);
			return;
		}

		FriendList wantedFriendList = getFriendList(wantedBadPlayer.getUniqueId());
		FriendList wantFriendList = getFriendList(wantBadPlayer.getUniqueId());
		FriendListRemoveStatus status;

		if (want.equals(wanted))
		{
			status = FriendListRemoveStatus.PLAYER_SCHIZOPHRENIA;
		}
		else
		{
			if (wantedFriendList.isInList(wantBadPlayer.getUniqueId()) || wantFriendList.isInList(wantedBadPlayer.getUniqueId()))
			{
				if (wantedFriendList.isFriend(wantBadPlayer.getUniqueId()) || wantFriendList.isFriend(wantedBadPlayer.getUniqueId()))
				{
					status = FriendListRemoveStatus.PLAYER_REMOVED_FROM_LIST;
				}
				else
				{
					if (wantedFriendList.wantToBeFriendWith(wantBadPlayer.getUniqueId()))
					{
						status = FriendListRemoveStatus.PLAYER_REQUEST_DECLINED;
					}
					else
					{
						if (wantedFriendList.alreadyRequestedBy(wantBadPlayer.getUniqueId()))
						{
							status = FriendListRemoveStatus.REQUEST_TO_PLAYER_CANCELLED;
						}
						else
						{
							status = FriendListRemoveStatus.UNKNOWN_ERROR;
						}
					}
				}
			}
			else
			{
				status = FriendListRemoveStatus.NOT_REQUESTED_OR_FRIEND_WITH_PLAYER;
			}
		}

		FriendListRemoveEvent event = new FriendListRemoveEvent(wantBadPlayer, wantedBadPlayer, status);
		event = ProxyServer.getInstance().getPluginManager().callEvent(event);

		if (event.isCancelled())
		{
			message.sendOperationCancelled(wantBadPlayer, event.getCancelReason());
		}
		else {
			switch (event.getStatus())
			{
			case UNKNOWN_ERROR:
				message.sendError(wantBadPlayer);
				break;

			case PLAYER_SCHIZOPHRENIA:
				message.sendCantActOnYourself(wantBadPlayer);
				break;

			case PLAYER_REQUEST_DECLINED:
				wantedFriendList.remove(wantBadPlayer.getUniqueId());
				message.sendDeclinedYourRequest(wantedBadPlayer, wantBadPlayer);
				wantFriendList.remove(wantedBadPlayer.getUniqueId());
				message.sendRejectRequestOf(wantBadPlayer, wantedBadPlayer);
				break;

			case PLAYER_REMOVED_FROM_LIST:
				wantedFriendList.remove(wantBadPlayer.getUniqueId());
				message.sendRemovedYouFromFriends(wantedBadPlayer, wantBadPlayer);
				wantFriendList.remove(wantedBadPlayer.getUniqueId());
				message.sendNowNoLongerFriends(wantBadPlayer, wantedBadPlayer);
				break;

			case REQUEST_TO_PLAYER_CANCELLED:
				wantFriendList.remove(wantedBadPlayer.getUniqueId());
				message.sendCancelRequestTo(wantBadPlayer, wantedBadPlayer);
				wantedFriendList.remove(wantBadPlayer.getUniqueId());
				message.sendCancelledRequest(wantedBadPlayer, wantBadPlayer);
				break;

			case NOT_REQUESTED_OR_FRIEND_WITH_PLAYER:
				message.sendNoRelationship(wantBadPlayer, wantedBadPlayer);
				break;

			default:
				message.sendError(wantBadPlayer);
				break;
			}
		}
	}

	public static void showFriendList(BadPlayer badPlayer)
	{
		showFriendList(badPlayer, "1");
	}

	public static List<BadPlayer> getFriends(BadPlayer badPlayer)
	{
		Map<UUID, FriendListPlayer> players = getFriendList(badPlayer.getUniqueId()).getPlayers();
		List<BadPlayer> friends = new ArrayList<>();
		Filter.filterSetStatic(e -> players.get(e).getState() == FriendListPlayerState.ACCEPTED, players.keySet()).forEach(e -> friends.add(BungeeManager.getInstance().getBadPlayer(e)));
		return friends;
	}

	public static void showFriendList(BadPlayer badPlayer, String page)
	{
		int i;

		try
		{
			i = Integer.parseInt(page);
		}
		catch (NumberFormatException e)
		{
			message.sendIncorrectPageNumber(badPlayer, page);
			return;
		}

		List<BadPlayer> friends = getFriends(badPlayer);
		int pages = friends.size() / 10;
		if
		(pages > 1 && i > pages)
		{
			message.sendTooBigPageNumber(badPlayer, i);
		}
		else
		{
			//TODO header
			for (int l = (i - 1) * 10; l < i * 10; l++)
			{
				if (friends.size() - 1 <= l) break;
				//BadPlayer friend = friends.get(l);
				//TODO friend list value
			}
			//TODO footer
		}
	}


}
