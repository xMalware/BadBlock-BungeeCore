package fr.badblock.bungee.modules.commands.modo.punishments;

import java.util.UUID;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.bungee.PunishType;
import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.objects.ModoSession;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PunishmentWorkerWarn extends PunishmentWorker
{

	@Override
	public PunishmentType getType()
	{
		return PunishmentType.WARN;
	}

	@Override
	public void process(CommandSender sender, String playerName, String reason, boolean isKey, long time)
	{
		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isFound() || !badOfflinePlayer.isLoaded())
		{
			if (sender != null)
			{
				I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			}
			return;
		}

		String byPlayer = "Console";
		String punisherIp = "127.0.0.1";
		String punisherUniqueId = null;

		String rawPrefix = "";
		String rawSuffix = "";

		BadPlayer badPlayer = null;

		if (sender != null && sender instanceof ProxiedPlayer)
		{
			ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
			badPlayer = BadPlayer.get(proxiedPlayer);
			byPlayer = badPlayer.getName();
			punisherIp = badPlayer.getLastIp();
			punisherUniqueId = badPlayer.getUniqueId().toString();
			rawPrefix = badPlayer.getRawChatPrefix();
			rawSuffix = badPlayer.getRawChatSuffix();
		}

		// Generate a unique id
		UUID uuid = UUID.randomUUID();

		// Create the punishment object
		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getUniqueId().toString(),
				badOfflinePlayer.getLastIp(), PunishType.WARN, TimeUtils.time(), -1, DateUtils.getHourDate(),
				reason, false, new String[] {}, byPlayer, punisherUniqueId, punisherIp);

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
		if (badOfflinePlayer.isOnline()) {
			// Send warn message
			badOfflinePlayer.getOnlineBadPlayer().warn(reason);
		}
		// If the player is offline
		else {
			// If the punished object is null
			if (badOfflinePlayer.getPunished() == null) {
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
			catch (Exception exception) {
				// Print stack trace
				exception.printStackTrace();
			}
		}

		// Array to translate
		int[] arr = null;

		if (badPlayer == null)
		{
			arr = isKey ? new int[] { 5 } : null;
		}
		else
		{
			arr = isKey ? new int[] { 0, 2, 5 } : new int[] { 0, 2 };
		}

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatwarn"),
				arr, rawPrefix, byPlayer, rawSuffix, badOfflinePlayer.getName(), reason);

		if (badPlayer != null)
		{
			ModoSession modoSession = badPlayer.getModoSession();

			if (modoSession != null)
			{
				modoSession.incrementPunishment();
			}
		}
	}

}
