package fr.badblock.bungee.link.datareceivers;

import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * 
 * Player data update sender object
 * 
 * @author xMalware
 *
 */
public class PlayerDataUpdateSender {

	/**
	 * Player data
	 * 
	 * @param Set
	 *            the new player data
	 * @return Returns the current player data
	 */
	private JsonObject data;

	/**
	 * Player name
	 * 
	 * @param Set
	 *            the new player name
	 * @return Returns the current player name
	 */
	private String playerName;

}