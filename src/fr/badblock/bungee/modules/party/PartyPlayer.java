package fr.badblock.bungee.modules.party;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class PartyPlayer
{

	private String				name;
	private PartyPlayerRole		role;
	private PartyPlayerState	state;
	private boolean				follow	= true;
	
	public PartyPlayer(String name, PartyPlayerRole role, PartyPlayerState state)
	{
		this(name, role, state, true);
	}
	
}
