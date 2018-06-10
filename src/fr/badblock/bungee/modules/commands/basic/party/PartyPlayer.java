package fr.badblock.bungee.modules.commands.basic.party;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * Party player
 * 
 * @author xMalware
 *
 */
public class PartyPlayer
{

	/**
	 * Username
	 * @param Set the username
	 * @return Returns the username
	 */
	private String				name;
	
	/**
	 * Role
	 * @param Set the role
	 * @return Returns the role
	 */
	private PartyPlayerRole		role;
	
	/**
	 * State
	 * @param Set the state
	 * @return Returns the state
	 */
	private PartyPlayerState	state;
	
	/**
	 * Follow
	 * @param Set the follow bool
	 * @return Returns the follow bool
	 */
	private boolean				follow	= true;
	
	/**
	 * Constructor
	 * @param Username
	 * @param Role
	 * @param State
	 */
	public PartyPlayer(String name, PartyPlayerRole role, PartyPlayerState state)
	{
		this(name, role, state, true);
	}
	
}
