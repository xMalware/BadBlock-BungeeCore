package fr.badblock.bungee.players.layer;

import fr.badblock.bungee._plugins.objects.party.Partyable;
import lombok.Data;

@Data
public class BadPlayerSettings
{
	
	// Is partyable by who?
	public Partyable	partyable;

	/**
	 * Default values for each player
	 */
	public BadPlayerSettings()
	{
		partyable = Partyable.WITH_EVERYONE;
	}
	
}
