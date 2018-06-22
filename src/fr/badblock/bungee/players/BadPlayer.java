package fr.badblock.bungee.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.api.common.utils.flags.FlagObject;
import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.bungee.tasks.BungeeTask;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.badblock.bungee.modules.commands.modo.objects.ModoSession;
import fr.badblock.bungee.utils.ChatColorUtils;
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
public final class BadPlayer extends BadOfflinePlayer {

	/**
	 * BadPlayer map
	 * 
	 * @param Set
	 *            the new BadPlayer map
	 * @return Returns the new BadPlayer map
	 */
	private static Map<String, BadPlayer> maps = new HashMap<>();

	/**
	 * Get a BadPlayer object
	 * 
	 * @param The
	 *            ProxiedPlayer object
	 * @return Returns a BadPlayer object
	 */
	public static BadPlayer get(ProxiedPlayer bPlayer) {
		// Get from another method
		return get(bPlayer.getName());
	}

	/**
	 * Get a BadPlayer object
	 * 
	 * @param Username
	 * @return Returns a BadPlayer object
	 */
	public static BadPlayer get(String name) {
		// Get from the map
		return maps.getOrDefault(name, null);
	}

	/**
	 * Get all BadPlayer objects
	 * 
	 * @return Returns all BadPlayer objects
	 */
	public static Collection<BadPlayer> getPlayers() {
		// Get the map values
		return maps.values();
	}

	/**
	 * If the username is in the map
	 * 
	 * @param Username
	 * @return Returns if the username is in the map or not
	 */
	public static boolean has(String name) {
		// Contains in the map?
		return maps.containsKey(name);
	}

	/**
	 * Put the BadPlayer object
	 * 
	 * @param badPlayer
	 */
	public static void put(BadPlayer badPlayer) {
		// Put in map
		maps.put(badPlayer.getName(), badPlayer);

		// Keep alive update
		BungeeTask.keepAlive();
	}

	/**
	 * Temp flags
	 */
	private FlagObject 				flags					= new FlagObject();

	/**
	 * Last message player (tmp var)
	 */
	private String					tmpLastMessagePlayer;

	private String					lastMessage;
	private long					lastMessageTime;
	private Map<String, Long>		spamMessages			= new HashMap<>();

	private transient ModoSession	modoSession;

	private boolean					loginStepOk;

	private long					loginTimestamp;

	/**
	 * Constructor
	 * 
	 * @param The
	 *            pending connection
	 */
	public BadPlayer(PendingConnection pendingConnection) {
		// Super!
		super(pendingConnection.getName());
		// Log => loaded data
		BadBungee.log(ChatColor.GREEN + "Loaded data for " + pendingConnection.getName());
		// Set the last IP
		setLastIp(pendingConnection.getAddress().getAddress().getHostAddress());
		// Set the version
		setVersion(pendingConnection.getVersion());
		// Set the last server
		setLastServer(getCurrentServer());
		// Set login timestamp
		setLoginTimestamp(System.currentTimeMillis());
		// Set the last login
		setLastLogin(getLoginTimestamp());
		// Update multiple values
		updateData(
				new String[] { "lastIp", "version", "lastServer", "lastLogin"},
				new Object[] { getLastIp(), getVersion(), getLastServer(), getLastLogin() }
				);
		// Put in the map
		put();
	}

	/**
	 * Get the ban message
	 * 
	 * @return Returns the ban message
	 */
	public String getBanMessage() {
		// If the punish is null
		if (getPunished() == null) {
			// Returns null
			return null;
		}

		// If the punish ban is null
		if (getPunished().getBan() == null) {
			// Returns null
			return null;
		}

		// We create an empty ban message
		StringBuilder stringBuilder = new StringBuilder();
		// Create array
		int[] arr = getPunished().getBan().isReasonKey() ? new int[] { 1 } : null;
		// For each line of the ban message
		for (String string : getTranslatedMessages("punishments.ban", arr, Time.MILLIS_SECOND
				.toFrench(getPunished().getBan().getExpire() - System.currentTimeMillis(), Time.SECOND, Time.YEAR),
				getPunished().getBan().getReason())) {
			// We add it to the final ban message
			stringBuilder.append(string + "\n");
		}

		// Returns the ban message
		return stringBuilder.toString();
	}

	/**
	 * Get the current server name
	 * 
	 * @return Returns the current server name
	 */
	public String getCurrentServer() {
		// Get the proxied player
		ProxiedPlayer proxiedPlayer = toProxiedPlayer();
		// Returns the name
		return proxiedPlayer != null && proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null
				? proxiedPlayer.getServer().getInfo().getName()
						: getLastServer();
	}

	/**
	 * Get the kick message
	 * 
	 * @return Returns the kick message
	 */
	public String getKickMessage(String reason) {
		// We create an empty kick message
		StringBuilder stringBuilder = new StringBuilder();
		// For each line of the kick message
		for (String string : getTranslatedMessages("punishments.kick", null, reason)) {
			// We add it to the final kick message
			stringBuilder.append(string + "\n");
		}

		// Returns the kick message
		return stringBuilder.toString();
	}

	/**
	 * Send data to bukkit
	 */
	public void sendDataToBukkit(String serverName)
	{
		RabbitPacketMessage message = new RabbitPacketMessage(-1L, getSavedObject().toString());
		String queueName = BadBungeeQueues.BUNGEE_DATA_PLAYERS + serverName;
		RabbitPacket rabbitPacket = new RabbitPacket(message, queueName, true, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER);
		BadBungee.getInstance().getRabbitService().sendPacket(rabbitPacket);
	}

	/**
	 * Send data to bukkit
	 */
	public void sendDataToBukkit()
	{
		sendDataToBukkit(getLastServer());
	}

	/**
	 * Get the mute message
	 * 
	 * @return Returns the mute message
	 */
	public String[] getMuteMessage() {
		// If the punish is null
		if (getPunished() == null) {
			// Returns null
			return null;
		}

		// If the punish mute is null
		if (getPunished().getMute() == null) {
			// Returns null
			return null;
		}

		// We create an empty mute message
		StringBuilder stringBuilder = new StringBuilder();
		// Create array
		int[] arr = getPunished().getMute().isReasonKey() ? new int[] { 1 } : null;
		// For each line of the mute message
		for (String string : getTranslatedMessages("punishments.mute", arr, getPunished().buildMuteTime(getLocale()),
				ChatColor.stripColor(getPunished().getMute().getReason()))) {
			// We add it to the final ban message
			stringBuilder.append(string + System.lineSeparator());
		}

		// Returns the ban message
		return stringBuilder.toString().split(System.lineSeparator());
	}

	/**
	 * If the player is logged (if he has passed the login server)
	 * 
	 * @return
	 */
	public boolean isLogged() {
		// Returns if the last server doesn't start with login
		return this.getLastServer() != null && this.isLoginStepOk();
	}

	/**
	 * If the player is on this node
	 * 
	 * @return
	 */
	public boolean isOnThisNode() {
		return toProxiedPlayer() != null;
	}

	@SuppressWarnings("deprecation")
	/**
	 * Kick the player
	 * 
	 * @param kick
	 *            message
	 */
	public void kick(String message) {
		// If the player is on this node
		if (isOnThisNode()) {
			// Disconnect the player
			toProxiedPlayer().disconnect(message);
			// So we stop there
			return;
		}

		// Send the packet
		BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), message, PlayerPacketType.KICK));
	}

	/**
	 * Warn the player
	 * 
	 * @param kick
	 *            message
	 */
	public void warn(String warnReason) {
		// If the punish is null
		if (getPunished() == null) {
			// Returns null
			return;
		}

		// Send outgoing message
		this.sendTranslatedOutgoingMessage("punishments.warn", null,
				warnReason);
	}

	/**
	 * Put the player in the map
	 */
	private void put() {
		// Put in the map
		maps.put(getName(), this);
		// Global logging
		BungeeManager.getInstance().log(ChatColor.GREEN + getName() + " is now connected.");
	}

	/**
	 * Reload data
	 */
	public void reload() {
		// Load data without create
		loadData(false);
	}

	/**
	 * Remove the player from the map
	 */
	public void remove() {
		// Remove from the map
		maps.remove(getName());
		// Global logging
		BungeeManager.getInstance().log(ChatColor.RED + getName() + " is now disconnected.");
	}

	/**
	 * Send a local Json message
	 * 
	 * @param jsons
	 */
	private void sendLocalJsonMessage(String... jsons) {
		// If the player isn't logged on this local node
		if (!isOnThisNode()) {
			// So we stop there
			return;
		}

		// Send json message
		McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&', jsons)));
	}

	@SuppressWarnings("deprecation")
	/**
	 * Send a local message
	 * 
	 * @param Messages
	 *            to send
	 */
	private void sendLocalMessage(String... messages) {
		// If the player isn't logged on this local node
		if (!isOnThisNode()) {
			// So we stop there
			return;
		}

		// Send messages
		toProxiedPlayer().sendMessages(ChatColorUtils.translateColors('&', messages));
	}

	/**
	 * Send an online/temp sync update
	 */
	public void sendOnlineTempSyncUpdate() {
		// Send the packet
		BungeeManager.getInstance().sendPacket(
				new PlayerPacket(getName(), GsonUtils.getGson().toJson(this), PlayerPacketType.BADPLAYER_UPDATE));
	}

	/**
	 * Send an outgoing Json message
	 * 
	 * @param jsons
	 */
	public void sendOutgoingJsonMessage(String... jsons) {
		// If the player is logged on this local node
		if (isOnThisNode()) {
			// Send a local json message
			sendLocalJsonMessage(jsons);
			// So we stop there
			return;
		}

		// Send a json message
		BungeeManager.getInstance().sendPacket(
				new PlayerPacket(getName(), StringUtils.toOneString(jsons), PlayerPacketType.SEND_JSON_MESSAGE));
	}

	/**
	 * Send an outgoing message
	 * 
	 * @param Messages
	 *            to send
	 */
	public void sendOutgoingMessage(String... messages) {
		// If the player is logged on this local node
		if (!isOnThisNode()) {
			// Send a local message
			sendLocalMessage(messages);
			// So we stop there
			return;
		}

		// Send a packet
		BungeeManager.getInstance().sendPacket(
				new PlayerPacket(getName(), StringUtils.toOneString(messages), PlayerPacketType.SEND_MESSAGE));
	}

	/**
	 * Send a translated local Json message
	 * 
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 */
	private void sendTranslatedLocalJsonMessage(String key, int[] indexesToTranslate, Object... args) {
		// If the player isn't logged on this local node
		if (!isOnThisNode()) {
			// So we stop there
			return;
		}

		// Send the translated local Json message
		McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(
				ChatColorUtils.translateColors('&', I19n.getMessages(getLocale(), key, indexesToTranslate, args))));
	}

	@SuppressWarnings("deprecation")
	/**
	 * Send a translated local message
	 * 
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 */
	private void sendTranslatedLocalMessage(String key, int[] indexesToTranslate, Object... args) {
		// If the player isn't logged on this local node
		if (!isOnThisNode()) {
			// So we stop there
			return;
		}

		// Send the translated local message
		toProxiedPlayer().sendMessages(
				ChatColorUtils.translateColors('&', I19n.getMessages(getLocale(), key, indexesToTranslate, args)));
	}

	/**
	 * Send a translated outgoing Json message
	 * 
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 */
	public void sendTranslatedOutgoingJsonMessage(String key, int[] indexesToTranslate, Object... args) {
		// If the player is logged on this local node
		if (isOnThisNode()) {
			// Send a local translated Json message
			sendTranslatedLocalJsonMessage(key, indexesToTranslate, args);
			// So we stop there
			return;
		}

		// Send a packet
		BungeeManager.getInstance()
		.sendPacket(new PlayerPacket(getName(),
				StringUtils.toOneString(I19n.getMessages(getLocale(), key, indexesToTranslate, args)),
				PlayerPacketType.SEND_JSON_MESSAGE));
	}

	/**
	 * Send a translated outgoing MCJson message
	 * 
	 * @param MCJson
	 *            object
	 */
	public void sendTranslatedOutgoingMCJson(McJson mcjson) {
		// If the player is logged on this locale node
		if (isOnThisNode()) {
			// Send a local MCJson message
			sendLocalJsonMessage(mcjson.toString());
			// So we stop there
			return;
		}

		// Send a translated MCJson message
		BungeeManager.getInstance()
		.sendPacket(new PlayerPacket(getName(), mcjson.toString(), PlayerPacketType.SEND_JSON_MESSAGE));
	}

	/**
	 * Send a translated outgoing message
	 * 
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 */
	public void sendTranslatedOutgoingMessage(String key, int[] indexesToTranslate, Object... args) {
		// If the player is logged on this local node
		if (isOnThisNode()) {
			// Send a local translated message
			sendTranslatedLocalMessage(key, indexesToTranslate, args);
			// So we stop there
			return;
		}

		// Send a packet
		BungeeManager.getInstance()
		.sendPacket(new PlayerPacket(getName(),
				StringUtils.toOneString(I19n.getMessages(getLocale(), key, indexesToTranslate, args)),
				PlayerPacketType.SEND_MESSAGE));
	}

	/**
	 * To ProxiedPlayer object
	 * 
	 * @return Returns a ProxiedPlayer object
	 */
	public ProxiedPlayer toProxiedPlayer() {
		// Get the proxied player object
		return ProxyServer.getInstance().getPlayer(getName());
	}

}