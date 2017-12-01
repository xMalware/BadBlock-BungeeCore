package fr.badblock.bungee.link.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacket;
import fr.badblock.bungee.link.processing.bungee.abstracts.BungeePacketType;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.rabbit.BadBungeeQueues;
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
	
	public void targetedBrodcast(String permission, String... messages)
	{
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendOutgoingMessage(messages));
	}
	
	public void targetedTranslatedBroadcast(String permission, String key, Object... args)
	{
		getLoggedPlayers(player -> player.hasPermission(permission)).forEach(player -> player.sendTranslatedOutgoingMessage(key, args));
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
	
	public boolean hasUsername(String name)
	{
		final String toLowerName = name.toLowerCase();
		long count = bungees.values().parallelStream().filter(bungee -> 
		bungee.getUsernames().keySet().parallelStream().filter(n -> n.toLowerCase().equals(toLowerName)).count() > 0).count();
		return count > 0;
	}
	
	public int getRealTimeOnlinePlayers()
	{
		return bungees.values().parallelStream().filter(bungee -> isValid(bungee)).
				map(bungee -> bungee.getUsernames().size()).mapToInt(Number::intValue).sum();
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
