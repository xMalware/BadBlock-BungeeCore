package fr.badblock.bungee._plugins.objects.friendlist;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.toenga.common.utils.general.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class FriendList
{

	public static final String				OWNER = "_owner";
	public static final String				QUERYABLE = "queryable";
	public static final String				PLAYERS = "players";

	private static final Type				collectionType = new TypeToken<Map<String, FriendListPlayer>>(){}.getType();

	private String							owner;
	private boolean 						queryable = true;
	public Map<String, FriendListPlayer>	players;

	public FriendList(String owner)
	{
		this.owner = owner;
		players = new HashMap<>();
	}

	public FriendList(String owner, String request, FriendListPlayerState state)
	{
		this(owner);
	}

	public FriendList(DBObject dbObject)
	{
		owner = dbObject.get(OWNER).toString();
		queryable = Boolean.getBoolean(dbObject.get(QUERYABLE).toString());
		players = GsonUtils.getGson().fromJson(dbObject.get(PLAYERS).toString(), collectionType);
	}

	public FriendListPlayer getFriendListPlayer(String name) {
		return getPlayers().get(name.toLowerCase());
	}

	private FriendListPlayer toFriendListPlayer(String name, FriendListPlayerState state) {
		return new FriendListPlayer(name, state);
	}

	public void add(String name, FriendListPlayerState state) {
		name = name.toLowerCase();
		if (players.containsKey(name)) players.remove(name);
		FriendListPlayer partyPlayer = toFriendListPlayer(name, state);
		players.put(name, partyPlayer);
	}
	public void add(String name) {
		add(name, FriendListPlayerState.ACCEPTED);
	}

	public void request(String name) {
		add(name, FriendListPlayerState.WAITING);
	}

	public void requested(String name) {
		add(name, FriendListPlayerState.REQUESTED);
	}

	public boolean isFriend(String name)
	{
		name = name.toLowerCase();
		return players.containsKey(name) && players.get(name).getState() == FriendListPlayerState.ACCEPTED;
	}

	public void save()
	{
		if (owner != null)
		{
			FriendListManager.update(this);
		}
	}

	public DBObject toObject()
	{
		BasicDBObject object = new BasicDBObject();
		if (owner != null) object.put(OWNER, owner);
		object.put(QUERYABLE, queryable);
		object.put(PLAYERS, GsonUtils.getGson().toJson(players, collectionType));
		return object;
	}
}
