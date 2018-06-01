package fr.badblock.bungee.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacket;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.common.utils.general.StringUtils;
import fr.toenga.common.utils.i18n.I18n;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public final class BadPlayer extends BadOfflinePlayer
{

	private static Map<String, BadPlayer> maps 			= new HashMap<>();

	public BadPlayer(PendingConnection pendingConnection)
	{
		super(pendingConnection.getName());
		BadBungee.log(ChatColor.GREEN + "Loaded data for " + pendingConnection.getName());
		setLastIp(pendingConnection.getAddress().getHostName());
		updateLastIp();
		setVersion(pendingConnection.getVersion());
		updateVersion();
		put();
	}

	public void sendDataToBukkit()
	{
		String serverName = getCurrentServer();
		PlayerDataUpdateSender playerDataUpdateSender = new PlayerDataUpdateSender(getName().toLowerCase(), ObjectUtils.getJsonObject(getDbObject().toString()));
		String rawPlayerDataUpdateSender = GsonUtils.getGson().toJson(playerDataUpdateSender);
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(300_000L, rawPlayerDataUpdateSender);
		String builtQueue = BadBungeeQueues.BUNGEE_DATA_SENDERS + serverName;
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, builtQueue, true, RabbitPacketEncoder.UTF8, RabbitPacketType.MESSAGE_BROKER);
		BadBungee.getInstance().getRabbitService().sendPacket(rabbitPacket);
	}
	
	public String getLastServer()
	{
		if (getDbObject() == null)
		{
			return null;
		}
		return getDbObject().get("lastServer").toString();
	}

	public boolean isLogged()
	{
		return getLastServer() != null && !getLastServer().startsWith("login");
	}

	public void reload() {
		loadData(false);
	}

	public void sendOutgoingMessage(String... messages)
	{
		if (toProxiedPlayer() != null)
		{
			sendLocalMessage(messages);
		}
		else
		{
			BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_MESSAGE, StringUtils.toOneString(messages)));
		}
	}

	@SuppressWarnings("deprecation")
	private void sendLocalMessage(String... messages)
	{
		if (toProxiedPlayer() == null)
		{
			return;
		}
		else
		{
			toProxiedPlayer().sendMessages(ChatColorUtils.translateColors('&', messages));
		}
	}

	public void sendTranslatedOutgoingMessage(String key, int[] indexesToTranslate, Object... args)
	{
		if (toProxiedPlayer() != null)
		{
			sendTranslatedLocalMessage(key, indexesToTranslate, args);
		}
		else
		{
			BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_MESSAGE, StringUtils.toOneString(I19n.getMessages(getLocale(), key, indexesToTranslate, args))));
		}
	}

	public void sendTranslatedOutgoingJsonMessage(String key, int[] indexesToTranslate, Object... args)
	{
		if (toProxiedPlayer() != null)
		{
			sendTranslatedLocalJsonMessage(key, indexesToTranslate, args);
		}
		else
		{
			BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, StringUtils.toOneString(I19n.getMessages(getLocale(), key, indexesToTranslate, args))));
		}
	}

	private void sendLocalJsonMessage(String... jsons)
	{
		if (toProxiedPlayer() == null)
		{
			return;
		}
		McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&', jsons)));
	}

	@SuppressWarnings("deprecation")
	private void sendTranslatedLocalMessage(String key,  int[] indexesToTranslate, Object... args)
	{
		if (toProxiedPlayer() == null)
		{
			return;
		}
		toProxiedPlayer().sendMessages(ChatColorUtils.translateColors('&', I19n.getMessages(getLocale(), key, indexesToTranslate, args)));
	}

	private void sendTranslatedLocalJsonMessage(String key, Object... args)
	{
		if (toProxiedPlayer() == null)
		{
			return;
		}
		McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&',  I18n.getInstance().get(getLocale(), key, args))));
	}

	public void sendOutgoingJsonMessage(String... jsons)
	{
		if (toProxiedPlayer() != null) sendLocalJsonMessage(jsons);
		else BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, StringUtils.toOneString(jsons)));
	}

	public void sendTranslatedOutgoingMCJson(McJson mcjson)
	{
		if (toProxiedPlayer() != null)
		{
			sendLocalJsonMessage(mcjson.toString());
		}
		else
		{
			BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, (mcjson.toString())));
		}
	}
	
	public String getCurrentServer()
	{
		ProxiedPlayer proxiedPlayer = toProxiedPlayer();
		return proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null ? proxiedPlayer.getServer().getInfo().getName() : null;
	}
	
	private void put()
	{
		maps.put(getName(), this);
		BungeeManager.getInstance().log(ChatColor.GREEN + getName() + " is now connected.");
	}

	public void remove()
	{
		maps.remove(getName());
		BungeeManager.getInstance().log(ChatColor.RED + getName() + " is now disconnected.");
		BadBungee.log(ChatColor.RED + getName() + " is now disconnected.");
	}

	private ProxiedPlayer toProxiedPlayer()
	{
		return ProxyServer.getInstance().getPlayer(getName());
	}
	
	public static BadPlayer get(ProxiedPlayer bPlayer)
	{
		return get(bPlayer.getName());
	}

	public static BadPlayer get(String name)
	{
		return maps.getOrDefault(name, null);
	}

	public static boolean has(String name)
	{
		return maps.containsKey(name);
	}

	public static Collection<BadPlayer> getPlayers()
	{
		return maps.values();
	}
	
}