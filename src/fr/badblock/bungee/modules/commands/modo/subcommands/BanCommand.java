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
import fr.badblock.bungee.modules.commands.modo.objects.BanReason;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanCommand extends AbstractModCommand {

	public BanCommand() {
		super("ban", new String[] { "b" });
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length != 2 && args.length != 3) {
			// /m ban <pseudo>
			I19n.sendMessage(sender, getPrefix("usage"), null);
			return;
		}

		String playerName = args[1];

		if (!canBeBanned(sender, playerName))
		{
			return;
		}

		boolean isPlayer = sender instanceof ProxiedPlayer;
		ProxiedPlayer proxiedPlayer = isPlayer ? (ProxiedPlayer) sender : null;
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		isPlayer = isPlayer && proxiedPlayer != null && badPlayer != null;

		if (args.length == 2) {
			I19n.sendMessage(sender, getPrefix("intro"), null);

			if (isPlayer)
			{
				badPlayer.sendTranslatedOutgoingMessage(getPrefix("select_intro"), null, playerName);
			}
			else
			{
				I19n.sendMessage(sender, getPrefix("select_intro"), null, playerName);
			}

			boolean hasReason = false;

			for (BanReason banReason : BanReason.values())
			{
				if (!sender.hasPermission(getPermission() + "." + banReason.getName()))
				{
					continue;
				}

				hasReason = true;

				if (isPlayer)
				{
					// Get the intro message
					String intro = badPlayer.getTranslatedMessage(getPrefix("reason_intro"), null);
					// Get the reason message
					String reason = badPlayer.getTranslatedMessage(getPrefix("reason." + banReason.getName()), null);

					// Get the McJson
					McJson json = new McJsonFactory(intro).finaliseComponent().initNewComponent(reason).setHoverText(reason)
							.setClickCommand("/m ban " + playerName + " " + banReason.getName()).finaliseComponent().build();

					// Send the message
					badPlayer.sendTranslatedOutgoingMCJson(json);
				}
				else
				{
					I19n.sendMessage(sender, getPrefix("reason." + banReason.getName()), null);
				}
			}

			if (!hasReason)
			{
				I19n.sendMessage(sender, getPrefix("noreason"), null);
			}

			return;			
		}

		boolean isKey = true;
		String rawBanReason = args[2];

		BanReason banReason = BanReason.getFromString(rawBanReason);

		if (banReason == null)
		{
			if (!sender.hasPermission(getPermission() + ".custom"))
			{
				I19n.sendMessage(sender, getPrefix("unknownreason"), null);
				return;
			}

			isKey = false;
		}

		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		String reason = isKey ? getPrefix("reason." + banReason.getName()) : rawBanReason;
		String punisherIp = !isPlayer ? "127.0.0.1" : badPlayer.getLastIp();

		UUID uuid = UUID.randomUUID();

		Punishment punishment = new Punishment(uuid.toString(), badOfflinePlayer.getName(), badOfflinePlayer.getLastIp(), PunishType.BAN,
				TimeUtils.time(), -1L, DateUtils.getHourDate(), reason, isKey, new String[] {}, sender.getName(), punisherIp);

		BadBungee badBungee = BadBungee.getInstance();

		MongoService mongoService = badBungee.getMongoService();

		DB db = mongoService.getDb();

		DBCollection collection = db.getCollection("punishments");

		collection.insert(punishment.toObject());

		Punished punished = badOfflinePlayer.getPunished();

		if (punished != null)
		{
			punished.setBan(punishment);
		}

		if (badOfflinePlayer.isOnline())
		{
			BadPlayer targetPlayer = BungeeManager.getInstance().getBadPlayer(badOfflinePlayer.getName());
			targetPlayer.sendOnlineTempSyncUpdate();
			try
			{
				targetPlayer.saveData();
			}
			catch (Exception exception)
			{	
				exception.printStackTrace();
			}
		}
		else
		{
			try
			{
				badOfflinePlayer.saveData();
			}
			catch (Exception exception)
			{	
				exception.printStackTrace();
			}
		}
	}

	public boolean canBeBanned(CommandSender sender, String playerName)
	{
		boolean bannerPlayer = sender instanceof ProxiedPlayer;

		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound())
		{
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			return false;
		}

		if (badOfflinePlayer.getPunished().isBan())
		{
			I19n.sendMessage(sender, getPrefix("alreadybanned"), null, badOfflinePlayer.getName());
			return false;
		}

		if (bannerPlayer)
		{
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			if (badPlayer == null)
			{
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				return false;
			}

			PermissionUser perm = badPlayer.getPermissions();

			if (perm == null)
			{
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				return false;
			}

			Permissible permissible = perm.getHighestRank("bungee", false);

			if (permissible == null)
			{
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 3);
				return false;
			}

			PermissionUser targetPerm = badOfflinePlayer.getPermissions();

			if (targetPerm == null)
			{
				return true;
			}

			Permissible	targetPermissible = targetPerm.getHighestRank("bungee", false);

			if (targetPermissible == null)
			{
				return true;
			}

			if (permissible.getPower() > targetPermissible.getPower() ||
					badPlayer.hasPermission(getPermission() + ".bypasspower"))
			{
				return true;
			}

			I19n.sendMessage(sender, getPrefix("notenoughpermissions"), null, badOfflinePlayer.getName());
			return false;
		}
		else
		{
			return true;
		}
	}

}