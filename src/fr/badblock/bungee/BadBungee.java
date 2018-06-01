package fr.badblock.bungee;

import com.google.gson.Gson;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
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
public class BadBungee extends Plugin
{

	@Getter@Setter private static BadBungee instance;

	private RabbitService		rabbitService;
	private MongoService		mongoService;
	private BadBungeeConfig		config;
	private Gson				gson;
	private Gson				prettyGson;
	private boolean				unloaded;

	@Override
	public void onEnable()
	{
		new BungeeLoader(this);
	}

	@Override
	public void onDisable()
	{
		new BungeeUnloader(this);
	}

	@SuppressWarnings("deprecation")
	public static void log(String message)
	{
		ProxyServer.getInstance().getConsole().sendMessage("§e[BadBungee] §f" + message);
	}

}
