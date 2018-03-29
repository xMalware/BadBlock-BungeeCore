package fr.badblock.bungee.players;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.BSONObject;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

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
import fr.toenga.common.utils.permissions.PermissionsManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadOfflinePlayer
{

	private 			String						name;
	private transient	BSONObject	  				dbObject;

	private transient	Callback<BadOfflinePlayer>	callback;
	private transient	List<Callback<BadPlayer>> 	loadedCallbacks;
	private 			boolean						loaded;

	private 			Permissible					permissions;
	private 			Punished					punished;

	private				BadPlayerSettings			settings;

	public BadOfflinePlayer(String name, Callback<BadOfflinePlayer> callback)
	{
		setName(name);
		setCallback(callback);
		setLoadedCallbacks(new ArrayList<>());
		System.out.println("load offlineplayer");
		loadData();
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
				loadData();
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

	protected void loadData()
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

				query.put("name", getName().toLowerCase());

				DBCursor cursor = collection.find(query); 
				boolean find = cursor.hasNext();

				if (find)
				{
					setDbObject(cursor.next());
					BadBungee.log("§c" + getName() + " exists in the player table.");

					punished = new Punished(getJsonObject("punish"));
					if (PermissionsManager.getManager() != null)
					{
						permissions = PermissionsManager.getManager().loadPermissible( getJsonObject("permissions") );
					}

					setSettings(new BadPlayerSettings(getJsonObject("settings")));

				}
				else
				{
					// Le joueur n'existe pas
					punished = new Punished();
					permissions = new Permissible();
					settings = new BadPlayerSettings();

					BadBungee.log(getName() + " doesn't exist in the player table.");
					BadBungee.log("§aCreating it...");

					BasicDBObject obj = getSavedObject();

					setDbObject(obj);
					collection.insert(obj);

					BadBungee.log("§aCreated!");
				}
				setLoaded(true);
				callback.done(BadOfflinePlayer.this, null);
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

		return getPermissions().hasPermission(permission);
	}

	public JsonObject getJsonObject(String part)
	{
		//FIXME vraiment pas optimisé, à voir si il y a mieux

		if(dbObject.containsField(part))
		{
			String value = dbObject.get(part).toString();
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
		object.put("lastIp", "");
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

}