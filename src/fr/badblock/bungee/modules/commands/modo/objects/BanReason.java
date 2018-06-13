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
public class BanReason
{
	
	public static final Type collectionType = new TypeToken<TreeMap<String, DBObject>>() {}.getType();

	private String					name;
	private Map<String, BanIndex>	punishments;
	
	public BanReason(DBObject dbObject)
	{
		name = dbObject.get("name").toString();
		punishments = new HashMap<>();
		
		Map<String, DBObject> temp = GsonUtils.getGson().fromJson(dbObject.get("punishments").toString(), collectionType);
		
		for (Entry<String, DBObject> entry : temp.entrySet())
		{
			punishments.put(entry.getKey(), new BanIndex(entry.getValue()));
		}
	}
	
}
