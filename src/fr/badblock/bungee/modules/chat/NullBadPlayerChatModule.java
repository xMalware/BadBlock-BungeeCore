package fr.badblock.bungee.modules.chat;

import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class NullBadPlayerChatModule extends ChatModule {

	@Override
	public ChatEvent check(ChatEvent event) {
		if (event.getSender() == null)
		{
			return event;
		}

		Connection connection = event.getSender();

		if (!(connection instanceof ProxiedPlayer))
		{
			return event;
		}

		ProxiedPlayer bPlayer = (ProxiedPlayer) connection;

		BadPlayer badPlayer = BadPlayer.get(bPlayer);

		if (badPlayer == null)
		{
			I19n.sendMessage(bPlayer, "bungee.chat.unabletochat", null);
			return event;
		}

		return event;
	}

}