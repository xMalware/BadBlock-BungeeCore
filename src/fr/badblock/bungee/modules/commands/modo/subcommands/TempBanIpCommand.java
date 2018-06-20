package fr.badblock.bungee.modules.commands.modo.subcommands;

import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.modules.commands.modo.punishments.PunishmentType;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TempBanIpCommand extends AbstractModCommand {

	public TempBanIpCommand() {
		super("tempbanip", new String[] { "tbip", "tbanip", "tbi" });
	}

	public boolean canBeBanned(CommandSender sender, String playerName) {
		boolean bannerPlayer = sender instanceof ProxiedPlayer;

		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			return false;
		}

		BadIP badIp = BadIP.get(badOfflinePlayer.getLastIp());

		if (badIp == null) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the player is already banned
		if (badIp.getPunished() != null && badIp.getPunished().isBan()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("alreadybanned"), null, badOfflinePlayer.getName());
			// So we stop there
			return false;
		}

		if (bannerPlayer) {
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			if (badPlayer == null) {
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				return false;
			}

			PermissionUser perm = badPlayer.getPermissions();

			if (perm == null) {
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				return false;
			}

			Permissible permissible = perm.getHighestRank("bungee", false);

			if (permissible == null) {
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 3);
				return false;
			}

			PermissionUser targetPerm = badOfflinePlayer.getPermissions();

			if (targetPerm == null) {
				return true;
			}

			Permissible targetPermissible = targetPerm.getHighestRank("bungee", false);

			if (targetPermissible == null) {
				return true;
			}

			if (permissible.getPower() > targetPermissible.getPower()
					|| badPlayer.hasPermission(getPermission() + ".bypasspower")) {
				return true;
			}

			I19n.sendMessage(sender, getPrefix("notenoughpermissions"), null, badOfflinePlayer.getName());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length < 4) {
			// /m ban <pseudo> <temps> <raison>
			I19n.sendMessage(sender, getPrefix("usage"), null);
			return;
		}

		String playerName = args[1];

		if (!canBeBanned(sender, playerName)) {
			return;
		}

		boolean isPlayer = sender instanceof ProxiedPlayer;
		ProxiedPlayer proxiedPlayer = isPlayer ? (ProxiedPlayer) sender : null;
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		isPlayer = isPlayer && proxiedPlayer != null && badPlayer != null;

		if (isPlayer && badPlayer.getFlags().has("tryban")) {
			return;
		}

		badPlayer.getFlags().set("tryban", 500);

		String rawTime = args[2];
		String banReason = StringUtils.join(args, " ", 3);

		long time = Time.MILLIS_SECOND.matchTime(rawTime);

		if (time == 0L) {
			I19n.sendMessage(sender, getPrefix("notgoodtime"), null, rawTime);
			return;
		}

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		if (badOfflinePlayer == null) {
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			return;
		}

		BadIP badIp = BadIP.get(badOfflinePlayer.getLastIp());

		if (badIp == null) {
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			return;
		}

		PunishmentType.BANIP.process(sender, playerName, banReason, false, time);

		I19n.sendMessage(sender, getPrefix("banned"), null, badOfflinePlayer.getName(),
				Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.YEAR), banReason);
	}

}