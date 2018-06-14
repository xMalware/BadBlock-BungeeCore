package fr.badblock.bungee.modules.commands.modo.subcommands;

import java.util.UUID;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.bungee.PunishType;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TempBanCommand extends AbstractModCommand {

	public TempBanCommand() {
		super("tempban", new String[] { "tban", "tb" });
	}

	public boolean canBeBanned(CommandSender sender, String playerName) {
		boolean bannerPlayer = sender instanceof ProxiedPlayer;

		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			return false;
		}

		if (badOfflinePlayer.getPunished().isBan()) {
			I19n.sendMessage(sender, getPrefix("alreadybanned"), null, badOfflinePlayer.getName());
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
		if (args.length != 4) {
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

		boolean isKey = true;

		String banReason = args[2];
		String rawTime = args[3];

		long time = Time.MILLIS_SECOND.matchTime(rawTime);

		if (time == 0L) {
			I19n.sendMessage(sender, getPrefix("notgoodtime"), null);
			return;
		}

		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		String punisherIp = !isPlayer ? "127.0.0.1" : badPlayer.getLastIp();

		UUID uuid = UUID.randomUUID();

		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getName(),
				badOfflinePlayer.getLastIp(), PunishType.BAN, TimeUtils.time(), TimeUtils.nextTime(time),
				DateUtils.getHourDate(), banReason, isKey, new String[] {}, sender.getName(), punisherIp);

		BadBungee badBungee = BadBungee.getInstance();

		MongoService mongoService = badBungee.getMongoService();

		DB db = mongoService.getDb();

		DBCollection collection = db.getCollection("punishments");

		collection.insert(punishment.toObject());

		if (badOfflinePlayer.getPunished() != null) {
			badOfflinePlayer.getPunished().setBan(punishment);
		}

		if (badOfflinePlayer.isOnline()) {
			BadPlayer targetPlayer = BungeeManager.getInstance().getBadPlayer(badOfflinePlayer.getName());
			targetPlayer.getPunished().setBan(punishment);
			targetPlayer.sendOnlineTempSyncUpdate();
			try {
				targetPlayer.saveData();
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			targetPlayer.kick(targetPlayer.getBanMessage());
		} else {
			try {
				badOfflinePlayer.saveData();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatban"),
				new int[] { 0, 2 }, badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(),
				rawTime, banReason);

		I19n.sendMessage(sender, getPrefix("banned"), isKey ? new int[] { 1 } : null, badOfflinePlayer.getName(),
				rawTime, banReason);
	}

}