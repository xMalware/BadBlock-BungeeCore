package fr.badblock.bungee.players.layer;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.bungee._plugins.objects.party.Partyable;
import lombok.Data;

@Data
public class BadPlayerSettings
{
	
	// Is partyable by who?
	public Partyable	partyable;

	/**
	 * Default values for each player
	 */
	public BadPlayerSettings()
	{
		partyable = Partyable.WITH_EVERYONE;
	}
	
	public BadPlayerSettings(JsonObject jsonObject)
	{
		partyable = Partyable.getByString(jsonObject.get("partyable").toString());
	}
	
	public DBObject toDBObject()
	{
		BasicDBObject object = new BasicDBObject();
		object.put("partyable", getPartyable().name());
		return object;
	}
	
}
