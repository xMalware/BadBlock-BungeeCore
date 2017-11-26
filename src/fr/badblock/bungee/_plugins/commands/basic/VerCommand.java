package fr.badblock.bungee._plugins.commands.basic;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.listeners.abstracts.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

public class VerCommand extends BadCommand
{

	public VerCommand() {
		super("ver", "", "version", "spigot", "badblock");
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		I19n.sendMessage(sender, "commands.ver.message", sender.getName(), BadBungee.getInstance().getDescription().getVersion());
	}

}