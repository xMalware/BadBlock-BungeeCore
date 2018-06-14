package fr.badblock.bungee.modules.commands.admin;

import fr.badblock.api.common.sync.bungee.packets.BungeePacket;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Force kick
 * 
 * @author xMalware
 *
 */
public class ForceKickCommand extends BadCommand {

	public static String prefix = "bungee.commands.forcekick.";

	/**
	 * Command constructor
	 */
	public ForceKickCommand() {
		super("forcekick", "bungee.command.forcekick", "fkick");
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
			I19n.sendMessage(sender, prefix + "usage", null);
			// Nothing has been written from him, no argument. After we explain it to him,
			// we stop there.
			return;
		}

		String name = args[0];

		BungeeManager bungeeManager = BungeeManager.getInstance();

		if (!bungeeManager.hasUsername(name)) {
			I19n.sendMessage(sender, prefix + "offline", null);
			return;
		}

		bungeeManager.sendPacket(new BungeePacket(BungeePacketType.FORCE_KICK, name));

		I19n.sendMessage(sender, prefix + "kicked", null, name);
	}

}