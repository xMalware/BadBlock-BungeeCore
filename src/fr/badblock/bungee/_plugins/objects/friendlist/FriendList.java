package fr.badblock.bungee._plugins.objects.friendlist;

import com.google.gson.reflect.TypeToken;
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
public class FriendList {
    private static final Type collectionType = new TypeToken<Map<String, FriendListPlayer>>(){}.getType();
    
    private String owner;
    private boolean queryable = true;
    public Map<String, FriendListPlayer> players;

    public FriendList(String owner) {
        this.owner = owner;
        players = new HashMap<>();
    }

    public FriendList(String owner, String request, FriendListPlayerState state) {
        this(owner);

    }

    public FriendList(DBObject dbObject) {
        owner = dbObject.get("owner").toString();
        queryable = Boolean.getBoolean(dbObject.get("queryable").toString());
        players = GsonUtils.getGson().fromJson(dbObject.get("players").toString(), collectionType);
    }

    public FriendListPlayer getPartyPlayer(String name) {
        return getPlayers().get(name.toLowerCase());
    }

    private FriendListPlayer toPartyPlayer(String name, FriendListPlayerState state) {
        return new FriendListPlayer(name, state);
    }
}
