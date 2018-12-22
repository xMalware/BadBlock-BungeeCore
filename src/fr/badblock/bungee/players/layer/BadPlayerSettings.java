package fr.badblock.bungee.players.layer;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.minecraft.PMPrivacy;
import fr.badblock.api.common.minecraft.friends.FriendListable;
import fr.badblock.api.common.minecraft.party.Partyable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
/**
 * 
 * BadPlayer settings
 * 
 * @author xMalware
 *
 */
public class BadPlayerSettings {

	/**
	 * Is FriendListable by who ?
	 */
	public FriendListable friendListable;
	/**
	 * Is partyable by who?
	 */
	public Partyable partyable;
	/**
	 * PM Privacy
	 */
	public PMPrivacy pmPrivacy;

	/**
	 * Constructor
	 * 
	 * @param a
	 *            JsonObject
	 */
	public BadPlayerSettings(JsonObject jsonObject) {
		// If the object has "partyable"
		if (jsonObject.has("partyable") && !jsonObject.get("partyable").isJsonNull()) {
			// Set the partyable
			partyable = Partyable.getByString(jsonObject.get("partyable").getAsString());
		}
		// Or
		else {
			// Default partyable
			partyable = Partyable.WITH_EVERYONE;
		}

		// If the object has "friendListable"
		if (jsonObject.has("friendListable") && !jsonObject.get("friendListable").isJsonNull()) {
			// Set the friend listable
			friendListable = FriendListable.getByString(jsonObject.get("friendListable").getAsString());
		} else {
			// Default friend listable
			friendListable = FriendListable.YES;
		}

		// If the object has "pmPrivacy"
		if (jsonObject.has("pmPrivacy") && !jsonObject.get("pmPrivacy").isJsonNull()) {
			// Set pmPrivacy
			pmPrivacy = PMPrivacy.getByString(jsonObject.get("pmPrivacy").getAsString());
		} else {
			// Default pm privacy
			pmPrivacy = PMPrivacy.WITH_EVERYONE;
		}
	}

	/**
	 * Get DBObject
	 * 
	 * @return
	 */
	public DBObject getDBObject() {
		// Create new DBObject
		BasicDBObject query = new BasicDBObject();
		// Put partyable
		query.put("partyable", partyable != null ? partyable.name() : null);
		// Put friend listable
		query.put("friendListable", friendListable != null ? friendListable.name() : null);
		// Returns DBObject
		return query;
	}

}