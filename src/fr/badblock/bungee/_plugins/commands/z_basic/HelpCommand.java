package fr.badblock.bungee._plugins.commands.z_basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

public class HelpCommand extends BadCommand
{

	public HelpCommand() {
		super("help", "", "?", "bukkit:help", "minecraft:help", "craftbukkit:help", "bukkit:?", "minecraft:?",
				"craftbukkit:?");
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		I19n.sendMessage(sender, "commands.help.message", sender.getName());
	}

}