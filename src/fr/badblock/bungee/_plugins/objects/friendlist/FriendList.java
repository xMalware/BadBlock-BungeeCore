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
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public final class FriendList
{

	public static final String				OWNER = "_owner";
	public static final String				PLAYERS = "players";

    private static final Type collectionType = new TypeToken<Map<UUID, FriendListPlayer>>() {
    }.getType();

    private UUID owner;
    private Map<UUID, FriendListPlayer> players;

    FriendList(UUID owner)
	{
		this.owner = owner;
		players = new HashMap<>();
	}

    FriendList(DBObject dbObject)
	{
        owner = UUID.fromString(dbObject.get(OWNER).toString());
		players = GsonUtils.getGson().fromJson(dbObject.get(PLAYERS).toString(), collectionType);
	}

    private FriendListPlayer toFriendListPlayer(UUID uuid, FriendListPlayerState state) {
        return new FriendListPlayer(uuid, state);
    }

    private void add(UUID uuid, FriendListPlayerState state) {
        FriendListPlayer partyPlayer = toFriendListPlayer(uuid, state);
        if (players.containsKey(uuid)) players.replace(uuid, partyPlayer);
        else players.put(uuid, partyPlayer);
        save();
    }

    void remove(UUID uuid) {
        players.remove(uuid);
        save();
    }

    void accept(UUID uuid) {
        add(uuid, FriendListPlayerState.ACCEPTED);
    }

    void request(UUID uuid) {
        add(uuid, FriendListPlayerState.WAITING);
    }

    void requested(UUID uuid) {
        add(uuid, FriendListPlayerState.REQUESTED);
    }

    boolean isFriend(UUID uuid) {
        return players.containsKey(uuid) && players.get(uuid).getState() == FriendListPlayerState.ACCEPTED;
    }

    boolean wantToBeFriendWith(UUID uuid) {
        return players.containsKey(uuid) && players.get(uuid).getState() == FriendListPlayerState.REQUESTED;
    }

    boolean alreadyRequestedBy(UUID uuid) {
        if (players.containsKey(uuid)) return players.get(uuid).getState() == FriendListPlayerState.WAITING;
        else return false;
    }

    boolean isInList(UUID uuid) {
        return players.containsKey(uuid);
    }

    private void save() {
        if (owner != null) FriendListManager.update(this);
    }

    DBObject toObject() {
		BasicDBObject object = new BasicDBObject();
        if (owner != null) object.put(OWNER, owner.toString());
		object.put(PLAYERS, GsonUtils.getGson().toJson(players, collectionType));
		return object;
	}
}
