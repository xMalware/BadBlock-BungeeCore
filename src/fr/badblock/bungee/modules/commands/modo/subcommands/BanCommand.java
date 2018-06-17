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
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.modules.commands.modo.objects.BanReasonType;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * BadCommand
 * 
 * @author xMalware
 *
 */
public class BanCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public BanCommand() {
		// Super!
		super("ban", new String[] { "b" });
	}

	/**
	 * If a player can be banned
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canBeBanned(CommandSender sender, String playerName) {
		// If the banner is a player
		boolean bannerPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the player is already banned
		if (badOfflinePlayer.getPunished().isBan()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("alreadybanned"), null, badOfflinePlayer.getName());
			// So we stop there
			return false;
		}

		// If the banner is a player
		if (bannerPlayer) {
			// Get the banner
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the banner is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the banner permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the banner doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest banner rank
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

			// If the banner has a higher rank than the target player (or a bypass)
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
		// If arg length != 2 or 3
		if (args.length != 2 && args.length != 3) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		// If he can't ban
		if (!canBeBanned(sender, playerName)) {
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

		// two args
		if (args.length == 2) {

			// If the sender is a player
			if (isPlayer) {
				// Send the message
				badPlayer.sendTranslatedOutgoingMessage(getPrefix("select_intro"), null, playerName);
			} else
			// If the sender isn't a player
			{
				// Send the message
				I19n.sendMessage(sender, getPrefix("select_intro"), null, playerName);
			}

			// Has reason
			boolean hasReason = false;

			// For each reason type
			for (BanReasonType banReason : BanReasonType.values()) {
				// If the sender doesn't have the permission to ban for this reason
				if (!sender.hasPermission(getPermission() + "." + banReason.getName())) {
					// So continue
					continue;
				}

				// One reason!
				hasReason = true;

				// If the sender is a player
				if (isPlayer) {
					// Get the intro message
					String intro = badPlayer.getTranslatedMessage(getPrefix("reason_intro"), null);
					// Get the reason message
					String reason = badPlayer.getTranslatedMessage(getPrefix("reason." + banReason.getName()), null);

					// Get the McJson
					McJson json = new McJsonFactory(intro).finaliseComponent().initNewComponent(reason)
							.setHoverText(reason).setClickCommand("/m ban " + playerName + " " + banReason.getName())
							.finaliseComponent().build();

					// Send the message
					badPlayer.sendTranslatedOutgoingMCJson(json);
				} else
				// If the sender isn't a player
				{
					// Send the reason message
					I19n.sendMessage(sender, getPrefix("reason." + banReason.getName()), null);
				}
			}

			// If we don't have reasons
			if (!hasReason) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("noreason"), null);
			}

			// So we stop there
			return;
		}

		// If the sender is a player
		if (isPlayer && badPlayer.getFlags().has("tryban")) {
			return;
		}

		// Set the flag to avoid to duplicate bans
		badPlayer.getFlags().set("tryban", 500);

		// Is this a key
		boolean isKey = true;

		// Get the raw ban reason
		String rawBanReason = args[2];

		// Get the ban reason
		BanReasonType banReason = BanReasonType.getFromString(rawBanReason);

		// If the ban reason doesn't exist
		if (banReason == null) {
			// If he doesn't have the permission
			if (!sender.hasPermission(getPermission() + ".custom")) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("unknownreason"), null);
				// So we stop there
				return;
			}

			// Not a key, just a message
			isKey = false;
		}

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		// Get the reason
		String reason = isKey ? getPrefix("reason." + banReason.getName()) : rawBanReason;
		// Get the punisher ip
		String punisherIp = !isPlayer ? "127.0.0.1" : badPlayer.getLastIp();

		// Generate a unique id
		UUID uuid = UUID.randomUUID();
		
		// Unique id
		String punisherUniqueId = isPlayer ? badPlayer.getUniqueId().toString() : null;

		// Create the punishment object
		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getUniqueId().toString(),
				badOfflinePlayer.getLastIp(), PunishType.BAN, TimeUtils.time(),
				TimeUtils.nextTime(Time.YEAR.convert(1L, Time.MILLIS_SECOND)), DateUtils.getHourDate(), reason, isKey,
				new String[] {}, sender.getName(), punisherUniqueId, punisherIp);

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
			// So set the ban
			badOfflinePlayer.getPunished().setBan(punishment);
		}
		// If the punish object is null
		else {
			// Create a punish object
			badOfflinePlayer.setPunished(new Punished());
			// Set the ban
			badOfflinePlayer.getPunished().setBan(punishment);
		}

		// If the target player is online
		if (badOfflinePlayer.isOnline()) {
			// Get the target player
			BadPlayer targetPlayer = BungeeManager.getInstance().getBadPlayer(badOfflinePlayer.getName());
			// Set ban
			targetPlayer.getPunished().setBan(punishment);
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

			// Kick the player
			targetPlayer.kick(targetPlayer.getBanMessage());
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

		// Array to translate
		int[] arr = isKey ? new int[] { 0, 2, 4 } : new int[] { 0, 2 };
		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatban"),
				arr, badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(),
				badOfflinePlayer.getName(), reason);

		// Send banned message
		I19n.sendMessage(sender, getPrefix("banned"), isKey ? new int[] { 1 } : null, badOfflinePlayer.getName(),
				reason);
	}

}