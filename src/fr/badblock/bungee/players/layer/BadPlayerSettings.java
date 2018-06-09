package fr.badblock.bungee.players.layer;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.bungee.modules.friends.FriendListable;
import fr.badblock.bungee.modules.party.Partyable;
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
public final class BadPlayerSettings
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
	 * Default values for each player
	 */
	public BadPlayerSettings()
	{
		// Set default partyable
		partyable = Partyable.WITH_EVERYONE;
		// Set default friend listable
		friendListable = FriendListable.YES;
	}

	/**
	 * Constructor
	 * @param a JsonObject
	 */
	public BadPlayerSettings(JsonObject jsonObject)
	{
		// If the object has "partyable"
		if (jsonObject.has("partyable"))
		{
			// Set the partyable
			partyable = Partyable.getByString(jsonObject.get("partyable").toString());
		}
		// Or
		else
		{
			// Default partyable
			partyable = Partyable.WITH_EVERYONE;
		}
		// If the object has "friendListable"
		if (jsonObject.has("friendListable"))
		{
			// Set the friend listable
			friendListable = FriendListable.getByString(jsonObject.get("friendListable").toString());
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
		query.put("partyable", partyable.name());
		// Put friend listable
		query.put("friendListable", friendListable.name());
		// Returns DBObject
		return query;
	}
	
}