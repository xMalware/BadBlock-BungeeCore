package fr.badblock.bungee.modules.commands.modo.subcommands;

import fr.badblock.api.common.minecraft.party.Party;
import fr.badblock.api.common.minecraft.party.Partyable;
import fr.badblock.api.common.utils.data.Callback;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.basic.friends.FriendListable;
import fr.badblock.bungee.modules.commands.basic.msg.PMPrivacy;
import fr.badblock.bungee.modules.commands.basic.party.PartyManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * BadCommand
 * 
 * @author xMalware
 *
 */
public class TrackCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public TrackCommand() {
		// Super!
		super("track", new String[] { "t" });
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

		BungeeManager bungeeManager = BungeeManager.getInstance();

		if (!bungeeManager.hasUsername(playerName)) {
			I19n.sendMessage(sender, getPrefix("offline"), null);
			return;
		}

		// Get the online target player
		BadPlayer badOnlinePlayer = bungeeManager.getBadPlayer(playerName);

		if (badOnlinePlayer == null) {
			I19n.sendMessage(sender, getPrefix("offline"), null);
			return;
		}

		String currentServer = badOnlinePlayer.getCurrentServer();
		String bungeeGroups = badOnlinePlayer.getRawChatPrefix();
		boolean isMute = badOnlinePlayer.getPunished() != null && badOnlinePlayer.getPunished().isMute();

		FriendListable friendlistable = badOnlinePlayer.getSettings().getFriendListable();
		Partyable partyable = badOnlinePlayer.getSettings().getPartyable();
		PMPrivacy pmPrivacy = badOnlinePlayer.getSettings().getPmPrivacy();

		BadIP badIp = BadIP.get(badOnlinePlayer.getLastIp());

		String locale = badOnlinePlayer.getLocale().name();

		PartyManager.sync.getParty(playerName, new Callback<Party>() {

			@Override
			public void done(Party result, Throwable error) {
				boolean inParty = result != null;
				String rawParty = Boolean.toString(inParty);
				String rawMute = Boolean.toString(isMute);
				String countryName = badIp != null && badIp.getApiData() != null ? badIp.getApiData().getCountryName()
						: "unknown";
				I19n.sendMessage(sender, getPrefix("message"), new int[] { 2, 3, 4, 5, 6, 7, 8, 9 }, playerName,
						currentServer, bungeeGroups, getPrefix("mute_" + rawMute),
						getPrefix("friendlistable_" + friendlistable.name()),
						getPrefix("partyable_" + partyable.name()), getPrefix("pmprivacy_" + pmPrivacy.name()),
						"locale." + locale, getPrefix("party_" + rawParty), "country." + countryName);
			}

		});

	}

}