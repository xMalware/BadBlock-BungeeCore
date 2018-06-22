package fr.badblock.bungee.modules.commands.login;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.modules.login.datamanager.SkeletonConnectorListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * @author xMalware
 *
 */
public class RegisterCommand extends BadCommand {

	private static String prefix = "bungee.commands.register.";

	/**
	 * Command constructor
	 */
	public RegisterCommand() {
		super("register", "");
		this.setForPlayersOnly(true);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (args.length != 2)
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "usage", null);
			return;
		}

		if (badPlayer.getLoginPassword() != null && !badPlayer.getLoginPassword().isEmpty())
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "pleaselogin", null);
			return;
		}

		String password = args[0];
		String repeatPassword = args[0];

		if (!HashLogin.isValidPassword(password))
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "notsecureenough", null);
			return;
		}

		if (!password.equals(repeatPassword))
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "notsamepasswords", null);
			return;
		}

		String passwordHash = HashLogin.hash(password);

		badPlayer.setLoginPassword(passwordHash);
		try
		{
			badPlayer.saveData();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		
		ServerInfo server = SkeletonConnectorListener.roundrobinHub();

		if (server == null)
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "nohubavailable", null);
			return;
		}

		badPlayer.sendTranslatedOutgoingMessage(prefix + "registered", null);
		badPlayer.setLoginStepOk(true);

		proxiedPlayer.connect(server);
	}

}