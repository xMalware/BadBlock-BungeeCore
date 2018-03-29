package fr.badblock.bungee.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bson.BSONObject;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.layer.BadPlayerSettings;
import fr.badblock.bungee.players.layer.BadPlayerSettingsSerializer;
import fr.badblock.bungee.utils.ObjectUtils;
import fr.badblock.bungee.utils.mongodb.SynchroMongoDBGetter;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import fr.toenga.common.utils.bungee.Punished;
import fr.toenga.common.utils.data.Callback;
import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.common.utils.i18n.I18n;
import fr.toenga.common.utils.i18n.Locale;
import fr.toenga.common.utils.permissions.PermissionUser;
import fr.toenga.common.utils.permissions.PermissionsManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadOfflinePlayer
{

	private 			String						name;
	private				BadIP						lastIp;
	private 			UUID						uniqueId;
	private transient	BSONObject	  				dbObject;

	private transient	Callback<BadOfflinePlayer>	callback;
	private transient	List<Callback<BadPlayer>> 	loadedCallbacks;
	private 			boolean						loaded;

	private 			PermissionUser				permissions;
	private 			Punished					punished;

	private				BadPlayerSettings			settings;
	private 			int							version;
	private				boolean						found				= false;

	public BadOfflinePlayer(String name)
	{
		setName(name);
		setCallback(callback);
		setLoadedCallbacks(new ArrayList<>());
		System.out.println("load offlineplayer");
		loadData(true);
	}

	public void registerLoadedCallback(Callback<BadPlayer> callback)
	{
		if (isLoaded())
		{
			System.out.println("set as loaded!");
			callback.done((BadPlayer) this, null);
			return;	
		}
		System.out.println("added as callback");
		getLoadedCallbacks().add(callback);
	}

	public BadOfflinePlayer(String name, boolean create)
	{
		setName(name);
		loadData(create);
	}

	public static BadOfflinePlayer get(String name)
	{
		BadOfflinePlayer p = new BadOfflinePlayer(name, false);
		if (!p.isFound())
		{
			return null;
		}
		else
		{
			return p;
		}
	}

	public void updateSettings()
	{
		updateData("settings", BadPlayerSettingsSerializer.serialize(settings));
	}

	public void updateVersion()
	{
		updateData("version", getVersion());
	}

	public void updateLastIp()
	{
		updateData("lastIp", getLastIp().toString());
	}

	public void updateData(String key, Object value)
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{

			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("players");
				BasicDBObject query = new BasicDBObject();
				BasicDBObject update = new BasicDBObject(key, value);

				query.put("name", getName().toLowerCase());
				BasicDBObject updater = new BasicDBObject("$set", update);

				System.out.println(key + " : " + value);
				collection.update(query, updater);
				System.out.println("update " + key);
				loadData(true);
				if (callback != null)
				{
					callback.done(BadOfflinePlayer.this, null);
					callback = null;
				}
			}

		});
	}

	public void updateLastServer(ProxiedPlayer proxiedPlayer)
	{
		updateData("lastServer", proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null ? proxiedPlayer.getServer().getInfo().getName() : "");
	}

	protected void loadData(boolean create)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("name", getName().toLowerCase());
		DBObject obj = new SynchroMongoDBGetter("players", query).getDbObject();
		if (obj != null)
		{
			setDbObject(obj);
			BadBungee.log("§c" + getName() + " exists in the player table.");
			setName(getString("realName"));
			setLastIp(BadIP.fromString(getString("lastIp")));
			setUniqueId(UUID.fromString(getString("uniqueId")));
			setSettings(BadPlayerSettingsSerializer.deserialize(getString("settings")));
			setPunished(new Punished(getJsonObject("punish")));
			if (PermissionsManager.getManager() != null)
			{
				setPermissions(new PermissionUser(getJsonObject("permissions")));
			}
			setVersion(Integer.valueOf(getString("version")));
		}
		else
		{
			BadBungee.log(getName() + " doesn't exist in the player table.");
			if (create)
			{
				insert();
			}
			else
			{
				setFound(false);
			}
		}
	}

	private void insert()
	{
		BadBungee.log("§aCreating it...");
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				punished = new Punished();
				permissions = new PermissionUser(new HashMap<>(), new ArrayList<>());
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

	public String[] getTranslatedMessages(String key, Object... objects)
	{
		return I18n.getInstance().get(getLocale(), key, objects);
	}

	public boolean hasPermission(String permission)
	{
		if (getPermissions() == null)
		{
			return false;
		}
		return getPermissions().hasPermission("bungee", permission);
	}

	private String getString(String part)
	{
		if (getDbObject().containsField(part))
		{
			return getDbObject().get(part).toString();
		}
		else
		{
			return "";
		}
	}

	public JsonObject getJsonObject(String part)
	{
		//FIXME vraiment pas optimisé, à voir si il y a mieux
		if (getDbObject().containsField(part))
		{
			String value = getDbObject().get(part).toString();
			return GsonUtils.getPrettyGson().fromJson(value, JsonObject.class);
		}
		else
		{
			return new JsonObject();
		}
	}

	public BasicDBObject getSavedObject()
	{
		BasicDBObject object = new BasicDBObject();
		object.put("name", getName().toLowerCase());
		object.put("realName", getName());
		object.put("lastIp", getLastIp());
		object.put("uniqueId", UUID.randomUUID().toString());
		object.put("settings", BadPlayerSettingsSerializer.serialize(settings));
		object.put("punish", punished.getDBObject());
		object.put("permissions", permissions.getDBObject());
		object.put("version", "0");
		// TODO
		return object;
	}

	public Locale getLocale()
	{
		return ObjectUtils.getOr(getDbObject(), "locale", Locale.FRENCH_FRANCE);
	}

	public boolean isOnline()
	{
		return BadPlayer.has(getName());
	}

	public BadPlayer getOnlineBadPlayer()
	{
		return isOnline() ? BadPlayer.get(getName()) : null;
	}

}