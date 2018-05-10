package fr.badblock.bungee.players.layer;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.bungee._plugins.objects.friendlist.FriendListable;
import fr.badblock.bungee._plugins.objects.party.Partyable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public final class BadPlayerSettings
{

	// Is partyable by who?
	public Partyable		partyable;
	// Is FriendListable by who ?
	public FriendListable	friendListable;

	//Champ ignoré lors de la (dé)sérialisation
	//@SettingIgnore
	//public String exemplenotserialiszed;

	/**
	 * Default values for each player
	 */
	public BadPlayerSettings()
	{
		partyable = Partyable.WITH_EVERYONE;
		friendListable = FriendListable.YES;
	}

	public BadPlayerSettings(JsonObject jsonObject)
	{
		if (jsonObject.has("partyable"))
		{
			partyable = Partyable.getByString(jsonObject.get("partyable").toString());
		}
		else
		{
			partyable = Partyable.WITH_EVERYONE;
		}
		if (jsonObject.has("friendListable"))
		{
			friendListable = FriendListable.getByString(jsonObject.get("friendListable").toString());
		}
		else
		{
			friendListable = FriendListable.YES;
		}
	}

	public DBObject getDBObject()
	{
		BasicDBObject query = new BasicDBObject();
		query.put("partyable", partyable.name());
		query.put("friendListable", friendListable.name());
		return query;
	}
	
}
