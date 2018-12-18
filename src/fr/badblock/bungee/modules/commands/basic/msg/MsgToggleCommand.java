package fr.badblock.bungee.modules.commands.basic.msg;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.players.layer.BadPlayerSettings;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Modo command
 * 
 * @author xMalware
 *
 */
public class MsgToggleCommand extends BadCommand {

	private static String prefix = "bungee.commands.msgtoggle.";

	/**
	 * Command constructor
	 */
	public MsgToggleCommand() {
		super("msgtoggle", null, "msgp");
		this.setForPlayersOnly(true);
	}

	@SuppressWarnings("static-access")
	public String prefix(String prefix)
	{
		return this.prefix + "." + prefix;
	}
	
	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		BadPlayer player = BadPlayer.get(proxiedPlayer);

		if (player == null) {
			I19n.sendMessage(sender, prefix + "erroroccurred", null, 1);
			return;
		}

		if (args.length < 1)
		{
			String help_intro = player.getTranslatedMessage(prefix("help_intro"), new int[] { 0 },
					player.getRawChatPrefix(), player.getName());
			String help_intro_hover = player.getTranslatedMessage(prefix("help_intro_hover"), null);

			String help_allow_everyone = player.getTranslatedMessage(prefix("help_allow_everyone"), new int[] { 0 },
					player.getRawChatPrefix(), player.getName());
			String help_allow_everyone_hover = player.getTranslatedMessage(prefix("help_allow_everyone_hover"),
					new int[] { 0 }, player.getRawChatPrefix(), player.getName());

			String help_allow_only_friends = player.getTranslatedMessage(prefix("help_allow_only_friends"), new int[] { 0 },
					player.getRawChatPrefix(), player.getName());
			String help_allow_only_friends_hover = player.getTranslatedMessage(prefix("help_allow_only_friends_hover"),
					new int[] { 0 }, player.getRawChatPrefix(), player.getName());
			
			String help_nobody = player.getTranslatedMessage(prefix("help_nobody"), new int[] { 0 },
					player.getRawChatPrefix(), player.getName());
			String help_nobody_hover = player.getTranslatedMessage(prefix("help_nobody_hover"),
					new int[] { 0 }, player.getRawChatPrefix(), player.getName());

			// Create MCJson object
			McJson json = new McJsonFactory(help_intro).setHoverText(help_intro_hover).finaliseComponent().build();
			player.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
			
			player.sendOutgoingMessage("");

			json = new McJsonFactory(help_allow_everyone).setHoverText(help_allow_everyone_hover).
					setClickCommand("/msgtoggle with_everyone").finaliseComponent().build();
			player.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);

			player.sendOutgoingMessage("");

			json = new McJsonFactory(help_allow_only_friends).setHoverText(help_allow_only_friends_hover).
					setClickCommand("/msgtoggle with_only_his_friends").finaliseComponent().build();
			player.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);

			player.sendOutgoingMessage("");
			
			json = new McJsonFactory(help_nobody).setHoverText(help_nobody_hover).
					setClickCommand("/msgtoggle with_nobody").finaliseComponent().build();
			player.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
			return;
		}
		
		BadPlayerSettings settings = player.getSettings();

		String action = args[0].toLowerCase();
		PMPrivacy newAction = PMPrivacy.getByString(action);

		if (newAction == null)
		{
			I19n.sendMessage(sender, prefix + "unknown_action", null, 1);
			return;
		}

		if (settings.getPmPrivacy().equals(newAction))
		{
			I19n.sendMessage(sender, prefix + "pm_" + action + "_already", null, 1);
			return;
		}

		settings.setPmPrivacy(newAction);
		try {
			player.saveData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		I19n.sendMessage(sender, prefix + "pm_" + action, null, 1);
		return;
	}

}