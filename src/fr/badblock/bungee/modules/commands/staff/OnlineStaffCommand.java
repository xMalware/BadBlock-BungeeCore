package fr.badblock.bungee.modules.commands.staff;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Online staff
 * 
 * @author xMalware
 *
 */
public class OnlineStaffCommand extends BadCommand {

	private static String	prefix = "bungee.commands.onlinestaff.";
	
	/**
	 * Command constructor
	 */
	public OnlineStaffCommand() {
		super("onlinestaff", "bungee.command.onlinestaff", "os");
	}

	/**
	 * Method called when using the command
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void run(CommandSender sender, String[] args) {
		Set<Permissible> permissibles = BungeeManager.getInstance().getLoggedPlayers(player -> player.hasPermission(getPermission()) &&
				player.getPermissions().getHighestRank("bungee", true) != null)
				.parallelStream().map(player -> player.getPermissions().getHighestRank("bungee", true)).sorted(new Comparator<Permissible>() {
					public int compare(Permissible o1, Permissible o2) {
						return Integer.valueOf(o1.getPower()).compareTo(o2.getPower());
					}
				}).collect(Collectors.toSet());
		permissibles.forEach(permissible -> 
		{
			StringBuilder data = new StringBuilder();
			data.append(I19n.getMessage(sender, prefix + "intro", new int[] { 0 }, permissible.getRawPrefix("chat")));
			List<BadPlayer> usernames = BungeeManager.getInstance().getLoggedPlayers(player -> player.getPermissions().getHighestRank("bungee", true) != null
					&& player.getPermissions().getHighestRank("bungee", false).getName().equalsIgnoreCase(permissible.getName()));
			boolean first = true;
			for (BadPlayer badPlayer : usernames)
			{
				if (!first)
				{
					data.append(I19n.getMessage(sender, prefix + "separator", null));
				}
				else
				{
					first = false;
				}
				data.append(I19n.getMessage(sender, prefix + "player", null, badPlayer.getName(), badPlayer.getLastServer()));
			}
			sender.sendMessage(data.toString());
		});

	}

}