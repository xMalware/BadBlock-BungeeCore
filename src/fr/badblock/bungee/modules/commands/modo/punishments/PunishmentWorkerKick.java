package fr.badblock.bungee.modules.commands.modo.punishments;

import java.util.UUID;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.bungee.PunishType;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.objects.ModoSession;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PunishmentWorkerKick extends PunishmentWorker {

	@Override
	public PunishmentType getType() {
		return PunishmentType.KICK;
	}

	@Override
	public void process(CommandSender sender, String playerName, String reason, boolean isKey, long time) {
		BungeeManager bungeeManager = BungeeManager.getInstance();

		if (!bungeeManager.hasUsername(playerName)) {
			if (sender != null) {
				I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			}
			return;
		}

		// Get the online target player
		BadPlayer badOnlinePlayer = BungeeManager.getInstance().getBadPlayer(playerName);

		if (badOnlinePlayer == null || !badOnlinePlayer.isFound() || !badOnlinePlayer.isLoaded()) {
			if (sender != null) {
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

		if (sender != null && sender instanceof ProxiedPlayer) {
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
		Punishment punishment = new Punishment(uuid.toString(), badOnlinePlayer.getUniqueId().toString(),
				badOnlinePlayer.getLastIp(), PunishType.KICK, TimeUtils.time(), -1, DateUtils.getHourDate(), reason,
				isKey, new String[] {}, byPlayer, punisherUniqueId, punisherIp);

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

		// Kick the player
		badOnlinePlayer.kick(badOnlinePlayer.getKickMessage(reason));

		// Array to translate
		int[] arr = null;

		if (badPlayer != null) {
			arr = isKey ? new int[] { 0, 2, 5 } : new int[] { 0, 2 };
		} else {
			arr = isKey ? new int[] { 5 } : null;
		}

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast(getPermission(), getPrefix("staffchatkick"), arr,
				rawPrefix, byPlayer, rawSuffix, badOnlinePlayer.getName(), reason);

		if (badPlayer != null) {
			ModoSession modoSession = badPlayer.getModoSession();

			if (modoSession != null) {
				modoSession.incrementPunishment();
			}
		}
	}

}
