package fr.badblock.bungee.modules.commands.basic.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.basic.friends.events.FriendListRemoveEvent;
import fr.badblock.bungee.modules.commands.basic.friends.events.FriendListRequestEvent;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

/**
 * 
 * Main class for FriendLists
 *
 * @author RedSpri
 * 
 */
public final class FriendListManager {

	/**
	 * MongoDB collection name
	 */
	private static final String COLLECTION = "friendlist";
	/**
	 * Friend list message instance
	 * 
	 * @param Set
	 *            the new friend list message instance
	 * @return Returns the current friend list message instance
	 */
	@Getter
	private static FriendListMessage message = new FriendListMessage();

	/**
	 * Get the friend list with the UUID
	 * 
	 * @param unique
	 *            ID
	 * @return
	 */
	public static synchronized FriendList getFriendList(UUID player) {
		// Empty query
		BasicDBObject query = new BasicDBObject();
		// Add the player unique id as the owner in the query
		query.append(FriendList.OWNER, player.toString());
		// Get the database object
		DBObject obj = new SynchroMongoDBGetter(COLLECTION, query).getDbObject();
		// If the database object isn't null
		if (obj != null) {
			// Returns a new friend list
			return new FriendList(obj);
		}
		// Create a new friend list
		FriendList friendList = new FriendList(player);
		// Insert the friend list
		insert(friendList);
		// Returns the friend list
		return friendList;
	}

	/**
	 * Update the friend list
	 * 
	 * @param friendlist
	 */
	static void update(FriendList friendlist) {
		// Get the mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * When executing asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection(COLLECTION);
				// Create a new query
				BasicDBObject query = new BasicDBObject();
				// Add the owner in the query
				query.put(FriendList.OWNER, friendlist.getOwner().toString());
				// Create set updater
				BasicDBObject updater = new BasicDBObject("$set", friendlist.toObject());
				// Update the friendlist of the owner
				collection.update(query, updater);
			}
		});
	}

	/**
	 * Insert a friend list
	 * 
	 * @param New
	 *            friend list
	 */
	private static void insert(FriendList friendList) {
		// Get the mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * When executing asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection(COLLECTION);
				// Insert the friendlist in the collection
				collection.insert(friendList.toObject());
			}
		});
	}

	/**
	 * Show the status selector
	 * 
	 * @param BadPlayer
	 *            object
	 */
	public static void showStatusSelector(BadPlayer player) {
		// Send the query selector message
		message.sendQuerySelector(player);
	}

	/**
	 * Set queryable
	 * 
	 * @param BadPlayer
	 * @param Raw
	 *            status
	 */
	public static void setQueryable(BadPlayer player, String status) {
		// Get the friendlistable status
		FriendListable friendListable = FriendListable.getByString(status);
		// If the friendlistable status is null
		if (friendListable == null) {
			// Unknown status
			message.sendUnknownStatus(player);
			// So we stop there
			return;
		}

		// Create a new event
		FriendListableChangeEvent event = new FriendListableChangeEvent(player,
				player.getSettings().getFriendListable(), friendListable);
		// Call the event
		event = ProxyServer.getInstance().getPluginManager().callEvent(event);

		// If the event is cancelled
		if (event.isCancelled()) {
			// Operation cancelled
			message.sendOperationCancelled(player, event.getCancelReason());
			// So we stop there
			return;
		}

		// If the old status is the same as the new status.
		if (event.getOldStatus() != null && event.getOldStatus().equals(event.getNewStatus())) {
			// If the new status is equal to YES
			if (event.getNewStatus().equals(FriendListable.YES)) {
				// Already accepted
				message.sendAlreadyAccepted(player);
				// So we stop there
				return;
			}
			// Already declined
			message.sendAlreadyRefused(player);
			// So we stop there
			return;
		}

		// Set the new status in the settings
		player.getSettings().setFriendListable(event.getNewStatus());

		// Update the settings
		player.updateSettings();

		// If the new status is equal to YES
		if (event.getNewStatus().equals(FriendListable.YES)) {
			// Now accepted
			message.sendNowAccepted(player);
			// We stop there
			return;
		}
		// Already declined
		message.sendNowRefused(player);
	}

	/**
	 * Request
	 * 
	 * @param want
	 *            player name
	 * @param wanted
	 *            player name
	 */
	public static void request(String want, String wanted) {
		// Get the want player name
		BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
		// Get the wanted player name
		BadOfflinePlayer wantedBadPlayer = BungeeManager.getInstance().getBadOfflinePlayer(wanted);

		// If the wanted player name is null
		if (wantedBadPlayer == null) {
			// Unknown player
			message.sendUnknownPlayer(wantBadPlayer, wanted);
			// So we stop there
			return;
		}

		// Get the wanted player friend list
		FriendList wantedFriendList = getFriendList(wantedBadPlayer.getUniqueId());
		// Get the want player friend list
		FriendList wantFriendList = getFriendList(wantBadPlayer.getUniqueId());
		// Create a friend list request status
		FriendListRequestStatus status;

		// If the want player is equal to the wanted player
		if (want.equalsIgnoreCase(wanted)) {
			// Set the status
			status = FriendListRequestStatus.PLAYER_SCHIZOPHRENIA;
		} else {
			// If the wanted player is in the list of the want player
			if (wantedFriendList.isInList(wantBadPlayer.getUniqueId())) {
				// If they're already friend
				if (wantedFriendList.isFriend(wantBadPlayer.getUniqueId())) {
					// Set the status => they're already friends
					status = FriendListRequestStatus.PLAYERS_ALREADY_FRIENDS;
				} else {
					// If he want to be friend
					if (wantedFriendList.wantToBeFriendWith(wantBadPlayer.getUniqueId())) {
						// Set the status => they're now friends
						status = FriendListRequestStatus.PLAYERS_NOW_FRIENDS;
					} else {
						// If he already requested
						if (wantedFriendList.alreadyRequestedBy(wantBadPlayer.getUniqueId())) {
							// Set the status => already requested
							status = FriendListRequestStatus.PLAYER_ALREADY_REQUESTED;
						}
						// What?
						else {
							// Set the status => unknown error
							status = FriendListRequestStatus.UNKNOWN_ERROR;
						}
						// End if
					}
					// End if
				}
				// End if
			}
			// If they're not in list
			else {
				// If the friend listable status is YES
				if (wantedBadPlayer.getSettings() == null || wantedBadPlayer.getSettings().getFriendListable() == null
						|| (wantedBadPlayer.getSettings() != null
								&& FriendListable.YES.equals(wantedBadPlayer.getSettings().getFriendListable()))) {
					// Set the status => receive request
					status = FriendListRequestStatus.PLAYER_RECEIVE_REQUEST;
				} else {
					// Set the status => don't accept requests
					status = FriendListRequestStatus.PLAYER_DO_NOT_ACCEPT_REQUEST;
				}
			}
		}

		// Create a new event
		FriendListRequestEvent event = new FriendListRequestEvent(wantBadPlayer, wantedBadPlayer, status);
		// Call the event
		event = ProxyServer.getInstance().getPluginManager().callEvent(event);

		// If the event is cancelled
		if (event.isCancelled()) {
			// Operation cancelled
			message.sendOperationCancelled(wantBadPlayer, event.getCancelReason());
			// So we stop there
			return;
		}

		// Switch
		switch (event.getStatus()) {
		// In case of an unknown erorr
		case UNKNOWN_ERROR:
			// Send an error
			message.sendError(wantBadPlayer);
			break;

		// Players now friends
		case PLAYERS_NOW_FRIENDS:
			// Accept the request
			wantedFriendList.accept(wantBadPlayer.getUniqueId());
			// Send the message
			message.sendRequestAccepted(wantedBadPlayer, wantBadPlayer);
			// Accept the request
			wantFriendList.accept(wantedBadPlayer.getUniqueId());
			// Send the message
			message.sendAcceptRequester(wantBadPlayer, wantedBadPlayer);
			break;

		// Sended to himself the request
		case PLAYER_SCHIZOPHRENIA:
			// Can't act on yourself message
			message.sendCantActOnYourself(wantBadPlayer);
			break;

		// Receive request
		case PLAYER_RECEIVE_REQUEST:
			// Send the request
			wantedFriendList.request(wantBadPlayer.getUniqueId());
			// Send the message
			message.sendRequest(wantedBadPlayer, wantBadPlayer);
			// Requested!
			wantFriendList.requested(wantedBadPlayer.getUniqueId());
			// Send the message
			message.sendRequestReceived(wantBadPlayer, wantedBadPlayer);
			break;

		// Already friends
		case PLAYERS_ALREADY_FRIENDS:
			// Send the message
			message.sendAlreadyFriend(wantBadPlayer, wantedBadPlayer);
			break;

		// Already requested
		case PLAYER_ALREADY_REQUESTED:
			// Send the message
			message.sendAlreadyRequested(wantBadPlayer);
			break;

		// Don't accept requests
		case PLAYER_DO_NOT_ACCEPT_REQUEST:
			// Send the message
			message.sendDoNotAcceptRequests(wantBadPlayer, wantedBadPlayer);

			// Unknown
		default:
			// Send the error message
			message.sendError(wantBadPlayer);
			break;
		}
	}

	/**
	 * Remove a friend
	 * 
	 * @param want
	 *            username
	 * @param wanted
	 *            username
	 */
	public static void remove(String want, String wanted) {
		// Get the want BadPlayer
		BadPlayer wantBadPlayer = BungeeManager.getInstance().getBadPlayer(want);
		// Get the wanted BadPlayer
		BadOfflinePlayer wantedBadPlayer = BungeeManager.getInstance().getBadOfflinePlayer(wanted);

		// If the wanted BadPlayer is null
		if (wantedBadPlayer == null) {
			// Send the message
			message.sendUnknownPlayer(wantBadPlayer, wanted);
			// So we stop there
			return;
		}

		// Get the wanted friend list
		FriendList wantedFriendList = getFriendList(wantedBadPlayer.getUniqueId());
		// Get the want friend list
		FriendList wantFriendList = getFriendList(wantBadPlayer.getUniqueId());
		// New status
		FriendListRemoveStatus status;

		// If the want player remove the wanted player
		if (want.equalsIgnoreCase(wanted)) {
			// Set the status
			status = FriendListRemoveStatus.PLAYER_SCHIZOPHRENIA;
		}
		// Else if
		else {
			// If the wanted player is in the friend list etc.
			if (wantedFriendList.isInList(wantBadPlayer.getUniqueId())
					|| wantFriendList.isInList(wantedBadPlayer.getUniqueId())) {
				// If they're friends
				if (wantedFriendList.isFriend(wantBadPlayer.getUniqueId())
						|| wantFriendList.isFriend(wantedBadPlayer.getUniqueId())) {
					// Set the status => removed from the list
					status = FriendListRemoveStatus.PLAYER_REMOVED_FROM_LIST;
				} else {
					// If he want to be friend
					if (wantedFriendList.wantToBeFriendWith(wantBadPlayer.getUniqueId())) {
						// Set the status => request declined
						status = FriendListRemoveStatus.PLAYER_REQUEST_DECLINED;
					} else {
						// If they're already requested
						if (wantedFriendList.alreadyRequestedBy(wantBadPlayer.getUniqueId())) {
							// Set the status => cancelled
							status = FriendListRemoveStatus.REQUEST_TO_PLAYER_CANCELLED;
						} else {
							// Set the status => error
							status = FriendListRemoveStatus.UNKNOWN_ERROR;
						}
					}
				}
			}
			// If they're not in list
			else {
				// Set the status => not request/or friend with player
				status = FriendListRemoveStatus.NOT_REQUESTED_OR_FRIEND_WITH_PLAYER;
			}
		}

		// Create an event
		FriendListRemoveEvent event = new FriendListRemoveEvent(wantBadPlayer, wantedBadPlayer, status);
		// Call the event
		event = ProxyServer.getInstance().getPluginManager().callEvent(event);

		// If the event is cancelled
		if (event.isCancelled()) {
			// Operation cancelled
			message.sendOperationCancelled(wantBadPlayer, event.getCancelReason());
			// We stop there
			return;
		}

		// Check status
		switch (event.getStatus()) {
		// Error case
		case UNKNOWN_ERROR:
			// Send the error message
			message.sendError(wantBadPlayer);
			break;

		// Same remove user
		case PLAYER_SCHIZOPHRENIA:
			// Send the message
			message.sendCantActOnYourself(wantBadPlayer);
			break;

		// Request declined
		case PLAYER_REQUEST_DECLINED:
			// Remove the friend
			wantedFriendList.remove(wantBadPlayer.getUniqueId());
			// Send the message
			message.sendDeclinedYourRequest(wantedBadPlayer, wantBadPlayer);
			// Remove the friend
			wantFriendList.remove(wantedBadPlayer.getUniqueId());
			// Send the message
			message.sendRejectRequestOf(wantBadPlayer, wantedBadPlayer);
			break;

		// Removed from list
		case PLAYER_REMOVED_FROM_LIST:
			// Remove the friend
			wantedFriendList.remove(wantBadPlayer.getUniqueId());
			// Send the message
			message.sendRemovedYouFromFriends(wantedBadPlayer, wantBadPlayer);
			// Remove the friend
			wantFriendList.remove(wantedBadPlayer.getUniqueId());
			// Send the message
			message.sendNowNoLongerFriends(wantBadPlayer, wantedBadPlayer);
			break;

		// Cancelled
		case REQUEST_TO_PLAYER_CANCELLED:
			// Remove the friend
			wantFriendList.remove(wantedBadPlayer.getUniqueId());
			// Send the message
			message.sendCancelRequestTo(wantBadPlayer, wantedBadPlayer);
			// Remove the friend
			wantedFriendList.remove(wantBadPlayer.getUniqueId());
			// Send the message
			message.sendCancelledRequest(wantedBadPlayer, wantBadPlayer);
			break;

		case NOT_REQUESTED_OR_FRIEND_WITH_PLAYER:
			// No relationship between them
			message.sendNoRelationship(wantBadPlayer, wantedBadPlayer);
			break;

		// What else?
		default:
			// Send the error message
			message.sendError(wantBadPlayer);
			break;
		}
	}

	/**
	 * Show the friend list (default: page 1)
	 * 
	 * @param badPlayer
	 */
	public static void showFriendList(BadPlayer badPlayer) {
		showFriendList(badPlayer, "1");
	}

	/**
	 * Get the friend list
	 * 
	 * @param BadOfflinePlayer
	 *            object
	 * @return friend list
	 */
	public static Map<BadOfflinePlayer, FriendListPlayer> getFriends(BadPlayer badPlayer) {
		// Get the friend list
		Map<UUID, FriendListPlayer> players = getFriendList(badPlayer.getUniqueId()).getPlayers();
		// Create a new friend list
		Map<BadOfflinePlayer, FriendListPlayer> friends = new HashMap<>();
		// Get BungeeManager object
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// Put all friends
		players.entrySet()
				.forEach(player -> friends.put(bungeeManager.getBadOfflinePlayer(player.getKey()), player.getValue()));
		// Returns friend list
		return friends;
	}

	/**
	 * Show the friend list
	 * 
	 * @param badPlayer
	 *            object
	 * @param page
	 *            number
	 */
	public static void showFriendList(BadPlayer badPlayer, String page) {
		// Page id
		int i;

		// Try
		try {
			// Parse int
			i = Integer.parseInt(page);
		}
		// Error case
		catch (NumberFormatException e) {
			// Send incorrect page number
			message.sendIncorrectPageNumber(badPlayer, page);
			// So we stop there
			return;
		}

		// Get all friends
		Map<BadOfflinePlayer, FriendListPlayer> friends = getFriends(badPlayer);

		// If he has no friends
		if (friends == null || (friends != null && friends.isEmpty())) {
			// Send no friends
			message.sendNoFriends(badPlayer);
			// So we stop there
			return;
		}

		// Get page numbers
		int pages = (int) Math.ceil(friends.size() / 10.0);

		// Incorrect page number
		if (i > pages) {
			// Send too big page number message
			message.sendTooBigPageNumber(badPlayer, i);
		} else {
			// Get BungeeManager
			BungeeManager bungeeManager = BungeeManager.getInstance();
			// Send the message
			message.sendIntroList(badPlayer);
			// As a list
			List<Entry<BadOfflinePlayer, FriendListPlayer>> list = new ArrayList<>(friends.entrySet());
			// For each
			for (int l = (i - 1) * 10; l < i * 10; l++) {
				System.out.println("A : " + (friends.size() - 1) + " / " + l);
				// Exception case
				if (friends.size() - 1 < l)
					break;
				// Get the entry
				Entry<BadOfflinePlayer, FriendListPlayer> friend = list.get(l);
				// If friend is null
				if (friend == null) {
					// So we stop there
					break;
				}
				// Get the friend data
				FriendListPlayer friendListPlayer = friend.getValue();
				System.out.println("B");
				// If the friend list player is null
				if (friendListPlayer == null) {
					// So we stop there
					continue;
				}
				System.out.println("C");
				// Get the frienship state
				FriendListPlayerState state = friendListPlayer.getState();
				// If the friendship state is accepted
				if (FriendListPlayerState.ACCEPTED.equals(state)) {
					System.out.println("D");
					// If the player is offline
					if (!bungeeManager.hasUsername(friend.getKey().getName())) {
						System.out.println("E");
						// Send offline list
						message.sendOfflineList(badPlayer, friend.getKey());
					}
					// If the player is online
					else {
						System.out.println("F");
						// Send online list
						message.sendOnlineList(badPlayer, bungeeManager.getBadPlayer(friend.getKey().getName()));
					}
				}
				// If the friendship state is 'requested'
				else if (FriendListPlayerState.REQUESTED.equals(state)) {
					System.out.println("G");
					// Send requested list
					message.sendRequestedList(badPlayer, friend.getKey());
				}
				// If the friendship state is 'waiting'
				else if (FriendListPlayerState.WAITING.equals(state)) {
					System.out.println("H");
					// Send waiting list
					message.sendWaitingList(badPlayer, friend.getKey());
				}
			}
			// Send footer
			message.sendFooterList(badPlayer);
		}
	}

}