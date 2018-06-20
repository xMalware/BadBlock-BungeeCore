package fr.badblock.bungee.modules.commands.modo.objects;

import com.mongodb.BasicDBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * 
 * Punishment index
 * 
 * @author xMalware
 *
 */
public class PunishmentIndex {

	/**
	 * Index
	 * 
	 * @param Set
	 *            the new index
	 * @return Returns the current index
	 */
	private int index;

	/**
	 * Time
	 * 
	 * @param Set
	 *            the new time
	 * @return Returns the current time
	 */
	private String time;

	/**
	 * Constructor
	 * 
	 * @param Database
	 *            object
	 */
	public PunishmentIndex(BasicDBObject dbObject) {
		// Set index
		index = dbObject.getInt("index");
		// Set time
		time = dbObject.getString("time");
	}

}