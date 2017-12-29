package fr.badblock.bungee._plugins.objects.party;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.toenga.common.utils.general.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class Party
{

	private static final Type collectionType = new TypeToken<Map<String, PartyPlayer>>(){}.getType();

	public String					uuid;
	public Map<String, PartyPlayer>	players;

	public Party(String leader)
	{
		players = new HashMap<>();
		add(leader, PartyPlayerRole.ADMIN);
	}

	public Party(String leader, String invited)
	{
		this(leader);
		invite(invited, PartyPlayerRole.DEFAULT);
	}

	public Party(DBObject dbObject)
	{
		uuid = dbObject.get("_id").toString();
		players = GsonUtils.getGson().fromJson(dbObject.get("players").toString(), collectionType);
	}

	public PartyPlayer getPartyPlayer(String name)
	{
		return getPlayers().get(name.toLowerCase());
	}

	private PartyPlayer toPartyPlayer(String name, PartyPlayerRole role, PartyPlayerState state)
	{
		return new PartyPlayer(name, role, state);
	}

	public void add(String name, PartyPlayerRole role, PartyPlayerState state)
	{
		name = name.toLowerCase();
		PartyPlayer partyPlayer = toPartyPlayer(name, role, state);
		players.put(name, partyPlayer);
		if (uuid != null)
		{
			PartyManager.update(this);
		}
	}
	public void add(String name, PartyPlayerRole role)
	{
		add(name, role, PartyPlayerState.ACCEPTED);
	}

	public void invite(String name, PartyPlayerRole role)
	{
		add(name, role, PartyPlayerState.WAITING);
	}

	public void accept(String name)
	{
		add(name, getPartyPlayer(name).getRole());
	}

	public DBObject toObject()
	{
		BasicDBObject object = new BasicDBObject();

		if (uuid != null)
		{
			object.put("_id", uuid);
		}
		object.put("players", players);

		return object;
	}

}
