package fr.badblock.bungee.loader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoConnector;
import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.rabbitmq.RabbitConnector;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.utils.i18n.I18n;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionsManager;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.config.BadBungeeConfig;
import fr.badblock.bungee.config.ConfigLoader;
import fr.badblock.bungee.link.bungee.BungeeTask;
import fr.badblock.bungee.rabbit.claimants.RabbitClaimant;
import fr.badblock.bungee.utils.PackageUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
/**
 * 
 * Bungee loader class
 * 
 * @author xMalware
 *
 */
public class BungeeLoader {

	/**
	 * BadBungee instance
	 * 
	 * @param Set
	 *            the new BadBungee instance
	 * @return Returns the current BadBungee instance
	 */
	private BadBungee badBungee;
	/**
	 * BadBungeeConfig instance
	 * 
	 * @param Set
	 *            the new BadBungee config
	 * @return Returns the current BadBungee config
	 */
	private BadBungeeConfig config;

	/**
	 * Constructor
	 * 
	 * @param Current
	 *            BadBungee object
	 */
	public BungeeLoader(BadBungee badBungee) {
		// Set the BadBungee object
		setBadBungee(badBungee);
		// Set the instance
		setInstance();
		// Set the Gson object
		setGson();
		// Load configuration
		loadConfig();
		// Load i18n
		loadI18n();
		// Load RabbitMQ
		loadRabbit();
		// Load MongoDB
		loadMongo();
		// Load Bungee linker
		loadBungeeLinker();
		// Load listeners
		loadListeners();
		// Load permissions
		loadPermissions();
	}

	/**
	 * Set the instance of BadBungee
	 */
	private void setInstance() {
		// Singleton :)
		BadBungee.setInstance(getBadBungee());
	}

	/**
	 * Set Gson objects
	 */
	private void setGson() {
		// Basic Gson
		getBadBungee().setGson(new Gson());
		// Pretty-printing Gson
		getBadBungee().setPrettyGson(new GsonBuilder().setPrettyPrinting().create());
	}

	/**
	 * Load the configuration
	 */
	private void loadConfig() {
		// Set the configuration
		setConfig(new ConfigLoader().load());
		// Set to BadBungee class
		getBadBungee().setConfig(getConfig());
	}

	/**
	 * Load i18n
	 */
	private void loadI18n() {
		// Get the folder
		File i18n = new File(getBadBungee().getDataFolder(), "i18n/");
		// Try to
		try {
			// Load i18n
			I18n.getInstance().load(i18n);
		}
		// Error case
		catch (Exception exception) {
			// Print the error in logs
			exception.printStackTrace();
		}
	}

	/**
	 * Load RabbitMQ
	 */
	private void loadRabbit() {
		// Set the service
		getBadBungee().setRabbitService(RabbitConnector.getInstance()
				.registerService(new RabbitService("default", getConfig().getRabbitSettings())));
		// Load claiments
		RabbitClaimant.load();
	}

	/**
	 * Load MongoDB
	 */
	private void loadMongo() {
		// Set the service
		getBadBungee().setMongoService(MongoConnector.getInstance()
				.registerService(new MongoService("default", getConfig().getMongoSettings())));
	}

	/**
	 * Load BungeeLinker
	 */
	private void loadBungeeLinker() {
		// Instanciate the task
		new BungeeTask();
	}

	/**
	 * Load listeners
	 */
	private void loadListeners() {
		// Try to?
		try {
			// Instanciate listeners/commmands
			PackageUtils.instanciateListeners(getBadBungee(),
					// Rabbit listeners
					"fr.badblock.bungee.rabbit.listeners",

					// Commands
					"fr.badblock.bungee.modules.commands.admin", "fr.badblock.bungee.modules.commands.badfriends",
					"fr.badblock.bungee.modules.commands.basic", "fr.badblock.bungee.modules.commands.basic.friends",
					"fr.badblock.bungee.modules.commands.basic.msg", "fr.badblock.bungee.modules.commands.basic.party",
					"fr.badblock.bungee.modules.commands.modo", "fr.badblock.bungee.modules.commands.staff",

					// Login
					"fr.badblock.bungee.modules.login.antivpn", "fr.badblock.bungee.modules.login.checkers",
					"fr.badblock.bungee.modules.login.datamanager", "fr.badblock.bungee.modules.permissions",
					"fr.badblock.bungee.modules.punishments", "fr.badblock.bungee.modules.ping");
		}
		// Error case
		catch (IOException exception) {
			// Print the error in logs
			exception.printStackTrace();
		}
	}

	/**
	 * Load permissions
	 */
	private void loadPermissions() {
		// Get the mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get the database object
		DB db = mongoService.getDb();
		// Get the permissions collection
		DBCollection collection = db.getCollection("permissions");
		// Create an empty query
		BasicDBObject query = new BasicDBObject();
		// Fetch everything with the empty query
		DBCursor cursor = collection.find(query);
		// Create a new permission map
		Map<String, Permissible> groups = new HashMap<>();
		// For each data
		while (cursor.hasNext()) {
			// Get the data
			DBObject dbObject = cursor.next();
			// Deserialize the data object
			String json = getBadBungee().getGson().toJson(dbObject);
			// Serialize the data as a Permissible object
			Permissible permissible = getBadBungee().getGson().fromJson(json, Permissible.class);
			// Put the object in the groups map
			groups.put(dbObject.get("name").toString(), permissible);
		}
		// Create a permission manager, with the 'bungee' place
		PermissionsManager.createPermissionManager(groups, "bungee");
	}

}
