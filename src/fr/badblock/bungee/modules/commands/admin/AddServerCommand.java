package fr.badblock.bungee.modules.commands.admin;

import fr.badblock.api.common.sync.bungee.BungeeUtils;
import fr.badblock.api.common.sync.bungee.objects.ServerObject;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Add a server
 * 
 * @author xMalware
 *
 */
public class AddServerCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public AddServerCommand() {
		super("addserver", "bungee.command.addserver", "as");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If no argument has been entered, i.e. the user's message
		if (args.length != 3) {
			// A message is sent to him containing the information allowing him to take note
			// of the use of the command.
			I19n.sendMessage(sender, "bungee.commands.addserver.usage", null);
			// Nothing has been written from him, no argument. After we explain it to him,
			// we stop there.
			return;
		}

		String name = args[0];
		String ip = args[1];
		int port = -1;
		
		try
		{
			port = Integer.parseInt(args[2]);
		}
		catch (Exception error)
		{
			// Error
			I19n.sendMessage(sender, "bungee.commands.addserver.portmustbeanint", null);
			return;
		}
		
		if (BungeeCord.getInstance().getServers().containsKey(name))
		{
			I19n.sendMessage(sender, "bungee.commands.addserver.alreadyadded", null);
			return;
		}
		
		ServerObject serverObject = new ServerObject(name, ip, port);
		
		BungeeUtils.addServer(BadBungee.getInstance().getRabbitService(), serverObject);
		
		I19n.sendMessage(sender, "bungee.commands.addserver.added", null, name);
	}

}