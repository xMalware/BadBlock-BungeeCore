package fr.badblock.bungee.players.layer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import fr.toenga.common.utils.general.GsonUtils;

public class BadPlayerSettingsSerializer
{

	private static Type collectionType = new TypeToken<Map<String, String>>() {}.getType();

	public static String serialize(BadPlayerSettings settings)
	{
		Map<String, String> map = new HashMap<>();
		for (Field f : BadPlayerSettings.class.getFields()) 
		{
			if (!f.isAnnotationPresent(SettingIgnore.class))
			{
				try
				{
					if (!f.isAccessible()) f.setAccessible(true);
					map.put(f.getName(), GsonUtils.getGson().toJson(f.get(settings)));
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
		return GsonUtils.getGson().toJson(map, collectionType);
	}

	public static BadPlayerSettings deserialize(String json)
	{
		BadPlayerSettings settings = new BadPlayerSettings();
		Map<String, String> map = GsonUtils.getGson().fromJson(json, collectionType);
		for (String s : map.keySet())
		{
			try
			{
				Field f = BadPlayerSettings.class.getField(s);
				if (!f.isAccessible())
				{
					f.setAccessible(true);
				}
				if (!f.isAnnotationPresent(SettingIgnore.class))
				{
					f.set(settings, GsonUtils.getGson().fromJson(map.get(s), f.getType()));
				}
			} catch (NoSuchFieldException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return settings;
	}

}