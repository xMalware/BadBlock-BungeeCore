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
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
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
		// If arg length < 3
		if (args.length < 3) {
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

		BadPlayer badPlayer = BadPlayer.get(playerName);

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

		// Get the punisher ip
		String punisherIp = badPlayer != null ? "127.0.0.1" : badPlayer.getLastIp();

		// Generate a unique id
		UUID uuid = UUID.randomUUID();
		
		// Unique id
		String punisherUniqueId = badPlayer != null ? badPlayer.getUniqueId().toString() : null;

		// Create the punishment object
		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getUniqueId().toString(),
				badOfflinePlayer.getLastIp(), PunishType.MUTE, TimeUtils.time(), time, DateUtils.getHourDate(),
				muteReason, false, new String[] {}, sender.getName(), punisherUniqueId, punisherIp);

		// Get the main class
		BadBungee badBungee = BadBungee.getInstance();

		// Get the service
		MongoService mongoService = badBungee.getMongoService();

		// Get the database
		DB db = mongoService.getDb();

		// Get the collection
		DBCollection collection = db.getCollection("punishments");

		// Insert in the collection
		collection.insert(punishment.toObject());

		// If the punish object isn't null
		if (badOfflinePlayer.getPunished() != null) {
			// So set the mute
			badOfflinePlayer.getPunished().setMute(punishment);
		}
		// If the punish object is null
		else {
			// Create a punish object
			badOfflinePlayer.setPunished(new Punished());
			// Set the mute
			badOfflinePlayer.getPunished().setMute(punishment);
		}

		// If the target player is online
		if (badOfflinePlayer.isOnline()) {
			// Get the target player
			BadPlayer targetPlayer = BungeeManager.getInstance().getBadPlayer(badOfflinePlayer.getName());
			// Set mute
			targetPlayer.getPunished().setMute(punishment);
			// Send online update
			targetPlayer.sendOnlineTempSyncUpdate();
			// Try to
			try {
				// Save the data
				targetPlayer.saveData();
			}
			// Error case
			catch (Exception exception) {
				// Print the stack trace
				exception.printStackTrace();
			}
		}
		// If the target player is offline
		else {
			// Try to
			try {
				// Save the data
				badOfflinePlayer.saveData();
			}
			// Error case
			catch (Exception exception) {
				// Print the stacktrace
				exception.printStackTrace();
			}
		}

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatmute"),
				new int[] { 0, 2 }, badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(),
				badOfflinePlayer.getName(), Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.YEAR), muteReason);

		// Send banned message
		I19n.sendMessage(sender, getPrefix("muted"), null, badOfflinePlayer.getName(), rawTime, muteReason);
	}

}