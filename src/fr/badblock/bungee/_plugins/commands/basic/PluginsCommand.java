package fr.badblock.bungee._plugins.commands.basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

public class PluginsCommand extends BadCommand
{

	public PluginsCommand() {
		super("plugins", "test", "pl", "bukkit:plugins", "bukkit:pl");
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		I19n.sendMessage(sender, "commands.plugins.message", sender.getName());
	}

}