package fr.badblock.bungee.modules.commands.modo.subcommands;

import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.modules.commands.modo.punishments.PunishmentType;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * BadCommand
 * 
 * @author xMalware
 *
 */
public class UnbanIPCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public UnbanIPCommand() {
		// Super!
		super("unbanip", new String[] { "ub", "ubi", "ubip" });
	}

	/**
	 * If a player can be unbanned
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canBeUnbanned(CommandSender sender, String playerName) {
		// If the unbanner is a player
		boolean unbannerPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		BadIP badIp = BadIP.get(badOfflinePlayer.getLastIp());

		if (badIp == null || !badIp.isFound() || !badIp.isLoaded()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		Punished punished = badIp.getPunished();

		if (punished == null) {
			punished = new Punished();
		}

		// If the player is not banned
		if (!punished.isBan()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("notbanned"), null, badOfflinePlayer.getName());
			// So we stop there
			return false;
		}

		// If the unbanner is a player
		if (unbannerPlayer) {
			// Get the unbanner
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the unbanner is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the unbanner permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the unbanner doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest unbanner rank
			Permissible permissible = perm.getHighestRank("bungee", false);

			// If the permissible is null
			if (permissible == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 3);
				// So we stop there
				return false;
			}

			// Get the target permissions
			PermissionUser targetPerm = badOfflinePlayer.getPermissions();

			// If the target player doesn't have any permissions
			if (targetPerm == null) {
				// So we stop there
				return true;
			}

			// Get the highest target rank
			Permissible targetPermissible = targetPerm.getHighestRank("bungee", false);

			// If the permissible is null
			if (targetPermissible == null) {
				// So we stop there
				return true;
			}

			// If the unbanner has a higher rank than the target player (or a bypass)
			if (permissible.getPower() > targetPermissible.getPower()
					|| badPlayer.hasPermission(getPermission() + ".bypasspower")) {
				// So we stop there
				return true;
			}

			Punishment punishment = punished.getMute();

			if (punishment == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("notbanned"), null, badOfflinePlayer.getName());
				// So we stop there
				return false;
			}

			String punisher = punished.getBan().getPunisher();

			// Get the punisher player
			BadOfflinePlayer punisherPlayer = BungeeManager.getInstance().getBadOfflinePlayer(punisher);

			// Get the muter permissions
			PermissionUser punisherPerm = punisherPlayer.getPermissions();

			// If the muter doesn't have any permissions
			if (punisherPerm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest banner rank
			Permissible punisherPermissible = perm.getHighestRank("bungee", false);

			// If the permissible is null
			if (punisherPermissible == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 3);
				// So we stop there
				return false;
			}

			// If the banner has a higher rank than the player
			if (permissible.getPower() > punisherPermissible.getPower()
					|| badPlayer.hasPermission(getPermission() + ".bypasspower")) {
				// So we stop there
				return true;
			}

			// Not enough permission message
			I19n.sendMessage(sender, getPrefix("notenoughpermissions"), null, badOfflinePlayer.getName());
			// Returns false
			return false;
		} else {
			// Returns true
			return true;
		}
	}

	/**
	 * Run
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If arg length != 2
		if (args.length != 2) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		// If he can't be unbanned
		if (!canBeUnbanned(sender, playerName)) {
			// So we stop there
			return;
		}

		// He's a player?
		boolean isPlayer = sender instanceof ProxiedPlayer;
		// Get the proxied player
		ProxiedPlayer proxiedPlayer = isPlayer ? (ProxiedPlayer) sender : null;
		// Get the bad player
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		// He's a player?
		isPlayer = isPlayer && proxiedPlayer != null && badPlayer != null;

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return;
		}

		BadIP badIp = BadIP.get(badOfflinePlayer.getLastIp());

		if (badIp == null || !badIp.isLoaded() || !badIp.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return;
		}

		PunishmentType.UNBANIP.process(sender, playerName, null, false, -1);

		// Send banned message
		I19n.sendMessage(sender, getPrefix("unbanned"), null, badOfflinePlayer.getName());
	}

}