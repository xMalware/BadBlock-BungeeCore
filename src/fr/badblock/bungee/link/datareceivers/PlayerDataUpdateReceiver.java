package fr.badblock.bungee.link.datareceivers;

import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * 
 * Player data update receiver object
 * 
 * @author xMalware
 *
 */
public class PlayerDataUpdateReceiver {

	/**
	 * Player data
	 * 
	 * @param Set
	 *            the new player data
	 * @return Returns the new player data
	 */
	private JsonObject data;

	/**
	 * Player name
	 * 
	 * @param Set
	 *            the new player name
	 * @return Returns the player name
	 */
	private String playerName;

}