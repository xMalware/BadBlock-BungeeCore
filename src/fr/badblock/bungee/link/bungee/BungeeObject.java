package fr.badblock.bungee.link.bungee;

import java.util.Map;

import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.time.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * Object representing a BungeCord node as well as all its affiliated data,
 * such as the name of the node, the IP address of the node,
 * the users connected to this node and its expiration time.
 * 
 * @author xMalware
 *
 */
public class BungeeObject
{

	/**
	 * Name of the BungeeCord node
	 * @param New name of the BungeeCord node
	 * @return Current name of the BungeeCord node
	 */
	private String					name;
	
	/**
	 * IP of the BungeeCord node
	 * @param New IP of the BungeeCord node
	 * @return Current IP of the BungeeCord name
	 */
	private String 		 			ip;
	
	/**
	 * Player list of the BungeeCord node
	 * @param New Player list of the BungeeCord node
	 * @return Current Player list of the BungeeCord node
	 */
	private Map<String, BadPlayer>	usernames;
	
	/**
	 * Expiry timestamp of the BungeeCord node
	 * @param New expiry timestamp of the BungeeCord node
	 * @return Current expiry timestamp of the BungeeCord node
	 */
	private long					timestamp;

	/**
	 * Refresh the node data with the new player list
	 * @param New player list of the BungeeCord node
	 */
	public void refresh(Map<String, BadPlayer> usernames)
	{
		// Set player list to the object
		setUsernames(usernames);
		// Set timestamp to the object
		setTimestamp(BungeeTask.getTimestamp());
	}

	/**
	 * If the BungeeCord node stills valid
	 * @return if the BungeeCord node stills valid or not
	 */
	public boolean isValid()
	{
		// Check with the timestamp
		return TimeUtils.isValid(getTimestamp());
	}

}
