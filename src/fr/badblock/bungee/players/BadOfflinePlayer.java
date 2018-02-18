package fr.badblock.bungee.players;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.layer.BadPlayerSettings;
import fr.badblock.bungee.players.layer.BadPlayerSettingsSerializer;
import fr.badblock.bungee.utils.Filter;
import fr.badblock.bungee.utils.ObjectUtils;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import fr.toenga.common.utils.bungee.Punished;
import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.common.utils.i18n.I18n;
import fr.toenga.common.utils.i18n.Locale;
import fr.toenga.common.utils.permissions.Permissible;
import fr.toenga.common.utils.permissions.PermissionsManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.BSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadOfflinePlayer {
    private static Map<String, BadOfflinePlayer> maps = new HashMap<>();

	private 			String						name;
    private BadIP lastIp;
    private UUID uniqueId;
	private transient	BSONObject	  				dbObject;

	private 			Permissible					permissions;
	private 			Punished					punished;
	
	private				BadPlayerSettings			settings;
    private Integer version;
    private Boolean finded = true;

    public BadOfflinePlayer(String name) {
		setName(name);
        loadData(true);
    }

    public BadOfflinePlayer(UUID uniqueId) {
        setUniqueId(uniqueId);
        loadData(true, true);
    }

    public BadOfflinePlayer(String name, boolean create) {
        setName(name);
        loadData(create);
    }

    public BadOfflinePlayer(UUID uniqueId, boolean create) {
        setUniqueId(uniqueId);
        loadData(true, create);
    }

    public static BadOfflinePlayer get(String name) {
        List<BadOfflinePlayer> list = Filter.filterCollectionStatic(p -> p.getName().equalsIgnoreCase(name), maps.values());
        if (list.isEmpty()) {
            BadOfflinePlayer p = new BadOfflinePlayer(name, false);
            if (!p.getFinded()) return null;
            maps.put(p.getName(), p);
            return p;
        } else return list.get(0);
    }

    public static BadOfflinePlayer get(UUID uuid) {
        List<BadOfflinePlayer> list = Filter.filterCollectionStatic(p -> p.getUniqueId().toString().equalsIgnoreCase(uuid.toString()), maps.values());
        if (list.isEmpty()) {
            BadOfflinePlayer p = new BadOfflinePlayer(uuid, false);
            if (!p.getFinded()) return null;
            maps.put(p.getName(), p);
            return p;
        } else return list.get(0);
    }

    public void updateSettings() {
        updateData("settings", BadPlayerSettingsSerializer.serialize(settings));
    }

    public void updateVersion() {
        updateData("version", getVersion().toString());
    }

    public void updateLastIp() {
        updateData("lastIp", getLastIp().toString());
    }

    public void updateData(String key, Object value) {
		MongoService mongoService = BadBungee.getInstance().getMongoService();
        mongoService.useAsyncMongo(new MongoMethod(mongoService) {

			@Override
            public void run(MongoService mongoService) {
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("players");
				BasicDBObject query = new BasicDBObject();
				BasicDBObject update = new BasicDBObject();
                query.put("name", getName().toLowerCase());
				update.put(key, value);
                collection.update(query, update);
                loadData(false);
			}

		});
	}

    public void updateLastServer(ProxiedPlayer proxiedPlayer) {
		updateData("lastServer", proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null ? proxiedPlayer.getServer().getInfo().getName() : "");
	}

    void loadData(boolean create) {
        loadData(false, create);
    }

    private void loadData(boolean uuid, boolean create) {
        BasicDBObject query = new BasicDBObject();
        if (!uuid) query.append("name", getName().toLowerCase());
        else query.append("uniqueId", getUniqueId().toString());
        DBObject obj = new SynchroMongoDBGetter("players", query).getDbObject();
        if (obj != null) {
            setDbObject(obj);
            BadBungee.log("§c" + getName() + " exists in the player table.");
            setName(getString("realName"));
            setLastIp(BadIP.fromString(getString("lastIp")));
            setUniqueId(UUID.fromString(getString("uniqueId")));
            setSettings(BadPlayerSettingsSerializer.deserialize(getString("settings")));
            setPunished(Punished.fromJson(getJsonObject("punish")));
            if (PermissionsManager.getManager() != null) {
                setPermissions(PermissionsManager.getManager().loadPermissible(getJsonObject("permissions")));
            }
            setVersion(Integer.valueOf(getString("version")));
        } else {
            BadBungee.log(getName() + " doesn't exist in the player table.");
            if (create) insert();
            else setFinded(false);
        }
    }

    private void insert() {
        BadBungee.log("§aCreating it...");
        MongoService mongoService = BadBungee.getInstance().getMongoService();
        mongoService.useAsyncMongo(new MongoMethod(mongoService) {

            @Override
            public void run(MongoService mongoService) {
                punished = new Punished();
                permissions = new Permissible();
                settings = new BadPlayerSettings();
                uniqueId = UUID.randomUUID();
                lastIp = BadIP.fromString("");
                version = 0;
                BasicDBObject obj = getSavedObject();
                setDbObject(obj);
                DB db = mongoService.getDb();
                DBCollection collection = db.getCollection("players");
                collection.insert(obj);
                BadBungee.log("§aCreated!");
            }
        });
    }

    public String[] getTranslatedMessages(String key, Object... objects) {
		return I18n.getInstance().get(getLocale(), key, objects);
	}

    public boolean hasPermission(String permission) {
        if (getPermissions() == null) return false;
		return getPermissions().hasPermission(permission);
	}

    private String getString(String part) {
        if (getDbObject().containsField(part)) return getDbObject().get(part).toString();
        else return "";
    }

    public JsonObject getJsonObject(String part) {
		//FIXME vraiment pas optimisé, à voir si il y a mieux
        if (getDbObject().containsField(part)) {
            String value = getDbObject().get(part).toString();
			return GsonUtils.getPrettyGson().fromJson(value, JsonObject.class);
		} else return new JsonObject();
    }

    public BasicDBObject getSavedObject() {
		BasicDBObject object = new BasicDBObject();
		object.put("name", getName().toLowerCase());
		object.put("realName", getName());
        object.put("lastIp", getLastIp().toString());
        object.put("uniqueId", getUniqueId());
        object.put("settings", BadPlayerSettingsSerializer.serialize(getSettings()));
        object.put("punish", getPunished());
        object.put("permissions", getPermissions());
        object.put("version", getVersion().toString());
		// TODO
		return object;
	}

    public Locale getLocale() {
		return ObjectUtils.getOr(getDbObject(), "locale", Locale.FRENCH_FRANCE);
	}

    public boolean isOnline() {
        return BadPlayer.has(getName());
    }

    public BadPlayer getOnlineBadPlayer() {
        return isOnline() ? BadPlayer.get(getName()) : null;
    }

}