package fr.badblock.bungee.players;

import java.util.HashMap;
import java.util.Map;

import org.bson.BSONObject;

import fr.badblock.bungee.BadBungee;
import fr.toenga.common.utils.data.Callback;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadPlayer extends BadOfflinePlayer
{

	private static Map<String, BadPlayer> maps 			= new HashMap<>();

	private ProxiedPlayer		proxiedPlayer;
	private PendingConnection	pendingConnection;
	private PreLoginEvent		preLoginEvent;
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
		setPreLoginEvent(preLoginEvent);
		setPendingConnection(pendingConnection);
		put();
	}

	public void load(ProxiedPlayer proxiedPlayer)
	{
		setProxiedPlayer(proxiedPlayer);
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(String message)
	{
		if (getProxiedPlayer() == null)
		{
			return;
		}
		getProxiedPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	private void put()
	{
		maps.put(pendingConnection.getName(), this);
		BadBungee.log(ChatColor.GREEN + getPendingConnection().getName() + " is now connected.");
	}	

	public void remove()
	{
		maps.remove(pendingConnection.getName());
		BadBungee.log(ChatColor.RED + getPendingConnection().getName() + " is now disconnected.");
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

}