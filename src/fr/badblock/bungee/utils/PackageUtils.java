package fr.badblock.bungee.utils;

import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListener;
import net.md_5.bungee.api.plugin.Plugin;

public class PackageUtils {

	public static void instanciateListeners(Plugin plugin, String... paths) throws IOException {
		URL url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();

		ZipInputStream zip = new ZipInputStream(url.openStream());
		ZipEntry entry = null;

		while((entry = zip.getNextEntry()) != null)
		{
			String finded = null;

			for(String path : paths){
				if(entry.getName().startsWith( path.replace(".", "/") ))
				{
					finded = entry.getName().replace("/", ".");
					break;
				}}

			if(finded != null && entry.getName().endsWith(".class"))
			{
				try {
					String className = finded.substring(0, finded.length() - 6);

					Class<?> clazz = plugin.getClass().getClassLoader().loadClass(className);

					if(inheritFrom(clazz, BadListener.class) 
							|| inheritFrom(clazz, RabbitListener.class)
							|| inheritFrom(clazz, BadCommand.class))
					{
						instanciate(clazz);
					}
				} catch(Exception exception) {
					exception.printStackTrace();
				}
			}

		}
	}
	
	private static boolean inheritFrom(Class<?> clazz, Class<?> from){
		while(clazz != Object.class)
		{
			if(clazz == from)
				return true;

			clazz = clazz.getSuperclass();
		}

		return false;
	}

	private static Object instanciate(Class<?> clazz) throws Exception {
		try {
			return clazz.getConstructor().newInstance();
		} catch(NoSuchMethodException e){
			return null;
		}
	}

}
