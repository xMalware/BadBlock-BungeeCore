package fr.badblock.bungee.modules.commands.basic.friends;

import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * The friendlist player object
 * 
 * @author xMalware
 *
 */
public final class FriendListPlayer {

	/**
	 * Friendlist player state
	 * 
	 * @param Set
	 *            the friendlist player state
	 * @return Returns the friendlist player state
	 */
	private FriendListPlayerState state;

	/**
	 * Unique ID of the player
	 * 
	 * @param Set
	 *            the unique ID
	 * @return Returns the unique ID
	 */
	private UUID uuid;

	/**
	 * Convert to object
	 * 
	 * @return
	 */
	public DBObject toObject() {
		// Create new database object
		BasicDBObject dbObject = new BasicDBObject();
		// Add unique id
		dbObject.put("uuid", getUuid().toString());
		// Add state
		dbObject.put("state", getState().name());
		// Returns the database object
		return dbObject;
	}

}
