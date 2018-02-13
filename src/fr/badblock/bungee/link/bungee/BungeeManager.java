package fr.badblock.bungee.link.bungee;

import com.mongodb.*;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacket;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacketType;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.rabbit.BadBungeeQueues;
import fr.badblock.bungee.utils.Filter;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacket;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.toenga.common.utils.data.Callback;
import fr.toenga.common.utils.general.GlobalFlags;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
public class BungeeManager
{
	
	@Getter@Setter
	private static BungeeManager instance = new BungeeManager();

	private Map<String, BungeeObject> bungees = new HashMap<>();

	private ServerPing	serverPing;
	private int			tempOnlinePlayers;
	private int			slots;
	
	public void add(BungeeObject bungeeObject)
	{
		bungees.put(bungeeObject.getName(), bungeeObject);
	}
	
	public void broadcast(String... messages)
	{
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<String> iterator = Arrays.asList(messages).iterator();
		while (iterator.hasNext())
		{
			String message = iterator.next();
			stringBuilder.append(message + (iterator.hasNext() ? System.lineSeparator() : ""));
		}
		sendPacket(new BungeePacket(BungeePacketType.BROADCAST, stringBuilder.toString()));
	}

    public void targetedBrodcast(String permission, String... messages) {
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendOutgoingMessage(messages));
	}

    public void targetedTranslatedBroadcast(String permission, String key, Object... args) {
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendTranslatedOutgoingMessage(key, args));
	}

    public void targetedJsonBrodcast(String permission, String... messages) {
        getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendOutgoingJsonMessage(messages));
    }

    public void targetedTranslatedJsonBroadcast(String permission, String key, Object... args) {
        getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendTranslatedOutgoingJsonMessage(key, args));
    }

    public void targetedTranslatedMCJsonBroadcast(String permission, McJson mcJson) {
        getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendTranslatedOutgoingMCJson(mcJson));
    }

    public void targetedBrodcast(Filter<BadPlayer> filter, String... messages) {
        filter.filterList(getLoggedPlayers()).forEach(player -> player.sendOutgoingMessage(messages));
    }

    public void targetedTranslatedBroadcast(Filter<BadPlayer> filter, String key, Object... args) {
        filter.filterList(getLoggedPlayers()).forEach(player -> player.sendTranslatedOutgoingMessage(key, args));
    }

    public void targetedJsonBrodcast(Filter<BadPlayer> filter, String... messages) {
        filter.filterList(getLoggedPlayers()).forEach(player -> player.sendOutgoingJsonMessage(messages));
    }

    public void targetedTranslatedJsonBroadcast(Filter<BadPlayer> filter, String key, Object... args) {
        filter.filterList(getLoggedPlayers()).forEach(player -> player.sendTranslatedOutgoingJsonMessage(key, args));
    }

    public void targetedTranslatedMCJsonBroadcast(Filter<BadPlayer> filter, McJson mcJson) {
        filter.filterList(getLoggedPlayers()).forEach(player -> player.sendTranslatedOutgoingMCJson(mcJson));
    }


	
	public BadPlayer getBadPlayer(String name)
	{
        List<BadPlayer> list = getLoggedPlayers(p -> p.getName().equalsIgnoreCase(name));
		return list.isEmpty() ? null : list.get(0);
	}

    public BadPlayer getBadPlayer(UUID uuid) {
        List<BadPlayer> list = getLoggedPlayers(p -> p.getUniqueId().toString().equalsIgnoreCase(uuid.toString()));
        return list.isEmpty() ? null : list.get(0);
    }
	
	public BadPlayer getBadPlayer(ProxiedPlayer proxiedPlayer)
	{
		return BadPlayer.get(proxiedPlayer);
	}
	
	public void getOfflinePlayer(String name, Callback<BadOfflinePlayer> callback)
	{
		name = name.toLowerCase();
		if (BadPlayer.has(name))
		{
			callback.done(BadPlayer.get(name), null);
			return;
		}
		new BadOfflinePlayer(name, callback);
	}
	
	public ServerPing generatePing()
	{
		if (serverPing != null && GlobalFlags.has(serverPing))
		{
			return serverPing;
		}
		BadBungee bungee = BadBungee.getInstance();
		MongoService mongoService = bungee.getMongoService();
		DB db = mongoService.getDb();
		DBCollection collection = db.getCollection("serverInfo");
		BasicDBObject query = new BasicDBObject();
		DBCursor cursor = collection.find(query);
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			String json = dbObject.toString();
			ServerPing serverPing = bungee.getGson().fromJson(json, ServerPing.class);
			setSlots(serverPing.getPlayers().getMax());
			GlobalFlags.set(serverPing, 1000);
			// Manage
			serverPing.getPlayers().setOnline(getOnlinePlayers());
			return serverPing;
		}
		return null;
	}
	
	public void sendPacket(PlayerPacket playerPacket)
	{
		BadBungee badBungee = BadBungee.getInstance();
		String json = badBungee.getGson().toJson(playerPacket);
		badBungee.getRabbitService().sendPacket(new RabbitPacket(new RabbitPacketMessage(5000, json), 
				BadBungeeQueues.PLAYER_PROCESSING, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER));
	}

	public void sendPacket(BungeePacket bungeePacket)
	{
		BadBungee badBungee = BadBungee.getInstance();
		String json = badBungee.getGson().toJson(bungeePacket);
		badBungee.getRabbitService().sendPacket(new RabbitPacket(new RabbitPacketMessage(5000, json), 
				BadBungeeQueues.BUNGEE_PROCESSING, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER));
	}
	
	public List<BadPlayer> getLoggedPlayers(Predicate<BadPlayer> predicate)
	{
		List<BadPlayer> badPlayers = new ArrayList<>();
		bungees.values().parallelStream().forEach(
				bungee -> 
				badPlayers.addAll(bungee.getUsernames().values().parallelStream().filter(player -> player.isLogged() && predicate.test(player)).collect(Collectors.toList())));
		return badPlayers;
	}

    public List<BadPlayer> getLoggedPlayers()
    {
        return getLoggedPlayers(a -> true);
    }
	
	public boolean hasUsername(String name)
	{
		final String toLowerName = name.toLowerCase();
		long count = bungees.values().parallelStream().filter(bungee -> 
		bungee.getUsernames().keySet().parallelStream().filter(n -> n.toLowerCase().equals(toLowerName)).count() > 0).count();
		return count > 0;
	}
	
	public int getRealTimeOnlinePlayers()
	{
        return bungees.values().parallelStream().filter(this::isValid).map(bungee -> bungee.getUsernames().size()).mapToInt(Number::intValue).sum();
	}
	
	public int getOnlinePlayers()
	{
		if (GlobalFlags.has("onlinePlayers"))
		{
			return tempOnlinePlayers;
		}
		GlobalFlags.set("onlinePlayers", 1_000);
		return tempOnlinePlayers = getRealTimeOnlinePlayers();
	}
	
	public boolean isValid(BungeeObject bungeeObject)
	{
		return bungeeObject.getTimestamp() > System.currentTimeMillis();
	}
	
}
