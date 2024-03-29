package fr.badblock.bungee.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bson.BSONObject;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.GsonUtils;

public class ObjectUtils {

	public static JsonObject getJsonObject(Object object) {
		return GsonUtils.getPrettyGson().fromJson(GsonUtils.getGson().toJson(object), JsonObject.class);
	}

	public static JsonObject getJsonObject(String part) {
		return GsonUtils.getPrettyGson().fromJson(part, JsonObject.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static <T> T getOr(BSONObject dbObject, String keyName, T d) {
		if (dbObject == null || !dbObject.containsKey(keyName))
			return d;
		return (T) dbObject.get(keyName);
	}

	public static DBObject toDbObject(Object object) throws Exception {
		if (object == null)
		{
			return new BasicDBObject();
		}
		BasicDBObject dbObject = new BasicDBObject();
		for (Field field : object.getClass().getFields()) {
			field.setAccessible(true);
			if (!field.isAccessible()) {
				continue;
			}
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			dbObject.put(field.getName(), field.get(object));
		}
		return dbObject;
	}

}
