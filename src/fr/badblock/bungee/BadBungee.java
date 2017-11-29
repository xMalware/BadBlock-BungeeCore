package fr.badblock.bungee;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.bungee.config.BadBungeeConfig;
import fr.badblock.bungee.config.ConfigLoader;
import fr.badblock.bungee.link.bungee.BungeeTask;
import fr.badblock.bungee.utils.PackageUtils;
import fr.toenga.common.tech.mongodb.MongoConnector;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.rabbitmq.RabbitConnector;
import fr.toenga.common.tech.rabbitmq.RabbitService;
import fr.toenga.common.utils.i18n.I18n;
import fr.toenga.common.utils.permissions.PermissionsManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadBungee extends Plugin {

	@Getter@Setter private static BadBungee instance;

	private RabbitService	rabbitService;
	private MongoService	mongoService;
	private BadBungeeConfig	config;
	private Gson		 	gson;
	private Gson		 	prettyGson;

	@Override
	public void onEnable()
	{
		// Set instance
		instance = this;
		// Load gson
		setGson(new Gson());
		// Load pretty gson
		setPrettyGson(new GsonBuilder().setPrettyPrinting().create());
		// Load config
		setConfig(new ConfigLoader().load());
		// Load i18n
		loadI18n();
		// Load rabbit service
		rabbitService = RabbitConnector.getInstance().registerService(new RabbitService("default", getConfig().getRabbitSettings()));
		// Load mongo service
		mongoService = MongoConnector.getInstance().registerService(new MongoService("default", getConfig().getMongoSettings()));
		// Creating bungee task
		new BungeeTask();
		// Loading listeners
		try
		{
			PackageUtils.instanciateListeners(this,
					"fr.xmalware.badblock.bungee.rabbit.listeners", // Rabbit listeners
					// Commands
					"fr.xmalware.badblock.bungee._plugins.commands.admin",
					"fr.xmalware.badblock.bungee._plugins.commands.modo",
					"fr.xmalware.badblock.bungee._plugins.commands.basic",
					// Listeners
					"fr.xmalware.badblock.bungee._plugins.listeners.logins.checkLogin",
					"fr.xmalware.badblock.bungee._plugins.listeners.logins.loadPlayer",
					"fr.xmalware.badblock.bungee._plugins.listeners.motd",
					"fr.xmalware.badblock.bungee._plugins.listeners.permissions",
					"fr.xmalware.badblock.bungee._plugins.listeners.punishments"
					);
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}

		// Permissions
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		DB db = mongoService.getDb();
		DBCollection collection = db.getCollection("permissions");
		BasicDBObject query = new BasicDBObject();
		DBCursor cursor = collection.find(query);

		if (cursor.hasNext())
		{
			DBObject dbObject = cursor.next();
			String json = getGson().toJson(dbObject.get("groups"));

			PermissionsManager.createPermissionManager(getGson().fromJson(json, JsonObject.class), "bungee");
		}
	}

	private void loadI18n() {
		File i18n = new File(getDataFolder(), "i18n/");

		try {
			I18n.getInstance().load(i18n);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void onDisable()
	{

	}

	@SuppressWarnings("deprecation")
	public static void log(String message)
	{
		ProxyServer.getInstance().getConsole().sendMessage("§e[BadBungee] §f" + message);
	}

}
