package fr.badblock.bungee.modules.chat;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class NullServerChatModule extends ChatModule {

	@Override
	public ChatEvent check(ChatEvent event) {
		if (event.getSender() == null) {
			event.setCancelled(true);
			return event;
		}

		Connection connection = event.getSender();

		if (connection instanceof ProxiedPlayer) {
			ProxiedPlayer bPlayer = (ProxiedPlayer) connection;
			if (bPlayer != null && bPlayer.getServer() == null) {
				event.setCancelled(true);
				bPlayer.disconnect(new TextComponent("Erreur. Impossible de trouver le serveur. (code 629)"));
			}
		}

		return event;
	}

}