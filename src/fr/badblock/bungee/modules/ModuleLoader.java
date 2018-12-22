package fr.badblock.bungee.modules;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import fr.badblock.bungee.BadBungee;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.plugin.PluginManager;

public class ModuleLoader
{

	@Getter
	private static ModuleLoader instance;

	private PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
	private File folder = new File("modules/");
	private final Yaml yaml;

	public ModuleLoader()
	{
		instance = this;
		// Ignore unknown entries in the plugin descriptions
		Constructor yamlConstructor = new Constructor();
		PropertyUtils propertyUtils = yamlConstructor.getPropertyUtils();
		yamlConstructor.setPropertyUtils( propertyUtils );
		yaml = new Yaml( yamlConstructor );

		System.out.println(folder.getAbsolutePath());
		this.detectAll();
	}

	public boolean unload(String name)
	{
		Plugin plugin = pluginManager.getPlugin(name);
		if (plugin == null)
		{
			return false;
		}

		if (plugin.getDescription() == null)
		{
			return false;
		}

		if (plugin.getDescription().getName().equalsIgnoreCase("BadBlockBungee"))
		{
			return false;
		}

		try {
			pluginManager.unloadPlugin(plugin);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String reload(String name)
	{
		if (name.equalsIgnoreCase("BadBlockBungee"))
		{
			return "Unknown module";
		}
		
		Plugin plugin = null;
		for (Plugin pl : pluginManager.getPlugins())
		{
			if (pl != null && pl.getDescription() != null && pl.getDescription().getName().equalsIgnoreCase(name))
			{
				plugin = pl;
			}
		}

		if (plugin != null && plugin.getDescription() != null)
		{
			if (!unload(name))
			{
				return "Unable to unload";
			}
		}

		File file = new File(folder, name + ".jar");

		if (!file.exists())
		{
			return "Unknown file";
		}

		if (!detect(file))
		{
			return "Unable to load. Please see console.";
		}

		return null;
	}

	public List<Plugin> getModules()
	{
		List<Plugin> plugins = new ArrayList<>();
		for (Plugin plugin : pluginManager.getPlugins())
		{
			if (plugin == null || plugin.getDescription() == null)
			{
				continue;
			}

			if (plugin.getDescription().getName() != null &&
					plugin.getDescription().getName().toLowerCase().startsWith("module"))
			{
				plugins.add(plugin);
			}
		}

		return plugins;
	}

	public boolean detect(File file)
	{
		if ( file.exists() && file.isFile() && file.getName().endsWith( ".jar" ) )
		{
			try ( JarFile jar = new JarFile( file ) )
			{
				JarEntry pdf = jar.getJarEntry( "bungee.yml" );
				if ( pdf == null )
				{
					pdf = jar.getJarEntry( "plugin.yml" );
				}
				if (pdf == null)
				{
					BadBungee.log("§c[Module] Module from " + file.getName() + " must have a plugin.yml");
					return false;
				}

				try ( InputStream in = jar.getInputStream( pdf ) )
				{
					PluginDescription desc = yaml.loadAs( in, PluginDescription.class );
					if (desc.getName() == null)
					{
						BadBungee.log("§c[Module] Module from " + file.getName() + " has no name");
						return false;
					}

					if (desc.getMain() == null)
					{
						BadBungee.log("§c[Module] Module from " + file.getName() + " has no main");
						return false;
					}

					desc.setFile( file );
					BadBungee.log("§e[Module] Module " + desc.getName() + " detected. Loading..");
					pluginManager.loadPlugin(file);
										
					pluginManager.loadPlugins();

					Plugin plugin = null;
					for (Plugin pl : pluginManager.getPlugins())
					{
						if (pl != null && pl.getDescription() != null && pl.getDescription().getName().equalsIgnoreCase(desc.getName()))
						{
							plugin = pl;
						}
					}

					if (plugin == null)
					{
						BadBungee.log("§c[Module] Module from " + file.getName() + " can't be loaded (null plugin?)");
						return false;
					}

	                plugin.onEnable();
	                ProxyServer.getInstance().getLogger().log( Level.INFO, "Enabled plugin {0} version {1} by {2}", new Object[]
	                {
	                    plugin.getDescription().getName(), plugin.getDescription().getVersion(), plugin.getDescription().getAuthor()
	                } );
					return true;
				}
			} catch ( Exception ex )
			{
				BadBungee.log("§c[Module] Module from " + file.getName() + " can't be loaded.");
				ex.printStackTrace();
				return false;
			}
		}
		else
		{
			BadBungee.log("§c[Module] Module " + file.getName() + " must be a jar file.");
			return false;
		}
	}

	public void detectAll()
	{
		for (File file : folder.listFiles())
		{
			detect(file);
		}
	}

}
