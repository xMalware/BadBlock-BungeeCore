package fr.badblock.bungee.players;

import java.util.UUID;

import org.bson.BSONObject;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import fr.badblock.bungee.BadBungee;
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

@EqualsAndHashCode(callSuper = false)
@Data
public class BadOfflinePlayer
{
	
	private String						name;
	private BSONObject	  				dbObject;
	private Callback<BadOfflinePlayer>	callback;
	
	private Permissible					permissions;
	private Punished					punished;

	public BadOfflinePlayer(String name, Callback<BadOfflinePlayer> callback)
	{
		setName(name);
		setCallback(callback);
		loadData();
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

					permissions = PermissionsManager.getManager().loadPermissible( getJsonObject("permissions") );
					punished = Punished.fromJson( getJsonObject("punish") );
				}
				else
				{
					// Le joueur n'existe pas
					punished = new Punished();
					permissions = new Permissible();

					BadBungee.log(getName() + " doesn't exist in the player table.");
					BadBungee.log("§aCreating it...");

					BasicDBObject obj = getSavedObject();

					setDbObject(obj);
					collection.insert(obj);

					BadBungee.log("§aCreated!");
				}
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
		object.put("version", "0");
		// FIXME

		return object;
	}

	public Locale getLocale()
	{
		return ObjectUtils.getOr(getDbObject(), "locale", Locale.FRENCH_FRANCE);
	}

}