package fr.badblock.bungee.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.permissions.PermissionsManager;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.config.BadBungeeConfig;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.layer.BadPlayerSettings;
import fr.badblock.bungee.utils.ObjectUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.time.TimeUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@SuppressWarnings("deprecation")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
/**
 * 
 * BadOfflinePlayer
 * 
 * @author xMalware
 *
 */
public class BadOfflinePlayer {

	/**
	 * Get a BadOfflinePlayer object
	 * 
	 * @param Username
	 * @return a BadOfflinePlayer object
	 */
	public static BadOfflinePlayer get(String name) {
		return get(name, false);
	}

	/**
	 * Get a BadOfflinePlayer object
	 * 
	 * @param Username
	 * @return a BadOfflinePlayer object
	 */
	public static BadOfflinePlayer get(String name, boolean create) {
		// New object
		BadOfflinePlayer badOfflinePlayer = new BadOfflinePlayer(name, create);

		// Returns the object
		return badOfflinePlayer;
	}

	/**
	 * Get a BadOfflinePlayer object
	 * 
	 * @param UUID
	 * @return a BadOfflinePlayer object
	 */
	public static BadOfflinePlayer get(UUID uuid) {
		// New object
		BadOfflinePlayer badOfflinePlayer = new BadOfflinePlayer(uuid, false);
		// Data not found?
		if (!badOfflinePlayer.isFound()) {
			// Returns null
			return null;
		} else {
			// Returns the object
			return badOfflinePlayer;
		}
	}

	/**
	 * Callback
	 * 
	 * @param Set
	 *            the callback
	 * @return Returns the callback
	 */
	private transient Callback<BadOfflinePlayer> callback;

	/**
	 * Database object
	 * 
	 * @param Set
	 *            the database object
	 * @return Returns the database object
	 */
	private BasicDBObject dbObject;

	/**
	 * Found
	 * 
	 * @param Set
	 *            if data was found
	 * @return Returns if data was found
	 */
	private boolean found = false;

	private boolean isNew = false;

	/**
	 * Last IP
	 * 
	 * @param Set
	 *            the last IP
	 * @return Returns the last IP
	 */
	private String lastIp;

	/**
	 * Loaded
	 * 
	 * @param Set
	 *            if data is loaded
	 * @return Returns if data is loaded
	 */
	private boolean loaded;

	/**
	 * Loaded callbacks
	 * 
	 * @param Set
	 *            the loaded callbacks
	 * @return Returns the loaded callbacks
	 */
	private transient List<Callback<BadPlayer>> loadedCallbacks;

	/**
	 * Name
	 * 
	 * @param Set
	 *            the new username
	 * @return Returns the current username
	 */
	private String name;

	/**
	 * Nickname
	 * 
	 * @param Set
	 *            the new nickname
	 * @return Returns the current nickname
	 */
	private String nickname;

	/**
	 * Last server
	 * 
	 * @param Set
	 *            the new "last server" :o
	 * @return Returns the current last server
	 */
	private String lastServer;

	/**
	 * Permissions
	 * 
	 * @param Set
	 *            the permissions
	 * @return Returns the permissions
	 */
	private PermissionUser permissions;

	/**
	 * Punished
	 * 
	 * @param Set
	 *            the punished object
	 * @return Returns the punished object
	 */
	private Punished punished;

	/**
	 * Settings
	 * 
	 * @param Set
	 *            the settings
	 * @return Returns the settings
	 */
	private BadPlayerSettings settings;

	/**
	 * Unique ID
	 * 
	 * @param Set
	 *            the unique ID
	 * @return Returns the unique ID
	 */
	private UUID uniqueId;

	/**
	 * Version
	 * 
	 * @param Set
	 *            the version
	 * @return Returns the version
	 */
	private int version;

	/**
	 * Auth key
	 * 
	 * @param Set
	 *            the auth key
	 * @return Returns the auth key
	 */
	private String authKey;

	/**
	 * Login password
	 * 
	 * @param Set
	 *            the login password
	 * @return Returns the login password
	 */
	private String loginPassword;

	/**
	 * Online mode
	 * 
	 * @param Set
	 *            the online mode
	 * @return Returns the online mode
	 */
	private boolean onlineMode;

	/**
	 * Last login
	 * 
	 * @param Set
	 *            the last login
	 * @return Returns the last login
	 */
	private long lastLogin;

	/**
	 * Constructor (auto-create)
	 * 
	 * @param Username
	 */
	public BadOfflinePlayer(String name) {
		// Set username
		setName(name);
		// Set callback
		setCallback(callback);
		// Set loaded callbacks
		setLoadedCallbacks(new ArrayList<>());
		// Load data & create
		loadData(true);
	}

	/**
	 * Constructor
	 * 
	 * @param Username
	 * @param Auto-create?
	 */
	public BadOfflinePlayer(String name, boolean create) {
		// Set name
		setName(name);
		// Load data (create or not?)
		loadData(create);
	}

	/**
	 * Constructor
	 * 
	 * @param Unique
	 *            ID
	 * @param Auto-create?
	 */
	public BadOfflinePlayer(UUID uuid, boolean create) {
		// Set unique id
		setUniqueId(uuid);
		// Set callback
		setCallback(callback);
		// Set loaded callbacks
		setLoadedCallbacks(new ArrayList<>());
		// Load data & create
		loadData(create);
	}

	/**
	 * Get full chat name
	 * 
	 * @param Locale
	 *            shown
	 * @return
	 */
	public String getFullChatName(Locale locale) {
		// TODO /gnick support
		// Get permissible object
		Permissible permissible = getPermissions().getHighestRank("bungee", true);
		// If permissible object is null
		if (permissible == null) {
			// Returns only the name
			return getName();
		}
		// Returns the prefix + username + suffix
		return permissible.getPrefix(locale, "chat") + getName() + permissible.getSuffix(locale, "chat");
	}

	/**
	 * Get JsonElement from the database object
	 * 
	 * @param part
	 * @return
	 */
	public JsonElement getJsonElement(String part) {
		// If the database object contains the key
		if (getDbObject().containsField(part)) {
			// Serialize the data
			String serialize = JSON.serialize(getDbObject().get(part));
			// Deserialize as a JsonElement
			JsonElement jsonElement = GsonUtils.toJsonElement(serialize);
			// Returns the element
			return jsonElement;
		} else {
			// Returns a new JsonObject
			return new JsonObject();
		}
	}

	/**
	 * Get player locale
	 * 
	 * @return a Locale object
	 */
	public Locale getLocale() {
		// Get from the database object
		return ObjectUtils.getOr(getDbObject(), "locale", Locale.FRENCH_FRANCE);
	}

	/**
	 * Get the online BadPlayer
	 * 
	 * @return
	 */
	public BadPlayer getOnlineBadPlayer() {
		// If he's offline, returns null
		if (!isOnline()) {
			// Returns null
			return null;
		}

		// If he's online on this node
		if (BadPlayer.has(getName().toLowerCase())) {
			// Returns the local player
			return BadPlayer.get(getName());
		}

		// Returns the BadPlayer object
		return BungeeManager.getInstance().getBadPlayer(getName().toLowerCase());
	}

	/**
	 * Get raw chat prefix
	 * 
	 * @return a String
	 */
	public String getRawChatPrefix() {
		// Get permissible object
		Permissible permissible = getPermissions().getHighestRank("bungee", true);
		// If permissible object is null
		if (permissible == null) {
			// Returns an empty string
			return "";
		}
		// Returns the raw chat prefix
		return permissible.getRawPrefix("chat");
	}

	/**
	 * Get raw chat suffix
	 * 
	 * @return a String
	 */
	public String getRawChatSuffix() {
		// Get permissible object
		Permissible permissible = getPermissions().getHighestRank("bungee", true);
		// If premissible object is null
		if (permissible == null) {
			// Returns an empty string
			return "";
		}
		// Returns the raw chat suffix
		return permissible.getRawSuffix("chat");
	}

	/**
	 * Get saved json object
	 * 
	 * @return a JsonObject
	 */
	public JsonObject getSavedJsonObject() {
		return new JsonParser().parse(JSON.serialize(getSavedObject())).getAsJsonObject();
	}

	/**
	 * Get saved object
	 * 
	 * @return a BasicDBObject object
	 */
	public BasicDBObject getSavedObject() {
		// Create a new BasicDBObject object
		BasicDBObject object = new BasicDBObject();
		// Put the lower-case name
		object.put("name", getName().toLowerCase());
		// Put the real name
		object.put("realName", getName());
		// Put the nickname
		object.put("nickname", getNickname());
		// Put the last IP
		object.put("lastIp", getLastIp());
		// Put the unique ID
		object.put("uniqueId", getUniqueId());
		// Put the settings
		object.put("settings", settings != null ? settings.getDBObject() : null);
		// Put the punish object
		object.put("punish", punished != null ? punished.getDBObject() : null);
		// Put the permissions
		object.put("permissions", permissions != null ? permissions.getDBObject() : null);
		// Put the user version
		object.put("version", "0");
		// Put the online mode
		object.put("onlineMode", onlineMode);
		// Put the last login
		object.put("lastLogin", lastLogin);
		// Put the login password
		object.put("loginPassword", loginPassword);
		// Put the auth key
		object.put("authKey", authKey);
		// TODO?

		// Returns the saved object
		return object;
	}

	/**
	 * Get a string from the database object
	 * 
	 * @param Field
	 * @return a String
	 */
	private String getString(String part) {
		// If the database object contains the key
		if (getDbObject().containsKey(part) && getDbObject().get(part) != null) {
			// Returns the data as a string
			return getDbObject().get(part).toString();
		} else {
			// Returns an empty string
			return "";
		}
	}

	/**
	 * Get a boolean from the database object
	 * 
	 * @param Field
	 * @return a String
	 */
	private boolean getBoolean(String part) {
		// If the database object contains the key
		if (getDbObject().containsKey(part) && getDbObject().get(part) != null) {
			// Returns the data as a string
			return Boolean.parseBoolean(getDbObject().get(part).toString());
		} else {
			// Returns an empty string
			return false;
		}
	}

	/**
	 * Get a translated message
	 * 
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 * @return
	 */
	public String getTranslatedMessage(String key, int[] indexesToTranslate, Object... objects) {
		// Use another method
		return getTranslatedMessages(key, indexesToTranslate, objects)[0];
	}

	/**
	 * Get translated messages
	 * 
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 * @return
	 */
	public String[] getTranslatedMessages(String key, int[] indexesToTranslate, Object... objects) {
		// Get from our layer
		return I19n.getMessages(getLocale(), key, indexesToTranslate, objects);
	}

	/**
	 * If the player has the permission
	 * 
	 * @param Permission
	 *            to check
	 * @return Returns if the player has the requested permission
	 */
	public boolean hasPermission(String permission) {
		// No permisisons
		if (getPermissions() == null) {
			// So he don't have the permision
			return false;
		}
		// Check with the permisison system
		return getPermissions().hasPermission("bungee", permission);
	}

	/**
	 * Load default values
	 */
	public void loadDefaultValues() {
		// Set new punished object
		punished = new Punished();
		// Set new permission object
		permissions = new PermissionUser(new HashMap<>(), new ArrayList<>());
		// Set default
		HashMap<String, Long> defaultBungeePermissions = new HashMap<>();
		defaultBungeePermissions.put("default", -1L);
		permissions.getGroups().put("bungee", defaultBungeePermissions);
		// Set new settins
		settings = new BadPlayerSettings(new JsonObject());
		// Set a random unique ID
		uniqueId = UUID.randomUUID();
		// Set the version
		version = 0;
		// Set the username
		nickname = null;
		// Set the online mode
		onlineMode = BadBungeeConfig.DEFAULT_ONLINEMODE;
		// Set the last login
		lastLogin = 0;
		// Set the login password
		loginPassword = null;
		// Set the auth key
		authKey = null;
		// Get saved object
		BasicDBObject obj = getSavedObject();
		// Set database object
		setDbObject(obj);
		// Set found
		setFound(true);
		// Set new
		setNew(true);
	}

	/**
	 * Insert data
	 */
	public void insert() {
		// Log => create data
		BadBungee.log("§aCreating it...");
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Run asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get database
				DB db = mongoService.getDb();
				// Get database collection
				DBCollection collection = db.getCollection("players");
				// Insert data into collection
				collection.insert(getSavedObject());
				// Log => created
				BadBungee.log("§aCreated!");
			}
		});
	}

	/**
	 * If this player is online
	 * 
	 * @return
	 */
	public boolean isOnline() {
		// Existing in the map?
		return BungeeManager.getInstance().hasUsername(getName());
	}

	/**
	 * Load data
	 * 
	 * @param Create
	 *            if data doesn't exist
	 */
	protected void loadData(boolean create) {
		// New query
		BasicDBObject query = new BasicDBObject();

		// If the username isn't null
		if (getName() != null) {
			// Add it to the query
			query.append("name", getName().toLowerCase());
		}
		// If the unique ID isn't null
		else if (getUniqueId() != null) {
			// Add it to the query
			query.append("uniqueId", getUniqueId().toString().toLowerCase());
		}

		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get database collection
		DBCollection dbCollection = mongoService.getDb().getCollection("players");
		// Get data
		DBCursor cursor = dbCollection.find(query);
		// If there's data
		if (cursor.hasNext()) {
			// Set database object by casting a 'basic DBObject'
			setDbObject((BasicDBObject) cursor.next());
			// Set name
			setName(getString("realName"));
			// Log => already exists!
			BadBungee.log("§c" + getName() + " exists in the player table.");
			// Set last IP
			setLastIp(getString("lastIp"));
			// Set nickname
			setNickname(getString("nickname"));
			// Set the auth key
			setAuthKey(getString("authKey"));
			// Set the login password
			setLoginPassword(getString("loginPassword"));
			// Set the online mode
			setOnlineMode(getBoolean("onlineMode"));
			// Set unique ID
			setUniqueId(UUID.fromString(getString("uniqueId")));
			// Set settings
			setSettings(new BadPlayerSettings(getJsonElement("settings").getAsJsonObject()));
			// Set punished
			setPunished(new Punished(getJsonElement("punish").getAsJsonObject()));

			// If the permission manager exists
			if (PermissionsManager.getManager() != null) {
				// Set permissions
				setPermissions(new PermissionUser(getJsonElement("permissions").getAsJsonObject()));
			}

			// Try to?
			try {
				// Set the version by parsing integer
				setVersion(Integer.parseInt(getString("version")));

				// Set the last login
				setLastLogin(Integer.parseInt(getString("lastLogin")));
			}
			// Error case
			catch (Exception error) {
				// Ok that's right. It can be empty...
			}

			// Set found
			setFound(true);

			// Set loaded
			setLoaded(true);

			// Set new
			setNew(false);

		} else {
			// Log => data doesn't exist
			BadBungee.log(getName() + " doesn't exist in the player table.");

			if (!isLoaded()) {
				// Load default values
				loadDefaultValues();
			}

			// Set loaded
			setLoaded(true);

			// If we are allowed to create data
			if (create) {
				// Insert data
				insert();

				// Set new
				setNew(false);
			}
			// If we're not allowed to create data
			else {
				// Not found..
				setFound(false);

				// Set new
				setNew(true);
			}
		}
	}

	/**
	 * Merge data
	 * 
	 * @param base
	 *            object
	 * @param toAdd
	 * @param basePoint
	 * @return
	 */
	public DBObject mergeData(DBObject base, JsonObject toAdd, boolean basePoint) {
		// For each object to add
		for (final Entry<String, JsonElement> entry : toAdd.entrySet()) {
			// Get object key
			String key = entry.getKey();
			// Get element
			JsonElement element = toAdd.get(key);
			// If it's not a JsonObject
			if (!base.containsField(key) || !element.isJsonObject()) {
				// Put JsonObject
				base.put(key, element);
				// Returns the base object
				return base;
			}
			// If it's a JsonObject
			else {
				// Try to?
				try {
					// Merge DBObject
					DBObject dbObject = mergeData(ObjectUtils.toDbObject(base.get(key)), element.getAsJsonObject(),
							false);
					// Put the DBObject into base
					base.put(key, dbObject);
					// If this is the base point
					if (basePoint) {
						// Save data
						saveData();
					}
					// Returns the database object
					return dbObject;
				}
				// Error case
				catch (Exception error) {
					// Print stack trace to logs
					error.printStackTrace();
				}
			}
		}
		// Returns null
		return null;
	}

	/**
	 * Register loaded callback
	 * 
	 * @param Callback
	 *            to register
	 */
	public void registerLoadedCallback(Callback<BadPlayer> callback) {
		// If the player is loaded
		if (isLoaded()) {
			// So we call back!
			callback.done((BadPlayer) this, null);
			// We stop there
			return;
		}
		// Add a callback
		getLoadedCallbacks().add(callback);
	}

	/**
	 * Save data
	 * 
	 * @throws Exception
	 *             in case the data isn't loaded
	 */
	public void saveData() throws Exception {
		// If the data isn't loaded
		if (!isLoaded()) {
			// Throw an exception
			throw new Exception("Trying to save data with unloaded data.");
		}
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Run asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get database
				DB db = mongoService.getDb();
				// Get database collection
				DBCollection collection = db.getCollection("players");
				// New query
				BasicDBObject query = new BasicDBObject();

				// Query name
				query.put("name", getName().toLowerCase());

				// Updater with all data to set
				BasicDBObject updater = new BasicDBObject("$set", getSavedObject());

				// Update data
				collection.update(query, updater);
			}

		});
	}

	/**
	 * Update data
	 * 
	 * @param key
	 * @param value
	 */
	public void updateData(String key, Object value) {
		updateData(new String[] { key }, new Object[] { value });
	}

	/**
	 * Update data
	 * 
	 * @param key
	 * @param value
	 */
	public void updateData(String[] keys, Object[] values) {
		// If the player is new
		if (isNew()) {
			// Don't do that!
			return;
		}
		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Run asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get database
				DB db = mongoService.getDb();
				// Get database collection
				DBCollection collection = db.getCollection("players");
				// New query
				BasicDBObject query = new BasicDBObject();
				// Update query
				BasicDBObject update = new BasicDBObject();

				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					Object value = values[i];
					update.put(key, value);
				}

				// Query name
				query.put("name", getName().toLowerCase());
				// Set the updater as a setter
				BasicDBObject updater = new BasicDBObject("$set", update);

				// Update the collection with the query & updater
				collection.update(query, updater);

				// Sleep 100 milliseconds
				TimeUtils.sleep(100);

				// Load data
				loadData(true);

				// If callback isn't null
				if (callback != null) {
					// Call back
					callback.done(BadOfflinePlayer.this, null);
					// Remove the callback
					callback = null;
				}
			}

		});
	}

	/**
	 * Update last IP
	 */
	public void updateLastIp() {
		// Update last IP
		updateData("lastIp", getLastIp().toString());
	}

	/**
	 * Update the nickname
	 */
	public void updateNickname() {
		// Update the nickname
		updateData("nickname", getNickname().toString());
	}

	/**
	 * Update punishments
	 */
	public void updatePunishments() {
		// Update punishments
		updateData("punish", getPunished().getDBObject());
	}

	/**
	 * Update online mode
	 */
	public void updateOnlineMode() {
		// Update online mode
		updateData("onlineMode", isOnlineMode());
	}

	/**
	 * Update last login
	 */
	public void updateLastLogin() {
		// Update last login
		updateData("lastLogin", getLastLogin());
	}

	/**
	 * Update login password
	 */
	public void updateLoginPassword() {
		// Update login password
		updateData("loginPassword", getLoginPassword());
	}

	/**
	 * Update auth key
	 */
	public void updateAuthKey() {
		// Update auth key
		updateData("authKey", getAuthKey());
	}

	/**
	 * Update the last server
	 * 
	 * @param The
	 *            ProxiedPlayer object
	 */
	public void updateLastServer(ProxiedPlayer proxiedPlayer) {
		// Update data
		updateData("lastServer",
				proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null
						? proxiedPlayer.getServer().getInfo().getName()
						: "");
	}

	/**
	 * Update settings
	 */
	public void updateSettings() {
		// Update settings
		updateData("settings", settings.getDBObject());
	}

	/**
	 * Update version
	 */
	public void updateVersion() {
		// Update version
		updateData("version", getVersion());
	}

}