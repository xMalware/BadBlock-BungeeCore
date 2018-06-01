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
import fr.badblock.bungee.utils.PackageUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class BungeeLoader
{

	//private static final Type collectionType = new TypeToken<Map<String, Permissible>>(){}.getType();

	private BadBungee		badBungee;
	private BadBungeeConfig	config;

	public BungeeLoader(BadBungee badBungee)
	{
		setBadBungee(badBungee);
		setInstance();
		setGson();
		loadConfig();
		loadI18n();
		loadRabbit();
		loadMongo();
		loadBungeeLinker();
		loadListeners();
		loadPermissions();
	}

	private void setInstance()
	{
		BadBungee.setInstance(getBadBungee());
	}

	private void setGson()
	{
		getBadBungee().setGson(new Gson());
		getBadBungee().setPrettyGson(new GsonBuilder().setPrettyPrinting().create());
	}

	private void loadConfig()
	{
		setConfig(new ConfigLoader().load());
		getBadBungee().setConfig(getConfig());
	}

	private void loadI18n()
	{
		File i18n = new File(getBadBungee().getDataFolder(), "i18n/");
		try
		{
			I18n.getInstance().load(i18n);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	private void loadRabbit()
	{
		getBadBungee().setRabbitService(RabbitConnector.getInstance().registerService(new RabbitService("default", getConfig().getRabbitSettings())));
	}

	private void loadMongo()
	{
		getBadBungee().setMongoService(MongoConnector.getInstance().registerService(new MongoService("default", getConfig().getMongoSettings())));
	}

	private void loadBungeeLinker()
	{
		new BungeeTask();
	}

	private void loadListeners()
	{
		try
		{
			PackageUtils.instanciateListeners(getBadBungee(),
					"fr.badblock.bungee.rabbit.listeners", // Rabbit listeners
					
					"fr.badblock.bungee.modules.admin.commands",
					"fr.badblock.bungee.modules.badfriends.commands",
					"fr.badblock.bungee.modules.basic.commands",
					"fr.badblock.bungee.modules.friends.commands",
					"fr.badblock.bungee.modules.login.antivpn",
					"fr.badblock.bungee.modules.login.checkers",
					"fr.badblock.bungee.modules.login.datamanager",
					"fr.badblock.bungee.modules.modo.commands",
					"fr.badblock.bungee.modules.party.commands",
					"fr.badblock.bungee.modules.permissions",
					"fr.badblock.bungee.modules.ping",
					"fr.badblock.bungee.modules.punishments",
					"fr.badblock.bungee.modules.staff.commands"
					);
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

	private void loadPermissions()
	{
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		DB db = mongoService.getDb();
		DBCollection collection = db.getCollection("permissions");
		BasicDBObject query = new BasicDBObject();
		DBCursor cursor = collection.find(query);
		Map<String, Permissible> groups = new HashMap<>();
		while (cursor.hasNext())
		{
			DBObject dbObject = cursor.next();
			String json = getBadBungee().getGson().toJson(dbObject);
			Permissible permissible = getBadBungee().getGson().fromJson(json, Permissible.class);
			groups.put(dbObject.get("name").toString(), permissible);
		}
		PermissionsManager.createPermissionManager(groups, "bungee");
	}

}
