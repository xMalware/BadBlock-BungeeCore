package fr.badblock.bungee.modules.commands.modo.subcommands;

import java.util.Map.Entry;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

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
import fr.badblock.bungee.modules.commands.modo.objects.BanIndex;
import fr.badblock.bungee.modules.commands.modo.objects.BanReason;
import fr.badblock.bungee.modules.commands.modo.objects.BanReasons;
import fr.badblock.bungee.players.BadIP;
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
public class BanIpCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public BanIpCommand() {
		// Super!
		super("banip", new String[] { "bi", "bip" });
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

		System.out.println("1");
		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
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
		if (args.length < 2) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		System.out.println("0");
		// If he can't ban
		if (!canBeBanned(sender, playerName)) {
			// So we stop there
			return;
		}
		System.out.println("-");

		// He's a player?
		boolean isPlayer = sender instanceof ProxiedPlayer;
		// Get the proxied player
		ProxiedPlayer proxiedPlayer = isPlayer ? (ProxiedPlayer) sender : null;
		// Get the bad player
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		// He's a player?
		isPlayer = isPlayer && proxiedPlayer != null && badPlayer != null;

		// two args
		if (args.length <= 2) {

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
			for (Entry<String, BanReason> entry : BanReasons.getInstance().getBanReasons().entrySet()) {
				// If the sender doesn't have the permission to ban for this reason
				if (!sender.hasPermission(getPermission() + "." + entry.getKey())) {
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
					String reason = badPlayer.getTranslatedMessage(getPrefix("reason." + entry.getKey()), null);

					// Get the McJson
					McJson json = new McJsonFactory(intro).finaliseComponent().initNewComponent(reason)
							.setHoverText(reason).setClickCommand("/m banip " + playerName + " " + entry.getKey())
							.finaliseComponent().build();

					// Send the message
					badPlayer.sendTranslatedOutgoingMCJson(json);
				} else
				// If the sender isn't a player
				{
					// Send the reason message
					I19n.sendMessage(sender, getPrefix("reason." + entry.getKey()), null);
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
		String rawBanReason = StringUtils.join(args, " ", 2);

		// Get the ban reason
		BanReason banReason = BanReasons.getInstance().getBanReasons().get(rawBanReason);

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
		else
		{
			if (!sender.hasPermission(getPermission() + "." + rawBanReason))
			{
				I19n.sendMessage(sender, getPrefix("notenoughpermissions"), null, playerName);
				// So we stop there
				return;
			}
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

		// Get the reason
		String reason = isKey ? getPrefix("reason." + banReason.getName()) : rawBanReason;
		// Get the punisher ip
		String punisherIp = !isPlayer ? "127.0.0.1" : badPlayer.getLastIp();

		// Generate a unique id
		UUID uuid = UUID.randomUUID();

		// Unique id
		String punisherUniqueId = isPlayer ? badPlayer.getUniqueId().toString() : null;

		long time = Time.YEAR.convert(1L, Time.MILLIS_SECOND);

		if (banReason != null && isKey) {

			// Get mongo service
			MongoService mongoService = BadBungee.getInstance().getMongoService();
			// Get database collection
			DBCollection dbCollection = mongoService.getDb().getCollection("punishments");
			// Create query
			BasicDBObject query = new BasicDBObject();
			// Add punished uuid
			query.put("punishedUuid", badOfflinePlayer.getUniqueId().toString().toLowerCase());
			// Add punished ip
			query.put("punishedIp", badIp.getIp());
			// Add type
			query.put("type", PunishType.BAN.name());
			// Add reason
			query.put("reason", reason);
			// Get data
			DBCursor cursor = dbCollection.find(query);

			int ban = cursor.count();

			if (banReason != null) {
				BanIndex index = null;
				for (BanIndex banIndex : banReason.getPunishments().values()) {
					if (index == null || (banIndex.getIndex() > index.getIndex() && banIndex.getIndex() >= ban)) {
						index = banIndex;
					}
				}

				if (index != null) {
					String rawTime = index.getTime();
					time = Time.MILLIS_SECOND.matchTime(rawTime);
					if (time == 0L) {
						I19n.sendMessage(sender, getPrefix("unknowntime"), null, rawTime);
						return;
					}
				} else {
					I19n.sendMessage(sender, getPrefix("unknownreason"), null, playerName);
					return;
				}
			}
		}

		// Create the punishment object
		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getUniqueId().toString(),
				badOfflinePlayer.getLastIp(), PunishType.BAN, TimeUtils.time(), TimeUtils.nextTime(time),
				DateUtils.getHourDate(), reason, isKey, new String[] {}, sender.getName(), punisherUniqueId,
				punisherIp);

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

		// Array to translate
		int[] arr = isKey ? new int[] { 0, 2, 5 } : new int[] { 0, 2 };
		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatban"), arr,
				badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(),
				badOfflinePlayer.getName(), Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.YEAR), reason);

		// Send banned message
		I19n.sendMessage(sender, getPrefix("banned"), isKey ? new int[] { 2 } : null, badOfflinePlayer.getName(),
				Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.YEAR), reason);
	}

}