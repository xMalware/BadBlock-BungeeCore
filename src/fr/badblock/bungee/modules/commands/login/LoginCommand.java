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
public class LoginCommand extends BadCommand {

	private static String prefix = "bungee.commands.login.";
	
	/**
	 * Command constructor
	 */
	public LoginCommand() {
		super("login", "", "l");
		this.setForPlayersOnly(true);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (badPlayer.getLoginPassword() == null || badPlayer.getLoginPassword().isEmpty())
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "pleaseregister", null);
			return;
		}
		
		if (args.length != 1)
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "usage", null, proxiedPlayer.getName());
			return;
		}
		
		String password = args[0];
		
		password = HashLogin.hash(password);
		
		if (!badPlayer.getLoginPassword().equals(password))
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "invalidpassword", null);
			return;
		}
		
		ServerInfo server = SkeletonConnectorListener.roundrobinHub();
		
		if (server == null)
		{
			badPlayer.sendTranslatedOutgoingMessage(prefix + "nohubavailable", null, badPlayer.getName());
			return;
		}
		
		System.out.println(server.getName());
		
		badPlayer.setLoginStepOk(true);
		badPlayer.sendTranslatedOutgoingMessage(prefix + "validpassword", null, badPlayer.getName());
		proxiedPlayer.connect(server);
	}

}