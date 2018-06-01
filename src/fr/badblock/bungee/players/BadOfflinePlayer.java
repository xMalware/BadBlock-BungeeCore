package fr.badblock.bungee.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.layer.BadPlayerSettings;
import fr.badblock.bungee.utils.ObjectUtils;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import fr.toenga.common.utils.bungee.Punished;
import fr.toenga.common.utils.data.Callback;
import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.common.utils.i18n.I18n;
import fr.toenga.common.utils.i18n.Locale;
import fr.toenga.common.utils.permissions.Permissible;
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
	private				String						lastIp;
	private 			UUID						uniqueId;
	private 			BasicDBObject	  			dbObject;

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
		loadData(true);
	}

	public void registerLoadedCallback(Callback<BadPlayer> callback)
	{
		if (isLoaded())
		{
			callback.done((BadPlayer) this, null);
			return;	
		}
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
		updateData("settings", settings.getDBObject());
	}

	public void updateVersion()
	{
		updateData("version", getVersion());
	}

	public void updateLastIp()
	{
		updateData("lastIp", getLastIp().toString());
	}

	public String getRawChatPrefix()
	{
		Permissible permissible = getPermissions().getHighestRank("bungee", true);
		if (permissible == null)
		{
			return "";
		}
		return permissible.getRawPrefix("prefix");
	}
	
	public String getRawChatSuffix()
	{
		Permissible permissible = getPermissions().getHighestRank("bungee", true);
		if (permissible == null)
		{
			return "";
		}
		return permissible.getRawSuffix("chat");
	}
	
	public String getFullChatName(Locale locale)
	{
		// TODO /gnick support
		Permissible permissible = getPermissions().getHighestRank("bungee", true);
		if (permissible == null)
		{
			return getName();
		}
		return permissible.getPrefix(locale, "chat") + getName() + permissible.getSuffix(locale, "chat");
	}

	public void saveData() throws Exception
	{
		if (!isLoaded())
		{
			throw new Exception("Trying to save data with unloaded data.");
		}
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{

			@Override
			public void run(MongoService mongoService)
			{
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("players");
				BasicDBObject query = new BasicDBObject();

				query.put("name", getName().toLowerCase());

				collection.update(query, getDbObject());
			}

		});
	}

	public DBObject mergeData(DBObject base, JsonObject toAdd, boolean basePoint)
	{
		for(final Entry<String, JsonElement> entry : toAdd.entrySet())
		{
			String key = entry.getKey();
			JsonElement element = toAdd.get(key);
			if (!base.containsField(key) || !element.isJsonObject())
			{
				base.put(key, element);
				return base;
			} else {
				try
				{
					DBObject dbObject = mergeData(ObjectUtils.toDbObject(base.get(key)), element.getAsJsonObject(), false);
					base.put(key, dbObject);
					if (basePoint)
					{
						saveData();
					}
					return dbObject;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
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

				collection.update(query, updater);

				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
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
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		DBCollection dbCollection = mongoService.getDb().getCollection("players");
		DBCursor cursor = dbCollection.find(query);
		if (cursor.hasNext())
		{
			setDbObject((BasicDBObject) cursor.next());
			System.out.print("set dbobject: " + getDbObject());
			BadBungee.log("§c" + getName() + " exists in the player table.");
			setName(getString("realName"));
			setLastIp(getString("lastIp"));
			setUniqueId(UUID.fromString(getString("uniqueId")));
			setSettings(new BadPlayerSettings(getJsonElement("settings").getAsJsonObject()));
			setPunished(new Punished(getJsonElement("punish").getAsJsonObject()));
			if (PermissionsManager.getManager() != null)
			{
				setPermissions(new PermissionUser(getJsonElement("permissions").getAsJsonObject()));
			}
			try
			{
				setVersion(Integer.valueOf(getString("version")));
			}
			catch (Exception error)
			{

			}
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
		BasicDBObject obj = getSavedObject();
		setDbObject(obj);
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			@Override
			public void run(MongoService mongoService)
			{
				punished = new Punished();
				permissions = new PermissionUser(new HashMap<>(), new ArrayList<>());
				settings = new BadPlayerSettings();
				uniqueId = UUID.randomUUID();
				version = 0;
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

	@SuppressWarnings("deprecation")
	private String getString(String part)
	{
		if (getDbObject().containsKey(part))
		{
			return getDbObject().get(part).toString();
		}
		else
		{
			return "";
		}
	}

	public JsonElement getJsonElement(String part)
	{
		//FIXME vraiment pas optimisé, à voir si il y a mieux
		if (getDbObject().containsField(part))
		{
			String serialize = JSON.serialize(getDbObject().get(part));
			JsonElement jsonElement = GsonUtils.toJsonElement(serialize);
			return jsonElement;
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
		object.put("settings", settings.getDBObject());
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