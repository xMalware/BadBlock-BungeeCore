package fr.badblock.bungee.modules.commands.modo.subcommands;

import java.util.UUID;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.bungee.PunishType;
import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.modules.commands.modo.objects.ModoSession;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
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

		System.out.println("2");

		if (badIp == null) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		System.out.println("3");

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

		System.out.println("A : " + badOfflinePlayer.getLastIp());
		if (badIp == null) {
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			return;
		}
		System.out.println("B : " + badOfflinePlayer.getLastIp());

		String punisherIp = !isPlayer ? "127.0.0.1" : badPlayer.getLastIp();

		UUID uuid = UUID.randomUUID();

		// Unique id
		String punisherUniqueId = isPlayer ? badPlayer.getUniqueId().toString() : null;

		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getUniqueId().toString(),
				badOfflinePlayer.getLastIp(), PunishType.BAN, TimeUtils.time(), TimeUtils.nextTime(time),
				DateUtils.getHourDate(), banReason, false, new String[] {}, sender.getName(), punisherUniqueId,
				punisherIp);

		BadBungee badBungee = BadBungee.getInstance();

		MongoService mongoService = badBungee.getMongoService();

		DB db = mongoService.getDb();

		DBCollection collection = db.getCollection("punishments");

		collection.insert(punishment.toObject());

		// If the punish object isn't null
		if (badIp.getPunished() != null) {
			// So set the ban
			badIp.getPunished().setBan(punishment);
		}
		// If the punish object is null
		else {
			// Create a punish object
			badIp.setPunished(new Punished());
			// Set the ban
			badIp.getPunished().setBan(punishment);
		}

		// Try to
		try {
			// Save the data
			badIp.saveData();
		}
		// Error case
		catch (Exception exception) {
			// Print the stacktrace
			exception.printStackTrace();
		}

		// If the target player is online
		if (badOfflinePlayer.isOnline()) {
			// Get the target player
			BadPlayer targetPlayer = BungeeManager.getInstance().getBadPlayer(badOfflinePlayer.getName());

			// Kick the player
			targetPlayer.kick(targetPlayer.getBanMessage());
		}

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatban"),
				new int[] { 0, 2 }, badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(),
				badOfflinePlayer.getName(), Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.YEAR), banReason);

		ModoSession modoSession = badPlayer.getModoSession();

		if (modoSession != null)
		{
			modoSession.incrementPunishment();
		}

		I19n.sendMessage(sender, getPrefix("banned"), null, badOfflinePlayer.getName(),
				Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.YEAR), banReason);
	}

}