package fr.badblock.bungee.modules.commands.basic.report;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
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
public class ReportCommand extends BadCommand {

	/**
	 * Constructor
	 */
	public ReportCommand() {
		// Super!
		super("report", "", "rp");
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

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isFound() || !badOfflinePlayer.isLoaded())
		{
			// Send the message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
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

			// For each type
			for (ReportType reportType : ReportType.values()) {
				// If the sender doesn't have the permission
				if (!sender.hasPermission(getPermission() + "." + reportType.getName())) {
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
					String reason = badPlayer.getTranslatedMessage(getPrefix("reason." + reportType.getName()), null);

					// Get the McJson
					McJson json = new McJsonFactory(intro).finaliseComponent().initNewComponent(reason)
							.setHoverText(reason).setClickCommand("/report " + playerName + " " + reportType.getName())
							.finaliseComponent().build();

					// Send the message
					badPlayer.sendTranslatedOutgoingMCJson(json);
				} else
					// If the sender isn't a player
				{
					// Send the reason message
					I19n.sendMessage(sender, getPrefix("reason." + reportType.getName()), null);
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
		if (isPlayer && badPlayer.getFlags().has("report")) {
			return;
		}

		// Set the flag to avoid to spam reports
		badPlayer.getFlags().set("report", 60_000);

		// Get the raw ban type
		String rawReportType = args[2];

		// Get the ban reason
		ReportType reportType = ReportType.getFromString(rawReportType);

		// If the ban reason doesn't exist
		if (reportType == null) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknowntype"), null);
			// So we stop there
			return;
		}

		// Get the reason
		String reason = getPrefix("reason." + reportType.getName());

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.command.report.receivereports", getPrefix("reportmessage"), new int[] { 0, 2 }, 
				badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(), reason);
		
		// Send message
		I19n.sendMessage(sender, getPrefix("reported"), null, badOfflinePlayer.getName(), reason);
	}

	public String getPrefix(String string)
	{
		return "bungee.commands.report." + string;
	}

}