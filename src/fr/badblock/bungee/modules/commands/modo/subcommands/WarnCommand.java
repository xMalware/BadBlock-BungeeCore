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
public class WarnCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public WarnCommand() {
		// Super!
		super("warn", new String[] { "w" });
	}

	/**
	 * If a player can be warn
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canBeWarn(CommandSender sender, String playerName) {
		// If the kicker is a player
		boolean kickPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the kicker is a player
		if (kickPlayer) {
			// Get the kicker
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the kicker is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the kicker permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the kicker doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest kicker rank
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

			// If the kicker has a higher rank than the target player (or a bypass)
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
		// If arg length != 3
		if (args.length < 3) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		BungeeManager bungeeManager = BungeeManager.getInstance();

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = bungeeManager.getBadOfflinePlayer(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isFound() || !badOfflinePlayer.isLoaded()) {
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			return;
		}

		// If he can't be warn
		if (!canBeWarn(sender, playerName)) {
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

		// Get the warn reason
		String warnReason = StringUtils.join(args, " ", 2);

		// Get the punisher ip
		String punisherIp = !isPlayer ? "127.0.0.1" : badPlayer.getLastIp();

		// Generate a unique id
		UUID uuid = UUID.randomUUID();
		
		// Unique id
		String punisherUniqueId = isPlayer ? badPlayer.getUniqueId().toString() : null;

		// Create the punishment object
		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getUniqueId().toString(), badOfflinePlayer.getLastIp(),
				PunishType.WARN, TimeUtils.time(), -1, DateUtils.getHourDate(), warnReason, false, new String[] {},
				sender.getName(), punisherUniqueId, punisherIp);

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

		// If the player is online
		if (badOfflinePlayer.isOnline())
		{
			// Send warn message
			badPlayer.getOnlineBadPlayer().warn(warnReason);
		}
		// If the player is offline
		else
		{
			// If the punished object is null
			if (badOfflinePlayer.getPunished() == null)
			{
				// Create a punished object
				badOfflinePlayer.setPunished(new Punished());
			}

			// Set warn
			badOfflinePlayer.getPunished().setWarn(punishment);
			
			// Try to
			try {
				// Save data
				badOfflinePlayer.saveData();
			}
			// Error case
			catch (Exception exception)
			{
				// Print stack trace
				exception.printStackTrace();
			}
		}

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatwarn"),
				new int[] { 0, 2 }, badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(),
				badOfflinePlayer.getName(), warnReason);

		// Send banned message
		I19n.sendMessage(sender, getPrefix("warned"), null, badOfflinePlayer.getName(), warnReason);
	}

}