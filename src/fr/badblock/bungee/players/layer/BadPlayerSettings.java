package fr.badblock.bungee.players.layer;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.bungee.modules.commands.basic.friends.FriendListable;
import fr.badblock.bungee.modules.commands.basic.party.Partyable;
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
public class BadPlayerSettings
{

	/**
	 *  Is partyable by who?
	 */
	public Partyable		partyable;
	/**
	 * Is FriendListable by who ?
	 */
	public FriendListable	friendListable;

	/**
	 * Constructor
	 * @param a JsonObject
	 */
	public BadPlayerSettings(JsonObject jsonObject)
	{
		// If the object has "partyable"
		if (jsonObject.has("partyable") && !jsonObject.get("partyable").isJsonNull())
		{
			// Set the partyable
			partyable = Partyable.getByString(jsonObject.get("partyable").getAsString());
		}
		// Or
		else
		{
			// Default partyable
			partyable = Partyable.WITH_EVERYONE;
		}
		// If the object has "friendListable"
		if (jsonObject.has("friendListable") && !jsonObject.get("friendListable").isJsonNull())
		{
			// Set the friend listable
			friendListable = FriendListable.getByString(jsonObject.get("friendListable").getAsString());
		}
		else
		{
			// Default friend listable
			friendListable = FriendListable.YES;
		}
	}

	/**
	 * Get DBObject
	 * @return
	 */
	public DBObject getDBObject()
	{
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