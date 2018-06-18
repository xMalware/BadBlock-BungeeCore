package fr.badblock.bungee.modules.commands.modo.subcommands;

import java.util.UUID;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.bungee.PunishType;
import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
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
public class UnmuteCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public UnmuteCommand() {
		// Super!
		super("unmute", new String[] { "um" });
	}

	/**
	 * If a player can be unmuted
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canBeUnmuted(CommandSender sender, String playerName) {
		// If the unmuter is a player
		boolean unmuterPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the player is not muted
		if (!badOfflinePlayer.getPunished().isMute()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("notmuted"), null, badOfflinePlayer.getName());
			// So we stop there
			return false;
		}

		// If the unmuter is a player
		if (unmuterPlayer) {
			// Get the unmuter
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the unmuter is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the unmuter permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the unmuter doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest unmuter rank
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

			// If the unmuter has a higher rank than the target player (or a bypass)
			if (permissible.getPower() > targetPermissible.getPower()
					|| badPlayer.hasPermission(getPermission() + ".bypasspower")) {
				// So we stop there
				return true;
			}

			Punishment punishment = badOfflinePlayer.getPunished().getMute();

			if (punishment == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("notmuted"), null, badOfflinePlayer.getName());
				// So we stop there
				return false;
			}

			String punisher = badOfflinePlayer.getPunished().getMute().getPunisher();

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

			// Get the highest muter rank
			Permissible punisherPermissible = perm.getHighestRank("bungee", false);

			// If the permissible is null
			if (punisherPermissible == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 3);
				// So we stop there
				return false;
			}

			// If the muter has a higher rank than the player
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

		// If he can't be unmuted
		if (!canBeUnmuted(sender, playerName)) {
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

		// Get the punisher ip
		String punisherIp = !isPlayer ? "127.0.0.1" : badPlayer.getLastIp();

		// Generate a unique id
		UUID uuid = UUID.randomUUID();

		// Unique id
		String punisherUniqueId = isPlayer ? badPlayer.getUniqueId().toString() : null;

		// Create the punishment object
		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getUniqueId().toString(),
				badOfflinePlayer.getLastIp(), PunishType.UNMUTE, TimeUtils.time(), -1, DateUtils.getHourDate(), "",
				false, new String[] {}, sender.getName(), punisherUniqueId, punisherIp);

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
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatunmute"),
				new int[] { 0, 2 }, badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(),
				badOfflinePlayer.getName());

		// Send banned message
		I19n.sendMessage(sender, getPrefix("unmuted"), null, badOfflinePlayer.getName());
	}

}