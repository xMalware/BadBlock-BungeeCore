package fr.badblock.bungee.modules.chat;

import java.util.Arrays;
import java.util.List;

import fr.badblock.bungee.modules.commands.login.HashLogin;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;

public class NotLoggedChatModule extends ChatModule {

	private static List<String> commands = Arrays.asList("/login", "/l", "/register");

	@Override
	public ChatEvent check(ChatEvent event) {
		if (event.isCancelled()) {
			return event;
		}

		if (!(event.getSender() instanceof ProxiedPlayer)) {
			return event;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (badPlayer == null) {
			event.setCancelled(true);
			return event;
		}

		Server server = proxiedPlayer.getServer();

		if (server == null) {
			return event;
		}

		ServerInfo serverInfo = server.getInfo();

		if (serverInfo == null) {
			return event;
		}

		String message = event.getMessage();

		String[] splitter = message.split(" ");

		String command = splitter[0];

		if (splitter[0].isEmpty()) {
			return event;
		}

		if (!badPlayer.isLoginStepOk()) {
			command = command.toLowerCase();
			if (!commands.contains(command)) {
				event.setCancelled(true);
				HashLogin.sendLoginMessage(badPlayer);
				return event;
			}
		}

		return event;
	}

}