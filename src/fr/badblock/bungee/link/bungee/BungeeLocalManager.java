package fr.badblock.bungee.link.bungee;

import java.util.Optional;
import java.util.UUID;

import fr.badblock.bungee.players.BadPlayer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
public class BungeeLocalManager
{
	
	@Getter@Setter
	private static BungeeLocalManager instance = new BungeeLocalManager();

	public BadPlayer getPlayer(String playerName)
	{
		return BadPlayer.get(playerName);
	}
	
	public BadPlayer getPlayer(UUID uuid)
	{
		Optional<BadPlayer> optional = BadPlayer.getPlayers().parallelStream().filter(player -> player.getUniqueId().equals(uuid)).findFirst();
		if (!optional.isPresent())
		{
			return null;
		}
		return optional.get();
	}
	
	public BadPlayer getPlayer(ProxiedPlayer player)
	{
		return BadPlayer.get(player);
	}
	
	public boolean isOnline(String playerName)
	{
		return BadPlayer.has(playerName);
	}
	
	public boolean isOnline(UUID uuid)
	{
		return BadPlayer.getPlayers().parallelStream().filter(player -> player.getUniqueId().equals(uuid)).count() > 0;
	}
	
}
