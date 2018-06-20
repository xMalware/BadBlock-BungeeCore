package fr.badblock.bungee.modules.login.datamanager;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 *
 * @author xMalware
 *
 */
public class SkeletonConnectorListener extends BadListener {

	private ServerInfo	skeleton;
	private ServerInfo	lobby;

	public SkeletonConnectorListener()
	{
		skeleton = BungeeCord.getInstance().constructServerInfo("skeleton", new InetSocketAddress("127.0.0.1", 8889), "skeleton", false);
		BungeeCord.getInstance().getServers().put("skeleton", skeleton);
		lobby = BungeeCord.getInstance().constructServerInfo("lobby", new InetSocketAddress("127.0.0.1", 8890), "lobby", false);
		BungeeCord.getInstance().getServers().put("lobby", lobby);
	}

	/**
	 * When someone connects to a server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onServerConnect(ServerConnectEvent event)
	{
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		ServerInfo target = event.getTarget();
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		System.out.println(target.getName());
		
		if (target != null && target.getName().equalsIgnoreCase( skeleton.getName() ))
		{
			ServerInfo serverInfo = this.roundrobinLogin();
			if (serverInfo != null)
			{
				badPlayer.sendTranslatedOutgoingMessage("bungee.teleport.login", null);
				event.setTarget(serverInfo);
			}
			else
			{
				if (getFallbackServer() == null || !getFallbackServer().equals(proxiedPlayer.getServer().getInfo()))
				{
					if (getFallbackServer() != null)
					{
						badPlayer.sendTranslatedOutgoingMessage("bungee.errors.nologintp", null);
						event.setTarget(getFallbackServer());
					}
					else
					{
						badPlayer.kick(badPlayer.getTranslatedMessage("bungee.errors.nologinkick", null));
					}
				}
			}
		}
		else if(target == null || (target != null && target.getName().equals("lobby")))
		{
			ServerInfo serverInfo = this.roundrobinHub();

			if (serverInfo != null)
			{
				badPlayer.sendTranslatedOutgoingMessage("bungee.teleport.hub", null);
				event.setTarget(serverInfo);
			}else{
				if (getFallbackServer() == null || !getFallbackServer().equals(proxiedPlayer.getServer().getInfo()))
				{
					if (getFallbackServer() != null)
					{
						badPlayer.sendTranslatedOutgoingMessage("bungee.errors.nohubtp", null);
						event.setTarget(getFallbackServer());
					}
					else
					{
						badPlayer.kick(badPlayer.getTranslatedMessage("bungee.errors.nohubkick", null));
					}
				}
			}
		}
	}

	private ServerInfo getFallbackServer()
	{
		return BungeeCord.getInstance().getServerInfo("fallback");
	}

	private ServerInfo roundrobinLogin() {
		List<ServerInfo> servers = new ArrayList<>();
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("login")) continue;
			int maxPlayers = Integer.MAX_VALUE;
			try
			{
				maxPlayers = Integer.parseInt(serverInfo.getMotd());
			}
			catch (Exception error)
			{

			}
			if (serverInfo.getPlayers().size() >= maxPlayers) continue;
			servers.add(serverInfo);
		}
		if (servers == null || servers.isEmpty()) return null;
		return servers.get(new SecureRandom().nextInt(servers.size()));
	}

	public ServerInfo roundrobinHub() {
		List<ServerInfo> servers = new ArrayList<>();
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			if (serverInfo == null) continue;
			if (!serverInfo.getName().startsWith("hub")) continue;
			int maxPlayers = Integer.MAX_VALUE;
			try
			{
				maxPlayers = Integer.parseInt(serverInfo.getMotd());
			}
			catch (Exception error)
			{

			}
			if (serverInfo.getPlayers().size() >= maxPlayers) continue;
			servers.add(serverInfo);
		}
		if (servers == null || servers.isEmpty()) return null;
		return servers.get(new SecureRandom().nextInt(servers.size()));
	}

}