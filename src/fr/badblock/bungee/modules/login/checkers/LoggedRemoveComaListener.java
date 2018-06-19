package fr.badblock.bungee.modules.login.checkers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import fr.badblock.bungee.modules.abstracts.BadListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.event.EventHandler;

public class LoggedRemoveComaListener extends BadListener {

	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
		if (event.getTag().equalsIgnoreCase("BungeeCord")) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
			try {
				String channel = in.readUTF(); // channel we delivered
				if (channel.equals("Auth")) {
					String name = in.readUTF(); // the inputstring
					ProxiedPlayer player = BungeeCord.getInstance().getPlayer(name);
					if (player != null) {
						UserConnection userConnection = (UserConnection) player;
						userConnection.setLogged(true);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

}