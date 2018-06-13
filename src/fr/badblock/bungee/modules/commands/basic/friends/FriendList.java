package fr.badblock.bungee.modules.commands.basic.friends;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * Managing the list of friends in memory
 * 
 * @author xMalware
 *
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public final class FriendList {

	// Some keys
	public static final String OWNER = "_owner";
	public static final String PLAYERS = "players";

	// Collection type
	private static final Type collectionType = new TypeToken<Map<UUID, FriendListPlayer>>() {
	}.getType();

	// The leader of his friend list
	private UUID owner;
	// Friend map
	private Map<UUID, FriendListPlayer> players;

	/**
	 * Constructor of the class
	 * 
	 * @param owner
	 *            > UUID of the owner
	 */
	FriendList(UUID owner) {
		this.owner = owner;
		players = new HashMap<>();
	}

	/**
	 * Constructor of the class
	 * 
	 * @param dbObject
	 *            > database object
	 */
	FriendList(DBObject dbObject) {
		// Set the owner in memory
		owner = UUID.fromString(dbObject.get(OWNER).toString());
		// Set the players in memory
		players = GsonUtils.getGson().fromJson(dbObject.get(PLAYERS).toString(), collectionType);
	}

	/**
	 * Convert into an object of friendship
	 * 
	 * @param uuid
	 *            > player uuid
	 * @param state
	 *            > state of the friendship
	 * @return
	 */
	private FriendListPlayer toFriendListPlayer(UUID uuid, FriendListPlayerState state) {
		return new FriendListPlayer(uuid, state);
	}

	/**
	 * Add a friend to memory
	 * 
	 * @param uuid
	 *            > player uuid
	 * @param state
	 *            > state of the friendship
	 */
	private void add(UUID uuid, FriendListPlayerState state) {
		// We get the friendship object
		FriendListPlayer partyPlayer = toFriendListPlayer(uuid, state);
		// If the friendlist contains the uuid
		if (players.containsKey(uuid)) {
			// We replace the new state of the friendship
			players.replace(uuid, partyPlayer);
		} else
		// If the uuid isn't in the friendlist
		{
			// Add the friendship in the list
			players.put(uuid, partyPlayer);
		}
		// Save changes to the database
		save();
	}

	/**
	 * Remove a friend from the list
	 * 
	 * @param uuid
	 *            > player uuid
	 */
	void remove(UUID uuid) {
		// Remove from memory
		players.remove(uuid);
		// Save changes to the database
		save();
	}

	/**
	 * Accept a friend
	 * 
	 * @param uuid
	 *            > Player UUID
	 */
	void accept(UUID uuid) {
		// Add a friend request with ACCEPTED state
		add(uuid, FriendListPlayerState.ACCEPTED);
	}

	/**
	 * Send a friend request
	 * 
	 * @param uuid
	 *            > Player UUID
	 */
	void request(UUID uuid) {
		// Add a friend request with WAITING state
		add(uuid, FriendListPlayerState.WAITING);
	}

	/**
	 * Send a friend request
	 * 
	 * @param uuid
	 *            > Player UUID
	 */
	void requested(UUID uuid) {
		// Add a friend request with REQUESTED state
		add(uuid, FriendListPlayerState.REQUESTED);
	}

	/**
	 * If the player is in the friendlist
	 * 
	 * @param uuid
	 *            > Player UUID
	 * @return true or false?
	 */
	boolean isFriend(UUID uuid) {
		// If the UUID is in the friendlist and if the request has been accepted
		return players.containsKey(uuid) && players.get(uuid).getState() == FriendListPlayerState.ACCEPTED;
	}

	/**
	 * If the player wants to be friend with another one
	 * 
	 * @param uuid
	 *            > Player UUID
	 * @return true or false?
	 */
	boolean wantToBeFriendWith(UUID uuid) {
		// If the UUID is in the friendlist and if the request is in REQUESTED state
		return players.containsKey(uuid) && players.get(uuid).getState() == FriendListPlayerState.REQUESTED;
	}

	/**
	 * If a player already requested to be friend
	 * 
	 * @param uuid
	 *            > Player UUID
	 * @return true or false?
	 */
	boolean alreadyRequestedBy(UUID uuid) {
		// If the UUID is in the friendlist, return if the friendship state is WAITING
		if (players.containsKey(uuid))
			return players.get(uuid).getState() == FriendListPlayerState.WAITING;
		// The UUID isn't in the friendlist, so we return false
		else
			return false;
	}

	/**
	 * If a player is in the friendlist
	 * 
	 * @param uuid
	 *            > Player UUID
	 * @return true or false?
	 */
	boolean isInList(UUID uuid) {
		// If the UUID is in the friendlist ? true or false
		return players.containsKey(uuid);
	}

	/**
	 * Save changes to database
	 */
	private void save() {
		// If the owner is set
		if (owner != null) {
			// Update changes to database
			FriendListManager.update(this);
		}
	}

	/**
	 * Get the friendlist as a DBObject
	 * 
	 * @return
	 */
	DBObject toObject() {
		// Create a new basic DBOBject
		BasicDBObject object = new BasicDBObject();
		// If the owner is set
		if (owner != null) {
			// So we put the owner in the object
			object.put(OWNER, owner.toString());
		}
		// We put the friendlist in the object
		Map<String, DBObject> map = new HashMap<>();
		// For each data
		for (Entry<UUID, FriendListPlayer> entry : players.entrySet()) {
			// Put in a map
			map.put(entry.getKey().toString(), entry.getValue().toObject());
		}
		// Put the map
		object.put(PLAYERS, map);
		// Return the database object
		return object;
	}

}