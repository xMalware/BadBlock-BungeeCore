package fr.badblock.bungee.link.processing.bungee;

import java.util.logging.Level;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.abstracts._BungeeProcessing;

public class BungeeLogProcessing extends _BungeeProcessing
{

	@Override
	public void done(String message)
	{
		for (String string : message.split(System.lineSeparator()))
		{
			BadBungee.getInstance().getLogger().log(Level.INFO, string);
		}
	}

}
