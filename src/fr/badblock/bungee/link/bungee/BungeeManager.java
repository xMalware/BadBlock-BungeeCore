package fr.badblock.bungee.link.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.general.GlobalFlags;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacket;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacketType;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.rabbit.BadBungeeQueues;
import fr.badblock.bungee.utils.Filter;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.time.TimeUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
/**
 * BungeeCord data manager in the network
 * 
 * @author xMalware
 *
 */
public class BungeeManager
{

	/**
	 * Unique BungeeManager instance
	 * @param New BungeeManager
	 * @return Current BungeeManager
	 */
	@Getter @Setter private static BungeeManager instance = new BungeeManager();

	/**
	 * BungeeCord nodes
	 */
	private Map<String, BungeeObject> bungees = new HashMap<>();

	/**
	 * Current server ping
	 */
	private ServerPing			serverPing;

	/**
	 * Number of players online, temporary variable. Very useful to avoid recalculating the number each time
	 */
	private int					tempOnlinePlayers;
	/**
	 * Current slots
	 */
	private int					slots;
	/**
	 * Cached player token
	 */
	private CachedPlayerToken	token;

	/**
	 * Add a BungeeCord node
	 * @param received bungee object
	 */
	public void add(BungeeObject bungeeObject)
	{
		// Add in the map
		bungees.put(bungeeObject.getName(), bungeeObject);
	}

	/**
	 * Send a message in all BungeeCord logs.
	 * @param message to send
	 */
	public void log(String message)
	{
		// Send the packet with the BungeePacket layer
		sendPacket(new BungeePacket(BungeePacketType.LOG, message));
	}

	/**
	 * Send a message to all players on the whole network
	 * @param array of all messages to send
	 */
	public void broadcast(String... messages)
	{
		// Create a new string builder
		StringBuilder stringBuilder = new StringBuilder();
		// Create an iterator with messages
		Iterator<String> iterator = Arrays.asList(messages).iterator();
		// On each message
		while (iterator.hasNext())
		{
			// Get the message
			String message = iterator.next();
			// Add in the string builder
			stringBuilder.append(message + (iterator.hasNext() ? System.lineSeparator() : ""));
		}
		// Send the broadcast packet
		sendPacket(new BungeePacket(BungeePacketType.BROADCAST, stringBuilder.toString()));
	}

	/**
	 * Send a message to all players with specific permission
	 * @param the permission
	 * @param all messages
	 */
	public void targetedBrodcast(String permission, String... messages)
	{
		// Get logged players, filter with the permision and send the message
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendOutgoingMessage(messages));
	}

	/**
	 * Send a message to all players with specific permission and a translated message
	 * @param the permission
	 * @param the message key
	 * @param array of indexes to translate
	 * @param message arguments
	 */
	public void targetedTranslatedBroadcast(String permission, String key, int[] indexesToTranslate, Object... args)
	{
		// Get logged players, filter with the permission and send the translated message
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendTranslatedOutgoingMessage(key, indexesToTranslate, args));
	}

	/**
	 * Send a JSON message to all players with specific permission
	 * @param the permission
	 * @param all messages
	 */
	public void targetedJsonBrodcast(String permission, String... messages)
	{
		// Get logged players, filter with the permission and send the JSON message
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendOutgoingJsonMessage(messages));
	}

	/**
	 * Send a translated JSON message to all players with a specific permission
	 * @param the permission
	 * @param the message key
	 * @param array of indexes to translate
	 * @param message arguments
	 */
	public void targetedTranslatedJsonBroadcast(String permission, String key, int[] indexesToTranslate, Object... args)
	{
		// Get logged players, filter with the permission and send the translated JSON message
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendTranslatedOutgoingJsonMessage(key, indexesToTranslate, args));
	}

	/**
	 * Send a translated MCJson message to all players with a specific permission
	 * @param the permission
	 * @param mcJson object
	 */
	public void targetedTranslatedMCJsonBroadcast(String permission, McJson mcJson)
	{
		// Get logged players, filter with the permission and send the translated MCJson object (message)
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendTranslatedOutgoingMCJson(mcJson));
	}

	/**
	 * Send messages to all players with a custom filter 
	 * @param the filter
	 * @param all messages
	 */
	public void targetedBrodcast(Filter<BadPlayer> filter, String... messages)
	{
		// Filter the logged players and send them messages
		filter.filterList(getLoggedPlayers()).forEach(player -> player.sendOutgoingMessage(messages));
	}

	/**
	 * Send translated message to all players with custom filter
	 * @param filter
	 * @param message key
	 * @param indexes to translate
	 * @param message args
	 */
	public void targetedTranslatedBroadcast(Filter<BadPlayer> filter, String key, int[] indexesToTranslate, Object... args)
	{
		// Filter the logged players and send them translated messages
		filter.filterList(getLoggedPlayers()).forEach(player -> player.sendTranslatedOutgoingMessage(key, indexesToTranslate, args));
	}

	/**
	 * Send JSON messages to all players with custom filter
	 * @param filter
	 * @param all messages
	 */
	public void targetedJsonBrodcast(Filter<BadPlayer> filter, String... messages)
	{
		// Filter the logged players and send them a JSON message
		filter.filterList(getLoggedPlayers()).forEach(player -> player.sendOutgoingJsonMessage(messages));
	}

	/**
	 * Send translated JSON message to all players with custom filter
	 * @param filter
	 * @param the message key
	 * @param the indexes to translate
	 * @param the message args
	 */
	public void targetedTranslatedJsonBroadcast(Filter<BadPlayer> filter, String key, int[] indexesToTranslate, Object... args)
	{
		// Filter the logged players and send them a tranlated JSON message
		filter.filterList(getLoggedPlayers()).forEach(player -> player.sendTranslatedOutgoingJsonMessage(key, indexesToTranslate, args));
	}

	/**
	 * Send a translated MCJson message to all players with custom filter
	 * @param filter
	 * @param mcJson object
	 */
	public void targetedTranslatedMCJsonBroadcast(Filter<BadPlayer> filter, McJson mcJson)
	{
		// Filter the logged players and send them a translated MCJson message
		filter.filterList(getLoggedPlayers()).forEach(player -> player.sendTranslatedOutgoingMCJson(mcJson));
	}

	/**
	 * Get a BadPlayer object from the network with an username
	 * @param playerName
	 * @return a BadPlayer object
	 */
	public BadPlayer getBadPlayer(String name)
	{
		// Get the logged players with the name
		List<BadPlayer> list = getLoggedPlayers(p -> p.getName().equalsIgnoreCase(name));
		// Returns BadPlayer object if the list isn't empty
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Get a BadPlayer object from the network with a universally unique identifier
	 * @param the universally unique identifier
	 * @return a BadPlayer object
	 */
	public BadPlayer getBadPlayer(UUID uuid)
	{
		// Get the logged players with the universally unique identifier
		List<BadPlayer> list = getLoggedPlayers(p -> p.getUniqueId().toString().equalsIgnoreCase(uuid.toString()));
		// Returns BadPlayer object if the list isn't empty
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Get a BadPlayer object from the network with a ProxiedPlayer object
	 * @param proxiedPlayer 
	 * @return a BadPlayer object
	 */
	public BadPlayer getBadPlayer(ProxiedPlayer proxiedPlayer)
	{
		// Get from the local map
		return BadPlayer.get(proxiedPlayer);
	}

	/**
	 * Get a BadOfflinePlayer object from the network
	 * @param username
	 * @return a BadOfflinePlayer object
	 */
	public BadOfflinePlayer getBadOfflinePlayer(String name)
	{
		// If the user is online
		if (getBadPlayer(name) != null)
		{
			// Returns the BadPlayer
			return getBadPlayer(name);
		}
		// Returns the BadOfflinePlayer object
		return BadOfflinePlayer.get(name);
	}

	/**
	 * Get a BadOfflinePlayer object from the network
	 * @param UUID
	 * @return a BadOfflinePlayer object
	 */
	public BadOfflinePlayer getBadOfflinePlayer(UUID uuid)
	{
		// If the user is online
		if (getBadPlayer(uuid) != null)
		{
			// Returns the BadPlayer
			return getBadPlayer(uuid);
		}
		// Returns the BadOfflinePlayer object
		return BadOfflinePlayer.get(uuid);
	}

	@SuppressWarnings("deprecation")
	/**
	 * Generate the server ping by using network data
	 * @return a ServerPing object
	 */
	public ServerPing generatePing()
	{
		// If a server ping has already been created recently
		if (serverPing != null && GlobalFlags.has(serverPing))
		{
			// Returns the current server ping
			return serverPing;
		}
		// Get main class
		BadBungee bungee = BadBungee.getInstance();
		// Get Mongo service by using the main class
		MongoService mongoService = bungee.getMongoService();
		// Get database object
		DB db = mongoService.getDb();
		// Get the collection
		DBCollection collection = db.getCollection("serverInfo");
		// New query
		BasicDBObject query = new BasicDBObject();
		// Find all data with an empty query
		DBCursor cursor = collection.find(query);
		// For each data
		while (cursor.hasNext())
		{
			// Get it
			DBObject dbObject = cursor.next();
			// Get deserialize object
			String json = dbObject.toString();
			// Serialize as a ServerPing object
			ServerPing serverPing = bungee.getGson().fromJson(json, ServerPing.class);
			// Replace {} to \n => escape lines
			serverPing.setDescription(serverPing.getDescription().replace("{}", "\n"));
			// Set slots
			setSlots(serverPing.getPlayers().getMax());
			// Add a flag for the server ping object to avoid spam
			GlobalFlags.set(serverPing, 1000);
			// Set online players
			serverPing.getPlayers().setOnline(getOnlinePlayers());
			// Returns the server ping object
			return serverPing;
		}
		// Returns null
		return null;
	}

	/**
	 * Send a player packet
	 * @param player packet object
	 */
	public void sendPacket(PlayerPacket playerPacket)
	{
		// Get main class
		BadBungee badBungee = BadBungee.getInstance();
		// Deserialize the player packet
		String json = badBungee.getGson().toJson(playerPacket);
		// Send the packet over RabbitMQ
		badBungee.getRabbitService().sendPacket(new RabbitPacket(new RabbitPacketMessage(5000, json), 
				BadBungeeQueues.PLAYER_PROCESSING, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER));
	}

	/**
	 * Send a bungee packet
	 * @param bungee packet object
	 */
	public void sendPacket(BungeePacket bungeePacket)
	{
		// Get main class
		BadBungee badBungee = BadBungee.getInstance();
		// Deserialize the bungee packet
		String json = badBungee.getGson().toJson(bungeePacket);
		// Send the packet over RabbitMQ
		badBungee.getRabbitService().sendPacket(new RabbitPacket(new RabbitPacketMessage(5000, json), 
				BadBungeeQueues.BUNGEE_PROCESSING, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER));
	}

	/**
	 * Get the logged players with a predicate
	 * @param predicate (filtering)
	 * @return list of online BadPlayer with filters
	 */
	public List<BadPlayer> getLoggedPlayers(Predicate<BadPlayer> predicate)
	{
		// New array list
		List<BadPlayer> badPlayers = new ArrayList<>();
		// Get all bungee
		bungees.values().parallelStream().forEach(
				bungee -> 
				// Add all online BadPlayer with a predicate (custom filter)
				badPlayers.addAll(bungee.getUsernames().values().parallelStream().filter(player -> player.isLogged() && predicate.test(player)).collect(Collectors.toList())));
		// Return the array list
		return badPlayers;
	}

	/**
	 * Get logged players
	 * @return
	 */
	public List<BadPlayer> getLoggedPlayers()
	{
		// Use the getLoggedPlayers() with a predicate which is always true
		return getLoggedPlayers(a -> true);
	}

	/**
	 * If an username exists in the online network
	 * @param username of the player
	 * @return if the player is online or not
	 */
	public boolean hasUsername(String name)
	{
		// Get the lower player username
		final String toLowerName = name.toLowerCase();
		// Get the count of players with this username on the network
		long count = bungees.values().parallelStream().filter(bungee -> 
		bungee.getUsernames().keySet().parallelStream().filter(n -> n.toLowerCase().equals(toLowerName)).count() > 0).count();
		// Returns if the count is more than 0
		return count > 0;
	}

	/**
	 * Get the online player count in realtime
	 * @return
	 */
	public int getRealTimeOnlinePlayers()
	{
		// If the token is null or if the token has expired
		if (token == null || token.isExpired())
		{
			// So we regenerate it
			token = CachedPlayerToken.generateToken();
		}
		// Returns the last token
		return token.getLastToken();
	}

	/**
	 * Get the online player count with cache time (to avoid compute-spamming)
	 * @return the online player count
	 */
	public int getOnlinePlayers()
	{
		// If the flag is valid
		if (GlobalFlags.has("onlinePlayers"))
		{
			// So we return the cached online player count
			return tempOnlinePlayers;
		}
		// Set to 1 second before rechecking the count
		GlobalFlags.set("onlinePlayers", 1_000);
		// Returns the temp online players
		return tempOnlinePlayers = getRealTimeOnlinePlayers();
	}

	/**
	 * If a Bungee object stills valid
	 * @param bungeeObject
	 * @return if the Bungee object stills valid or not
	 */
	public boolean isValid(BungeeObject bungeeObject)
	{
		// Check with the timestamp
		return TimeUtils.isValid(bungeeObject.getTimestamp());
	}

}
