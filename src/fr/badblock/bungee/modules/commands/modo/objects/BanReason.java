package fr.badblock.bungee.modules.commands.modo.objects;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.reflect.TypeToken;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.GsonUtils;
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
	private Map<String, BanIndex> punishments;

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

		// Get temp map
		Map<String, DBObject> temp = GsonUtils.getGson().fromJson(dbObject.get("punishments").toString(),
				collectionType);

		// For each entry
		for (Entry<String, DBObject> entry : temp.entrySet()) {
			// Insert into punishment map
			punishments.put(entry.getKey(), new BanIndex(entry.getValue()));
		}
	}

}