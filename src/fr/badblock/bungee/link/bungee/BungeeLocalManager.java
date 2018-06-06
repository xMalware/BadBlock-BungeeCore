package fr.badblock.bungee.link.bungee;

import java.util.Optional;
import java.util.UUID;

import fr.badblock.bungee.players.BadPlayer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
/**
 * 
 * Local manager of the current BungeeCord instance, without any network connection.
 * The purpose of this class is to be able to recover players connected to the current BungeeCord, as well as their data.
 * 
 * @author xMalware
 *
 */
public class BungeeLocalManager
{
	
	/**
	 * Unique BungeeLocalManager instance
	 * @param New BungeeLocalManager
	 * @return Current BungeeLocalManagerInstance
	 */
	@Getter@Setter private static BungeeLocalManager instance = new BungeeLocalManager();

	/**
	 * Get a local player with his player name
	 * @param current player name
	 * @return BadPlayer object, with data
	 */
	public BadPlayer getPlayer(String playerName)
	{
		// Get BadPlayer from a map
		return BadPlayer.get(playerName);
	}
	
	/**
	 * Recover a local player with his universally unique identifier
	 * @param current universally unique identifier
	 * @return BadPlayer object, with data
	 */
	public BadPlayer getPlayer(UUID uuid)
	{
		// Search for the player with the corresponding unique identifier 
		Optional<BadPlayer> optional = BadPlayer.getPlayers().parallelStream().filter(player -> player.getUniqueId().equals(uuid)).findFirst();
		// If no results have been found
		if (!optional.isPresent())
		{
			// Then we return null
			return null;
		}
		// We return the data
		return optional.get();
	}
	
	/**
	 * Get a local player with a ProxiedPlayer object
	 * @param current ProxiedPlayer object
	 * @return BadPlayer object, with data
	 */
	public BadPlayer getPlayer(ProxiedPlayer player)
	{
		// Get BadPlayer from a map
		return BadPlayer.get(player);
	}
	
	/**
	 * Check if a local player is online with his username
	 * @param current player name you're looking for
	 * @return the player is or isn't online 
	 */
	public boolean isOnline(String playerName)
	{
		// Check from a map
		return BadPlayer.has(playerName);
	}
	
	/**
	 * Check if a local player is online with his universally unique identifier
	 * @param current universally unique identifier
	 * @return the player is or isn't online 
	 */
	public boolean isOnline(UUID uuid)
	{
		// We count the number of local players online and if the count is more than 0, then he's online
		return BadPlayer.getPlayers().parallelStream().filter(player -> player.getUniqueId().equals(uuid)).count() > 0;
	}
	
}
