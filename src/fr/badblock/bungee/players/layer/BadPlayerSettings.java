package fr.badblock.bungee.players.layer;

import com.google.common.reflect.TypeToken;
import fr.badblock.bungee._plugins.objects.friendlist.FriendListable;
import fr.badblock.bungee._plugins.objects.party.Partyable;
import fr.toenga.common.utils.general.GsonUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Data
public class BadPlayerSettings
{

	// Is partyable by who?
	public Partyable	partyable;
    // Is FriendListable by who ?
    public FriendListable friendListable;

    //Champ ignoré lors de la sérialisation
    //@SettingIgnore
    //public String exemplenotsetting;

	/**
	 * Default values for each player
	 */
	public BadPlayerSettings()
	{
		partyable = Partyable.WITH_EVERYONE;
        friendListable = FriendListable.YES;
	}

    public BadPlayerSettings(String json) {
        Type collectionType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = GsonUtils.getGson().fromJson(json, collectionType);
        for (String s : map.keySet()) {
            try {
                Field f = this.getClass().getField(s);
                if (!f.isAccessible()) f.setAccessible(true);
                if (!f.isAnnotationPresent(SettingIgnore.class)) {
                    f.set(this, GsonUtils.getGson().fromJson(map.get(s), f.getType()));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String toJson() {
        Type collectionType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = new HashMap<>();
        for (Field f : this.getClass().getFields()) {
            if (!f.isAnnotationPresent(SettingIgnore.class)) {
                try {
                    if (!f.isAccessible()) f.setAccessible(true);
                    map.put(f.getName(), GsonUtils.getGson().toJson(f.get(this)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return GsonUtils.getGson().toJson(map, collectionType);
    }
	
}
