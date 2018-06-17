package fr.badblock.bungee.modules.commands.modo.subcommands;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
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
public class SanctionCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public SanctionCommand() {
		// Super!
		super("sanction", new String[] { "casier", "case", "s", "c", "cases" });
	}

	/**
	 * If a player can view ban history
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canViewHistory(CommandSender sender, String playerName) {
		// If the checker is a player
		boolean checkerPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the checker is a player
		if (checkerPlayer) {
			// Get the checker
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the checker is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the checker permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the checker doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest checker rank
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

			// If the checker has a higher rank than the target player (or a bypass)
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
		if (args.length != 3) {
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

		if (badOfflinePlayer == null) {
			I19n.sendMessage(sender, getPrefix("offline"), null);
			return;
		}

		// If he can't view ban history
		if (!canViewHistory(sender, playerName)) {
			// So we stop there
			return;
		}
		
		I19n.sendMessage(sender, getPrefix("intro"), null, badOfflinePlayer.getName());

		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get database collection
		DBCollection dbCollection = mongoService.getDb().getCollection("players");
		// Create query
		DBObject query = new BasicDBObject();
		// Add punished
		query.put("punished", playerName.toLowerCase());
		// Get data
		DBCursor cursor = dbCollection.find(query);
		// If there's data
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			Punishment punishment = new Punishment(dbObject);
			I19n.sendMessage(sender, getPrefix("history"), new int[] { 1 }, badOfflinePlayer.getName(),
					punishment.getType().name(), punishment.getReason(),
					DateUtils.getHourDate(new Date(punishment.getExpire())), punishment.getPunisher());
		}

		// Send banned message
	}

}