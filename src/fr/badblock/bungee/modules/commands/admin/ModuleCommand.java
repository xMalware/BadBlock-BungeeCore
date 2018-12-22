package fr.badblock.bungee.modules.commands.admin;

import java.util.List;

import fr.badblock.bungee.modules.ModuleLoader;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * 
 * @author xMalware
 *
 */
public class ModuleCommand extends BadCommand {
	
	private static String prefix = "bungee.commands.module.";

	/**
	 * Command constructor
	 */
	public ModuleCommand() {
		super("module", "bungee.command.module", "modules");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0) {
			I19n.sendMessage(sender, prefix + "usage", null);
			return;
		}

		String subcommand = args[0];

		switch (subcommand) {
		case "?":
		case "help":
		case "aide":
			I19n.sendMessage(sender, prefix + "usage", null);
			break;
		case "reload":
			reload(sender, args);
			break;
		case "unload":
			unload(sender, args);
			break;
		case "list":
		case "l":
		case "liste":
			list(sender, args);
			break;
		default:
			I19n.sendMessage(sender, prefix + "unknowncommand", null);
			break;
		}
	}

	public void reload(CommandSender sender, String[] args) {
		if (args.length != 2 || args[1] == null) {
			I19n.sendMessage(sender, prefix + "reload.usage", null);
			return;
		}

		String pluginName = args[1];
		
		String info = ModuleLoader.getInstance().reload(pluginName);
		
		if (info == null)
		{
			I19n.sendMessage(sender, prefix + "reload.reloaded", null, pluginName);
			return;
		}
		
		I19n.sendMessage(sender, prefix + "reload.error", null, pluginName, info);
	}
	
	public void unload(CommandSender sender, String[] args) {
		if (args.length != 2 || args[1] == null) {
			I19n.sendMessage(sender, prefix + "unload.usage", null);
			return;
		}

		String pluginName = args[1];
		
		boolean info = ModuleLoader.getInstance().unload(pluginName);
		
		if (!info)
		{
			I19n.sendMessage(sender, prefix + "unload.error", null, pluginName);
			return;
		}
		
		I19n.sendMessage(sender, prefix + "unload.unloaded", null, pluginName);
	}

	public void list(CommandSender sender, String[] args) {
		List<Plugin> modules = ModuleLoader.getInstance().getModules();
		StringBuilder moduleInfo = new StringBuilder();
		for (Plugin plugin : modules)
		{
			if (moduleInfo.length() > 0)
			{
				 moduleInfo.append("§f, ");
			}
			moduleInfo.append("§a" + plugin.getDescription().getName());
		}
		
		String info = moduleInfo.toString();
		if (modules.isEmpty())
		{
			info = I19n.getMessage(sender, prefix + "list.nothing", null);
		}
		
		I19n.sendMessage(sender, prefix + "list.data", null, modules.size(), info);
	}

}