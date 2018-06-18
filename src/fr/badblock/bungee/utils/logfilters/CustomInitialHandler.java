package fr.badblock.bungee.utils.logfilters;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.connection.InitialHandler;

public class CustomInitialHandler extends InitialHandler {

	public CustomInitialHandler(BungeeCord bungee, ListenerInfo listener) {
		super(bungee, listener);
	}

}
