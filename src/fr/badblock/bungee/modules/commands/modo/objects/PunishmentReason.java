package fr.badblock.bungee.modules.commands.modo.objects;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * 
 * BanReason
 * 
 * @author xMalware
 *
 */
public class PunishmentReason {

	/**
	 * Collection type
	 */
	public static final Type collectionType = new TypeToken<TreeMap<String, DBObject>>() {
	}.getType();

	/**
	 * Name
	 * 
	 * @param Set
	 *            the new name
	 * @return Returns the current name
	 */
	private String name;

	/**
	 * Punishments
	 * 
	 * @param Set
	 *            the new punishments
	 * @return Returns the current punishments
	 */
	private List<PunishmentIndex> punishments;

	/**
	 * Constructor
	 * 
	 * @param Database
	 *            object
	 */
	public PunishmentReason(DBObject dbObject) {
		// Set the name
		name = dbObject.get("name").toString();
		// Create a map!
		punishments = new ArrayList<>();

		// Get table
		BasicDBList dbList = (BasicDBList) dbObject.get("punishments");
		// Get database array
		BasicDBObject[] dbArray = dbList.toArray(new BasicDBObject[] {});

		// For each entry
		for (BasicDBObject basicDbObject : dbArray) {
			// Insert into punishment map
			punishments.add(new PunishmentIndex(basicDbObject));
		}
	}

}