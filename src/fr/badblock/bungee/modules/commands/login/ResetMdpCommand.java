package fr.badblock.bungee.modules.commands.login;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * @author xMalware
 *
 */
public class ResetMdpCommand extends BadCommand {

	private static String prefix = "bungee.commands.resetmdp.";

	/**
	 * Command constructor
	 */
	public ResetMdpCommand() {
		super("changepassword", "", "editpassword", "resetmdp", "mdp");
		this.setForPlayersOnly(true);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (!badPlayer.isLogged())
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "mustbeloggedtodothis", null);
			return;
		}
		
		if (badPlayer.getLoginPassword() == null || badPlayer.getLoginPassword().isEmpty()) {
			badPlayer.sendTranslatedOutgoingMessage(prefix + "mustberegisteredtodothis", null);
			return;
		}

		if (args.length != 2) {
			badPlayer.sendTranslatedOutgoingMessage(prefix + "usage", null, proxiedPlayer.getName());
			return;
		}

		String password = args[0];

		password = HashLogin.hash(password);

		if (!badPlayer.getLoginPassword().equals(password)) {
			badPlayer.sendTranslatedOutgoingMessage(prefix + "invalidpassword", null);
			return;
		}
		
		String newpassword = args[1];

		String newpassword2 = HashLogin.hash(newpassword);

		if (badPlayer.getLoginPassword().equals(newpassword2)) {
			badPlayer.sendTranslatedOutgoingMessage(prefix + "same", null);
			return;
		}

		if (!HashLogin.isValidPassword(newpassword))
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "notsecureenough", null);
			return;
		}

		badPlayer.setLoginPassword(newpassword2);
		try {
			badPlayer.saveData();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		badPlayer.kick(MessageUtils.getFullMessage(badPlayer.getTranslatedMessages(prefix + "passwordchanged", null, badPlayer.getName())));
		
	}

}