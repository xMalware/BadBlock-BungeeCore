package fr.badblock.bungee.utils;

import org.bson.BSONObject;

import com.google.gson.JsonObject;

import fr.toenga.common.utils.general.GsonUtils;

public class ObjectUtils
{

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static <T> T getOr(BSONObject dbObject, String keyName, T d)
	{
		if (dbObject == null || !dbObject.containsKey(keyName)) return d;
		return (T) dbObject.get(keyName);
	}

	public static JsonObject getJsonObject(String part)
	{
		return GsonUtils.getPrettyGson().fromJson(part, JsonObject.class);
	}

	public static JsonObject getJsonObject(Object object)
	{
		return GsonUtils.getPrettyGson().fromJson(GsonUtils.getGson().toJson(object), JsonObject.class);
	}

}
