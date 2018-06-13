package fr.badblock.bungee.modules.commands.modo.objects;

import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * 
 * Ban index
 * 
 * @author xMalware
 *
 */
public class BanIndex {

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
	private long time;

	/**
	 * Constructor
	 * 
	 * @param Database
	 *            object
	 */
	public BanIndex(DBObject dbObject) {
		// Set index
		index = Integer.parseInt(dbObject.get("index").toString());
		// Set time
		time = Long.parseLong(dbObject.get("time").toString());
	}

}