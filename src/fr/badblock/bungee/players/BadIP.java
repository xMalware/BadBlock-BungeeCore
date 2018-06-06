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

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.api.common.utils.data.Callback;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.utils.ObjectUtils;
import fr.badblock.bungee.utils.time.TimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
/**
 * 
 * BadIP
 * 
 * @author xMalware
 *
 */
public final class BadIP
{

	/**
	 * IP
	 * @param Set the IP
	 * @return Returns the IP
	 */
	private 			String						ip;
	
	/**
	 * DBObject
	 * @param Set the DBObject
	 * @return Returns the DBObject
	 */
	private transient	DBObject	  				dbObject;
	
	/**
	 * Callback
	 * @param Set the callback
	 * @return Returns the callback
	 */
	private transient	Callback<BadIP>				callback;
	
	/**
	 * Loaded callbacks
	 * @param Set the loaded callbacks
	 * @return Returns the loaded callbacks
	 */
	private transient	List<Callback<BadIP>> 		loadedCallbacks;

	/**
	 * Loaded
	 * @param Set if loaded
	 * @return Returns if loaded
	 */
	private 			boolean						loaded;
	
	/**
	 * VPN
	 * @param Set if VPN
	 * @return Returns if VPN
	 */
	private 			boolean						vpn;
	
	/**
	 * Found
	 * @param Set if found
	 * @return Returns if found
	 */
	private 			boolean						found;

	/**
	 * Punished
	 * @param Set punished
	 * @return Returns punished
	 */
	private 			Punished					punished;

	/**
	 * Constructor
	 * @param IP address
	 */
	public BadIP(String ip)
	{
		// Set IP address
		setIp(ip);
		// Set callback
		setCallback(callback);
		// Set loaded callbacks
		setLoadedCallbacks(new ArrayList<>());
		// Load data
		loadData(true);
	}

	/**
	 * Register loaded callbacks
	 * @param Callback
	 */
	public void registerLoadedCallback(Callback<BadIP> callback)
	{
		// If it's loaded
		if (isLoaded())
		{
			// Ok, call back
			callback.done(this, null);
			// We stop there
			return;
		}
		// Add a loaded callback
		getLoadedCallbacks().add(callback);
	}

	/**+
	 * Constructor
	 * @param IP address
	 * @param Create
	 */
	public BadIP(String ip, boolean create)
	{
		// Set IP address
		setIp(ip);
		// Load data
		loadData(create);
	}

	/**
	 * Get BadIP object
	 * @param IP address
	 * @return the BadIP object
	 */
	public static BadIP get(String ip)
	{
		// Create a BadIP
		BadIP badIp = new BadIP(ip, false);
		// If it's not found
		if (!badIp.isFound())
		{
			// Returns null
			return null;
		}
		else
		{
			// Returns BadIP object
			return badIp;
		}
	}

	/**
	 * Update VPN object
	 */
	public void updateVPN()
	{
		// Update data
		updateData("vpn", isVpn());
	}

	/**
	 * Save data
	 * @throws Exception
	 */
	public void saveData() throws Exception
	{
		// If it's not loaded
		if (!isLoaded())
		{
			// Exception!
			throw new Exception("Trying to save data with unloaded data.");
		}
		// Mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			/**
			 * Run asynchronously
			 */
			@Override
			public void run(MongoService mongoService)
			{
				// Get database
				DB db = mongoService.getDb();
				// Get collection
				DBCollection collection = db.getCollection("ips");
				// New query
				BasicDBObject query = new BasicDBObject();

				// Put IP in query
				query.put("ip", getIp().toLowerCase());
				
				// Update data
				collection.update(query, getDbObject());
			}

		});
	}

	/**
	 * Merge data
	 * @param Base DBObject
	 * @param toAdd
	 * @param basePoint
	 * @return
	 */
	public DBObject mergeData(DBObject base, JsonObject toAdd, boolean basePoint)
	{
		// For each element to add
		for (final Entry<String, JsonElement> entry : toAdd.entrySet())
		{
			// Get key
			String key = entry.getKey();
			// Get element
			JsonElement element = toAdd.get(key);
			// If it's not a json object
			if (!base.containsField(key) || !element.isJsonObject())
			{
				// Put the element in the base object
				base.put(key, element);
				// Returns the base
				return base;
			}
			// If it's a json object
			else
			{
				// Try to?
				try
				{
					// Merge data from the DBObject
					DBObject dbObject = mergeData(ObjectUtils.toDbObject(base.get(key)), element.getAsJsonObject(), false);
					// Add the database object in the base object
					base.put(key, dbObject);
					// If it's the base point
					if (basePoint)
					{
						// We just save the data
						saveData();
					}
					// Returns the database object
					return dbObject;
				}
				// Error case
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
			}
		}
		// Returns null
		return null;
	}

	/**
	 * Update data
	 * @param key
	 * @param value
	 */
	public void updateData(String key, Object value)
	{
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			/**
			 * Run asynchronously
			 */
			@Override
			public void run(MongoService mongoService)
			{
				// Get database
				DB db = mongoService.getDb();
				// Get database collection
				DBCollection collection = db.getCollection("ips");
				// New query
				BasicDBObject query = new BasicDBObject();
				// Update query
				BasicDBObject update = new BasicDBObject(key, value);

				// Query IP
				query.put("ip", getIp().toLowerCase());
				
				// Updater with all data to set
				BasicDBObject updater = new BasicDBObject("$set", update);

				// Update data in collection
				collection.update(query, updater);

				// Sleep100 ms
				TimeUtils.sleep(100);
				
				// Load data
				loadData(true);
				
				// Callback not null
				if (callback != null)
				{
					// Done!
					callback.done(BadIP.this, null);
					// Remove the callback
					callback = null;
				}
			}

		});
	}

	/**
	 * Load data
	 * @param If we create data if it doesn't exist
	 */
	protected void loadData(boolean create)
	{
		// New query data
		BasicDBObject query = new BasicDBObject();
		// Query IP
		query.append("ip", getIp().toLowerCase());
		
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get databse collection
		DBCollection dbCollection = mongoService.getDb().getCollection("players");
		
		// Find each data with this IP
		DBCursor cursor = dbCollection.find(query);
		
		// If we find anything
		if (cursor.hasNext())
		{
			// Set the database object
			setDbObject(cursor.next());
			// Log
			BadBungee.log("§c" + getIp() + " exists in the player table.");
			// Set IP
			setIp(getString("ip"));
			// Set VPN boolean
			setVpn(getBoolean("vpn"));
			// Set punished
			setPunished(new Punished(getJsonElement("punish").getAsJsonObject()));
		}
		else
		{
			// Log
			BadBungee.log(getIp() + " doesn't exist in the player table.");
			// If we can create
			if (create)
			{
				// So we insert
				insert();
			}
			else
			{
				// Not found.
				setFound(false);
			}
		}
	}

	/**
	 * Insert BadIP
	 */
	private void insert()
	{
		// Log
		BadBungee.log("§aCreating it...");
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService)
		{
			/**
			 * Run asynchronously
			 * @param mongoService
			 */
			@Override
			public void run(MongoService mongoService)
			{
				// New punished data
				punished = new Punished();
				// Set VPN state
				vpn = false;
				// Get saved object
				BasicDBObject obj = getSavedObject();
				// Set the saved object
				setDbObject(obj);
				// Get the database
				DB db = mongoService.getDb();
				// Get the database collection
				DBCollection collection = db.getCollection("players");
				// Insert the object in the collection
				collection.insert(obj);
				// Log
				BadBungee.log("§aCreated!");
			}
		});
	}

	/**
	 * Get a string
	 * @param part
	 * @return
	 */
	private String getString(String part)
	{
		// If the database object contains the field
		if (getDbObject().containsField(part))
		{
			// Returns the field content
			return getDbObject().get(part).toString();
		}
		else
		{
			// Returns an empty string
			return "";
		}
	}
	
	/**
	 * Get a boolean
	 * @param part
	 * @return
	 */
	private boolean getBoolean(String part)
	{
		// Parsing a boolean from getString();
		return Boolean.parseBoolean(getString(part));
	}

	@SuppressWarnings("deprecation")
	/**
	 * Get a JsonElement
	 * @param part
	 * @return
	 */
	public JsonElement getJsonElement(String part)
	{
		// If the database object contains the field
		if (getDbObject().containsField(part))
		{
			// Serialize the object
			String serialize = JSON.serialize(getDbObject().get(part));
			// Deserialize to a JsonElement
			JsonElement jsonElement = GsonUtils.toJsonElement(serialize);
			// Returns the element
			return jsonElement;
		}
		else
		{
			// Returns a new json object
			return new JsonObject();
		}
	}

	/**
	 * Get saved object
	 * @return BasicDBObject
	 */
	public BasicDBObject getSavedObject()
	{
		// Create a new object
		BasicDBObject object = new BasicDBObject();
		// Set the IP
		object.put("ip", getIp().toLowerCase());
		// Set the VPN boolean
		object.put("vpn", isVpn());
		// Set the punished object
		object.put("punish", punished.getDBObject());
		// Returns the object
		return object;
	}

}