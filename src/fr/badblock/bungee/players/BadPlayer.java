package fr.badblock.bungee.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bson.BSONObject;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.toenga.common.utils.data.Callback;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadPlayer extends BadOfflinePlayer
{

	private static Map<String, BadPlayer> maps 			= new HashMap<>();

	private String				name;
	private BSONObject	  		dbObject;

	public BadPlayer(PreLoginEvent preLoginEvent, PendingConnection pendingConnection)
	{
		super (pendingConnection.getName(), new Callback<BadOfflinePlayer>()
		{

			@Override
			public void done(BadOfflinePlayer result, Throwable error) {
				BadBungee.log(ChatColor.GREEN + "Loaded data for " + pendingConnection.getName());
			}

		});
		setName(pendingConnection.getName());
		put();
	}

	public void reload()
	{
		loadData();
	}

	@SuppressWarnings("deprecation")
	public void sendLocalMessage(String message)
	{
		if (toProxiedPlayer() == null)
		{
			return;
		}
		toProxiedPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public void sendOutgoingMessage(String message)
	{
		if (toProxiedPlayer() != null)
		{
			sendLocalMessage(message);
			return;
		}
		BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_MESSAGE, message));
	}

	private void put()
	{
		maps.put(getName(), this);
		BadBungee.log(ChatColor.GREEN + getName() + " is now connected.");
	}	

	public void remove()
	{
		maps.remove(getName());
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