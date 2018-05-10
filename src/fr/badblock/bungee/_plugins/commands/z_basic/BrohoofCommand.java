package fr.badblock.bungee._plugins.commands.z_basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

public class BrohoofCommand extends BadCommand
{

	public BrohoofCommand() {
		super("brohoof", "", ")");
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		I19n.sendMessage(sender, "commands.broohof.message", sender.getName());
	}

}