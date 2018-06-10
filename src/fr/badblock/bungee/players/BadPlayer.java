package fr.badblock.bungee.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.badblock.bungee.rabbit.BadBungeeQueues;
import fr.badblock.bungee.rabbit.datareceivers.PlayerDataUpdateSender;
import fr.badblock.bungee.utils.ChatColorUtils;
import fr.badblock.bungee.utils.ObjectUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
/**
 * 
 * BadPlayer
 * 
 * @author xMalware
 *
 */
public final class BadPlayer extends BadOfflinePlayer
{

	/**
	 * BadPlayer map
	 * @param Set the new BadPlayer map
	 * @return Returns the new BadPlayer map
	 */
	private static Map<String, BadPlayer> maps 			= new HashMap<>();

	/**
	 * Constructor
	 * @param The pending connection
	 */
	public BadPlayer(PendingConnection pendingConnection)
	{
		// Super!
		super(pendingConnection.getName());
		// Log => loaded data
		BadBungee.log(ChatColor.GREEN + "Loaded data for " + pendingConnection.getName());
		// Set the last IP
		setLastIp(pendingConnection.getAddress().getHostName());
		// Update the last IP
		updateLastIp();
		// Set the version
		setVersion(pendingConnection.getVersion());
		// Update the vesion
		updateVersion();
		// Put in the map
		put();
	}

	/**
	 * Send data to bukkit
	 */
	public void sendDataToBukkit()
	{
		// Get the current server name
		String serverName = getCurrentServer();
		// Get the player data update sender
		PlayerDataUpdateSender playerDataUpdateSender = new PlayerDataUpdateSender(getName().toLowerCase(), ObjectUtils.getJsonObject(getDbObject().toString()));
		// Serialize data
		String rawPlayerDataUpdateSender = GsonUtils.getGson().toJson(playerDataUpdateSender);
		// Create a Rabbit packet message
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(300_000L, rawPlayerDataUpdateSender);
		// Get the queue
		String builtQueue = BadBungeeQueues.BUNGEE_DATA_SENDERS + serverName;
		// Create a Rabbit packet
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, builtQueue, true, RabbitPacketEncoder.UTF8, RabbitPacketType.MESSAGE_BROKER);
		// Send the RabbitMQ packet
		BadBungee.getInstance().getRabbitService().sendPacket(rabbitPacket);
	}

	/**
	 * Get the last server
	 * @return a String
	 */
	public String getLastServer()
	{
		// If the database object is null
		if (getDbObject() == null)
		{
			// Unknown last server
			return null;
		}
		// Returns the last server
		return getDbObject().get("lastServer").toString();
	}

	/**
	 * If the player is logged (if he has passed the login server)
	 * @return
	 */
	public boolean isLogged()
	{
		// Returns if the last server doesn't start with login
		return getLastServer() != null && !getLastServer().startsWith("login");
	}

	/**
	 * Reload data
	 */
	public void reload()
	{
		// Load data without create
		loadData(false);
	}

	/**
	 * Send an outgoing message
	 * @param Messages to send
	 */
	public void sendOutgoingMessage(String... messages)
	{
		// If the player is logged on this local node
		if (toProxiedPlayer() != null)
		{
			// Send a local message
			sendLocalMessage(messages);
			// So we stop there
			return;
		}

		// Send a packet
		BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_MESSAGE, StringUtils.toOneString(messages)));
	}

	@SuppressWarnings("deprecation")
	/**
	 * Send a local message
	 * @param Messages to send
	 */
	private void sendLocalMessage(String... messages)
	{
		// If the player isn't logged on this local node
		if (toProxiedPlayer() == null)
		{
			// So we stop there
			return;
		}

		// Send messages
		toProxiedPlayer().sendMessages(ChatColorUtils.translateColors('&', messages));
	}

	/**
	 * Send a translated outgoing message
	 * @param Message key
	 * @param Indexes to translate
	 * @param Arguments
	 */
	public void sendTranslatedOutgoingMessage(String key, int[] indexesToTranslate, Object... args)
	{
		// If the player is logged on this local node
		if (toProxiedPlayer() != null)
		{
			// Send a local translated message
			sendTranslatedLocalMessage(key, indexesToTranslate, args);
			// So we stop there
			return;
		}

		// Send a packet
		BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_MESSAGE, StringUtils.toOneString(I19n.getMessages(getLocale(), key, indexesToTranslate, args))));
	}

	/**
	 * Send a translated outgoing Json message
	 * @param Message key
	 * @param Indexes to translate
	 * @param Arguments
	 */
	public void sendTranslatedOutgoingJsonMessage(String key, int[] indexesToTranslate, Object... args)
	{
		// If the player is logged on this local node
		if (toProxiedPlayer() != null)
		{
			// Send a local translated Json message
			sendTranslatedLocalJsonMessage(key, indexesToTranslate, args);
			// So we stop there
			return;
		}

		// Send a packet
		BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, StringUtils.toOneString(I19n.getMessages(getLocale(), key, indexesToTranslate, args))));
	}

	/**
	 * Send a local Json message
	 * @param jsons
	 */
	private void sendLocalJsonMessage(String... jsons)
	{
		// If the player isn't logged on this local node
		if (toProxiedPlayer() == null)
		{
			// So we stop there
			return;
		}

		// Send json message
		McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&', jsons)));
	}

	@SuppressWarnings("deprecation")
	/**
	 * Send a translated local message
	 * @param Message key
	 * @param Indexes to translate
	 * @param Arguments
	 */
	private void sendTranslatedLocalMessage(String key,  int[] indexesToTranslate, Object... args)
	{
		// If the player isn't logged on this local node
		if (toProxiedPlayer() == null)
		{
			// So we stop there
			return;
		}

		// Send the translated local message
		toProxiedPlayer().sendMessages(ChatColorUtils.translateColors('&', I19n.getMessages(getLocale(), key, indexesToTranslate, args)));
	}

	/**
	 * Send a translated local Json message
	 * @param Message key
	 * @param Indexes to translate
	 * @param Arguments
	 */
	private void sendTranslatedLocalJsonMessage(String key, int[] indexesToTranslate, Object... args)
	{
		// If the player isn't logged on this local node
		if (toProxiedPlayer() == null)
		{
			// So we stop there
			return;
		}

		// Send the translated local Json message
		McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&', I19n.getMessages(getLocale(), key, indexesToTranslate, args))));
	}

	/**
	 * Send an outgoing Json message
	 * @param jsons
	 */
	public void sendOutgoingJsonMessage(String... jsons)
	{
		// If the player is logged on this locale node
		if (toProxiedPlayer() != null)
		{
			// Send a local json message
			sendLocalJsonMessage(jsons);
			// So we stop there
			return;
		}

		// Send a json message
		BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, StringUtils.toOneString(jsons)));
	}

	/**
	 * Send a translated outgoing MCJson message
	 * @param MCJson object
	 */
	public void sendTranslatedOutgoingMCJson(McJson mcjson)
	{
		// If the player is logged on this locale node
		if (toProxiedPlayer() != null)
		{
			// Send a local MCJson message
			sendLocalJsonMessage(mcjson.toString());
			// So we stop there
			return;
		}

		// Send a translated MCJson message
		BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, mcjson.toString()));
	}

	/**
	 * Get the current server name
	 * @return Returns the current server name
	 */
	public String getCurrentServer()
	{
		// Get the proxied player
		ProxiedPlayer proxiedPlayer = toProxiedPlayer();
		// Returns the name
		return proxiedPlayer != null && proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null ? proxiedPlayer.getServer().getInfo().getName() : null;
	}

	/**
	 * Put the player in the map
	 */
	private void put()
	{
		// Put in the map
		maps.put(getName(), this);
		// Global logging
		BungeeManager.getInstance().log(ChatColor.GREEN + getName() + " is now connected.");
	}

	/**
	 * Remove the player from the map
	 */
	public void remove()
	{
		// Remove from the map
		maps.remove(getName());
		// Global logging
		BungeeManager.getInstance().log(ChatColor.RED + getName() + " is now disconnected.");
	}

	/**
	 * To ProxiedPlayer object
	 * @return Returns a ProxiedPlayer object
	 */
	private ProxiedPlayer toProxiedPlayer()
	{
		// Get the proxied player object
		return ProxyServer.getInstance().getPlayer(getName());
	}

	/**
	 * Get a BadPlayer object
	 * @param The ProxiedPlayer object
	 * @return Returns a BadPlayer object
	 */
	public static BadPlayer get(ProxiedPlayer bPlayer)
	{
		// Get from another method
		return get(bPlayer.getName());
	}

	/**
	 * Get a BadPlayer object
	 * @param Username
	 * @return Returns a BadPlayer object
	 */
	public static BadPlayer get(String name)
	{
		// Get from the map
		return maps.getOrDefault(name, null);
	}

	/**
	 * If the username is in the map
	 * @param Username
	 * @return Returns if the username is in the map or not
	 */
	public static boolean has(String name)
	{
		// Contains in the map?
		return maps.containsKey(name);
	}

	/**
	 * Get all BadPlayer objects
	 * @return Returns all BadPlayer objects
	 */
	public static Collection<BadPlayer> getPlayers()
	{
		// Get the map values
		return maps.values();
	}

}