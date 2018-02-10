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
	public static final String				PLAYERS = "players";

	private static final Type				collectionType = new TypeToken<Map<String, FriendListPlayer>>(){}.getType();

	private String							owner;
    private Map<String, FriendListPlayer> players;

    FriendList(String owner)
	{
		this.owner = owner;
		players = new HashMap<>();
	}

    FriendList(DBObject dbObject)
	{
		owner = dbObject.get(OWNER).toString();
		players = GsonUtils.getGson().fromJson(dbObject.get(PLAYERS).toString(), collectionType);
	}

	private FriendListPlayer toFriendListPlayer(String name, FriendListPlayerState state) {
		return new FriendListPlayer(name, state);
	}

    private void add(String name, FriendListPlayerState state) {
		name = name.toLowerCase();
		FriendListPlayer partyPlayer = toFriendListPlayer(name, state);
        if (players.containsKey(name)) players.replace(name, partyPlayer);
        else players.put(name, partyPlayer);
        save();
    }


    void setQueryable(boolean queryable) {
        save();
    }

    void remove(String name) {
        name = name.toLowerCase();
        players.remove(name);
        save();
    }

    void accept(String name) {
		add(name, FriendListPlayerState.ACCEPTED);
	}

    void request(String name) {
		add(name, FriendListPlayerState.WAITING);
	}

    void requested(String name) {
		add(name, FriendListPlayerState.REQUESTED);
	}

    boolean isFriend(String name)
	{
		name = name.toLowerCase();
		return players.containsKey(name) && players.get(name).getState() == FriendListPlayerState.ACCEPTED;
	}

    boolean wantToBeFriendWith(String name) {
        name = name.toLowerCase();
        return players.containsKey(name) && players.get(name).getState() == FriendListPlayerState.REQUESTED;
    }

    boolean alreadyRequestedBy(String name) {
        name = name.toLowerCase();
        if (players.containsKey(name)) return players.get(name).getState() == FriendListPlayerState.WAITING;
        else return false;
    }

    boolean isInList(String name) {
        name = name.toLowerCase();
        return players.containsKey(name);
    }

    private void save()
	{
		if (owner != null)
		{
			FriendListManager.update(this);
		}
	}

    DBObject toObject()
	{
		BasicDBObject object = new BasicDBObject();
		if (owner != null) object.put(OWNER, owner);
		object.put(PLAYERS, GsonUtils.getGson().toJson(players, collectionType));
		return object;
	}
}
