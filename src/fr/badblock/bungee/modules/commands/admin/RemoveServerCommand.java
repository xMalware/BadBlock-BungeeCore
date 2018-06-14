package fr.badblock.bungee.modules.commands.admin;

import fr.badblock.api.common.sync.bungee.BungeeUtils;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Remove a server
 * 
 * @author xMalware
 *
 */
public class RemoveServerCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public RemoveServerCommand() {
		super("removeserver", "bungee.command.removeserver", "rs");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If no argument has been entered, i.e. the user's message
		if (args.length != 1) {
			// A message is sent to him containing the information allowing him to take note
			// of the use of the command.
			I19n.sendMessage(sender, "bungee.commands.removeserver.usage", null);
			// Nothing has been written from him, no argument. After we explain it to him,
			// we stop there.
			return;
		}

		String name = args[0];
		
		if (!BungeeCord.getInstance().getServers().containsKey(name))
		{
			I19n.sendMessage(sender, "bungee.commands.removeserver.doesntexist", null);
			return;
		}
		
		BungeeUtils.removeServer(BadBungee.getInstance().getRabbitService(), name);
		
		I19n.sendMessage(sender, "bungee.commands.addserver.removed", null, name);
	}

}