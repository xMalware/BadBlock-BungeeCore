package fr.badblock.bungee.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.utils.ObjectUtils;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import fr.toenga.common.utils.bungee.Punished;
import fr.toenga.common.utils.data.Callback;
import fr.toenga.common.utils.general.GsonUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BadIP
{

	private 			String						ip;
	
	private transient	DBObject	  				dbObject;
	private transient	Callback<BadIP>				callback;
	private transient	List<Callback<BadIP>> 		loadedCallbacks;

	private 			boolean						loaded;
	private 			boolean						vpn;
	private 			boolean						found;

	private 			Punished					punished;

	public BadIP(String ip)
	{
		setIp(ip);
		setCallback(callback);
		setLoadedCallbacks(new ArrayList<>());
		loadData(true);
	}

	public void registerLoadedCallback(Callback<BadIP> callback)
	{
		if (isLoaded())
		{
			callback.done(this, null);
			return;	
		}
		getLoadedCallbacks().add(callback);
	}

	public BadIP(String ip, boolean create)
	{
		setIp(ip);
		loadData(create);
	}

	public static BadIP get(String ip)
	{
		BadIP p = new BadIP(ip, false);
		if (!p.isFound())
		{
			return null;
		}
		else
		{
			return p;
		}
	}

	public void updateVPN()
	{
		updateData("vpn", isVpn());
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
				DBCollection collection = db.getCollection("ips");
				BasicDBObject query = new BasicDBObject();

				query.put("ip", getIp().toLowerCase());

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
				DBCollection collection = db.getCollection("ips");
				BasicDBObject query = new BasicDBObject();
				BasicDBObject update = new BasicDBObject(key, value);

				query.put("ip", getIp().toLowerCase());
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
					callback.done(BadIP.this, null);
					callback = null;
				}
			}

		});
	}

	protected void loadData(boolean create)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("ip", getIp().toLowerCase());
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		DBCollection dbCollection = mongoService.getDb().getCollection("players");
		DBCursor cursor = dbCollection.find(query);
		if (cursor.hasNext())
		{
			setDbObject(cursor.next());
			BadBungee.log("§c" + getIp() + " exists in the player table.");
			setIp(getString("ip"));
			setVpn(getBoolean("vpn"));
			setPunished(new Punished(getJsonElement("punish").getAsJsonObject()));
		}
		else
		{
			BadBungee.log(getIp() + " doesn't exist in the player table.");
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
				vpn = false;
				BasicDBObject obj = getSavedObject();
				setDbObject(obj);
				DB db = mongoService.getDb();
				DBCollection collection = db.getCollection("players");
				collection.insert(obj);
				BadBungee.log("§aCreated!");
			}
		});
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
	
	private boolean getBoolean(String part)
	{
		return Boolean.parseBoolean(getString(part));
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
		object.put("ip", getIp().toLowerCase());
		object.put("vpn", isVpn());
		object.put("punish", punished.getDBObject());
		return object;
	}

}