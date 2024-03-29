package fr.badblock.bungee;

import com.google.gson.Gson;

import fr.badblock.api.common.minecraft.party.PartySyncManager;
import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.redis.RedisService;
import fr.badblock.bungee.config.BadBungeeConfig;
import fr.badblock.bungee.loader.BungeeLoader;
import fr.badblock.bungee.loader.BungeeUnloader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@EqualsAndHashCode(callSuper = false)
@Data
/**
 * 
 * Main class of BadBlock Bungee
 * 
 * @author xMalware
 *
 */
public class BadBungee extends Plugin {

	/**
	 * BadBungee
	 * 
	 * @param Set
	 *            the new BadBungee instance
	 * @return Returns the current BadBungee instance
	 */
	@Getter
	@Setter
	private static BadBungee instance;

	@SuppressWarnings("deprecation")
	/**
	 * Proxy logging (with a prefix)
	 * 
	 * @param Message
	 *            to log
	 */
	public static void log(String message) {
		// Local logging
		ProxyServer.getInstance().getConsole().sendMessage("§e[BadBungee] §f" + message);
	}

	/**
	 * BadBungee configuration
	 * 
	 * @param Set
	 *            the new BadBungee configuration
	 * @return Returns the current BadBungee configuration
	 */
	private BadBungeeConfig config;

	/**
	 * Gson
	 * 
	 * @param Set
	 *            the new Gson
	 * @return Returns the current Gson
	 */
	private Gson gson;

	/**
	 * Mongo service
	 * 
	 * @param Set
	 *            the new Mongo service
	 * @return Returns the current Mongo service
	 */
	private MongoService mongoService;

	/**
	 * Redis service
	 * 
	 * @param Set
	 *            the new Redis service
	 * @return Returns the current Redis service
	 */
	private RedisService redisService;

	/**
	 * Pretty Gson
	 * 
	 * @param Set
	 *            the new pretty Gson
	 * @return Returns the current pretty Gson
	 */
	private Gson prettyGson;

	/**
	 * Rabbit service
	 * 
	 * @param Set
	 *            the new Rabbit service
	 * @return Returns the current Rabbit service
	 */
	private RabbitService rabbitService;

	/**
	 * Unloaded
	 * 
	 * @param Set
	 *            the plugin to the unloaded state
	 * @return Returns if the plugin is unloaded
	 */
	private boolean unloaded;

	private BungeeLoader bungeeLoader;
	
	private PartySyncManager syncManager;

	@Override
	/**
	 * Plugin disable
	 */
	public void onDisable() {
		// Call the unloader
		new BungeeUnloader(this);
	}

	@Override
	/**
	 * Plugin enable
	 */
	public void onEnable() {
		// Call the loader
		setBungeeLoader(new BungeeLoader(this));
	}

}