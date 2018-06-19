package fr.badblock.bungee.modules.commands.modo.objects;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class BanReason {

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
	private Map<String, List<BanIndex>> punishments;

	/**
	 * Constructor
	 * 
	 * @param Database
	 *            object
	 */
	public BanReason(DBObject dbObject) {
		// Set the name
		name = dbObject.get("name").toString();
		// Create a map!
		punishments = new HashMap<>();

		// Get table
		BasicDBList dbList = (BasicDBList) dbObject.get("punishments");
		// Get database array
		BasicDBObject[] dbArray = dbList.toArray(new BasicDBObject[] {});

		// For each entry
		for (BasicDBObject basicDbObject : dbArray) {
			String name = basicDbObject.getString("name");
			// If the punishment table is nul
			if (!punishments.containsKey(name))
			{
				punishments.put(name, new ArrayList<>());
			}
			List<BanIndex> pun = punishments.get(name);
			// Insert into punishment map
			pun.add(new BanIndex(basicDbObject));
			punishments.put(name, pun);
		}
	}

}