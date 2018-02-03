package fr.badblock.bungee.players;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.badblock.bungee.utils.ChatColorUtils;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonUtils;
import fr.toenga.common.utils.data.Callback;
import fr.toenga.common.utils.general.StringUtils;
import fr.toenga.common.utils.i18n.I18n;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadPlayer extends BadOfflinePlayer
{

	private static Map<String, BadPlayer> maps 			= new HashMap<>();

	public BadPlayer(PreLoginEvent preLoginEvent, PendingConnection pendingConnection)
	{
		super (pendingConnection.getName(), new Callback<BadOfflinePlayer>()
		{

			@Override
			public void done(BadOfflinePlayer result, Throwable error) {
				result.getLoadedCallbacks().forEach(callback -> callback.done((BadPlayer) result, null));
				BadBungee.log(ChatColor.GREEN + "Loaded data for " + pendingConnection.getName());
			}

		});
		setName(pendingConnection.getName());
		put();
	}
	
	public String getLastServer()
	{
		return getDbObject().get("lastServer").toString();
	}
	
	public boolean isLogged()
	{
		return getDbObject().containsField("lastServer") && getLastServer() != null && !getLastServer().startsWith("login");
	}

	public void reload()
	{
		loadData();
	}

	@SuppressWarnings("deprecation")
	public void sendLocalMessage(String... messages) {
		if (toProxiedPlayer() == null) return;
		toProxiedPlayer().sendMessages(ChatColorUtils.translateColors('&', messages));
	}

    public void sendLocalJsonMessage(String... jsons) {
        if (toProxiedPlayer() == null) return;
        McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&', jsons)));
    }
	
	@SuppressWarnings("deprecation")
	public void sendTranslatedLocalMessage(String key, Object... args) {
		if (toProxiedPlayer() == null) return;
		toProxiedPlayer().sendMessages(ChatColorUtils.translateColors('&', I18n.getInstance().get(getLocale(), key, args)));
	}

    public void sendTranslatedLocalJsonMessage(String key, Object... args) {
        if (toProxiedPlayer() == null) return;
        McJsonUtils.sendJsons(toProxiedPlayer(), McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&',  I18n.getInstance().get(getLocale(), key, args))));
    }
	
	public void sendOutgoingMessage(String... messages) {
		if (toProxiedPlayer() != null) sendLocalMessage(messages);
		else BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_MESSAGE, StringUtils.toOneString(messages)));
	}

    public void sendOutgoingJsonMessage(String... jsons) {
        if (toProxiedPlayer() == null) return;
        else BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, StringUtils.toOneString(jsons)));
    }
	
	public void sendTranslatedOutgoingMessage(String key, Object... args) {
		if (toProxiedPlayer() != null) sendTranslatedLocalMessage(key, args);
		else BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_MESSAGE, StringUtils.toOneString(I18n.getInstance().get(getLocale(), key, args))));
	}

    public void sendTranslatedOutgoingJsonMessage(String key, Object... args) {
        if (toProxiedPlayer() != null) sendTranslatedLocalJsonMessage(key, args);
        else BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, StringUtils.toOneString(I18n.getInstance().get(getLocale(), key, args))));
    }

    public void sendTranslatedOutgoingMCJson(McJson mcjson) {
        if (toProxiedPlayer() != null) sendLocalJsonMessage(mcjson.toString());
        else
            BungeeManager.getInstance().sendPacket(new PlayerPacket(getName(), PlayerPacketType.SEND_JSON_MESSAGE, (mcjson.toString())));
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