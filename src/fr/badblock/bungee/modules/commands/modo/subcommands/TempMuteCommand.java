package fr.badblock.bungee.modules.commands.modo.subcommands;

import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.modules.commands.modo.punishments.PunishmentType;
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
public class TempMuteCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public TempMuteCommand() {
		// Super!
		super("tempmute", new String[] { "tm" });
	}

	/**
	 * If a player can be muted
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canBeMuted(CommandSender sender, String playerName) {
		// If the muter is a player
		boolean muterPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the player is already muted
		if (badOfflinePlayer.getPunished().isMute()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("alreadymuted"), null, badOfflinePlayer.getName());
			// So we stop there
			return false;
		}

		// If the muter is a player
		if (muterPlayer) {
			// Get the muter
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the muter is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the muter permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the muter doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest muter rank
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

			// If the muter has a higher rank than the target player (or a bypass)
			if (permissible.getPower() > targetPermissible.getPower()
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
		// If arg length < 4
		if (args.length < 4) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		// If he can't be muted
		if (!canBeMuted(sender, playerName)) {
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

		// If the sender is a player
		if (badPlayer != null && badPlayer.getFlags().has("trymute")) {
			return;
		}

		// Set the flag to avoid to duplicate mutes
		badPlayer.getFlags().set("trymute", 500);

		// Get the mute reason
		String rawTime = args[2];

		String muteReason = StringUtils.join(args, " ", 3);

		long time = Time.MILLIS_SECOND.matchTime(rawTime);

		if (time == 0L) {
			I19n.sendMessage(sender, getPrefix("notgoodtime"), null, rawTime);
			return;
		}

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		PunishmentType.MUTE.process(sender, playerName, muteReason, false, time);

		// Send banned message
		I19n.sendMessage(sender, getPrefix("muted"), null, badOfflinePlayer.getName(), rawTime, muteReason);
	}

}