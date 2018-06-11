package fr.badblock.bungee.modules.commands.basic.party;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.badblock.api.common.utils.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * Party object
 * 
 * @author xMalware
 *
 */
public class Party
{

	/**
	 * Map type
	 */
	private static final Type collectionType = new TypeToken<Map<String, PartyPlayer>>(){}.getType();

	/**
	 * Unique ID
	 * @param Set the unique ID
	 * @return Returns the unique ID
	 */
	public String					uuid;

	/**
	 * Players in the party
	 * @param Set the party
	 * @return Returns the party
	 */
	public Map<String, PartyPlayer>	players;

	/**
	 * Constructor
	 * @param Leader of the party
	 */
	public Party(String leader)
	{
		// Set UUID
		uuid = UUID.randomUUID().toString();
		// Create a new map
		players = new HashMap<>();
		// Add the leader as admin
		add(leader, PartyPlayerRole.ADMIN);
	}

	/**
	 * Constructor
	 * @param Leader of the party
	 * @param Invited player
	 */
	public Party(String leader, String invited)
	{
		// Do the first thing
		this(leader);
		// Invite as a default player
		invite(invited, PartyPlayerRole.DEFAULT);
	}

	/**
	 * Constructor
	 * @param Database object
	 */
	public Party(DBObject dbObject)
	{
		// Set the unique id
		uuid = dbObject.get("uuid").toString();
		// Set the player map
		players = GsonUtils.getGson().fromJson(dbObject.get("players").toString(), collectionType);
	}

	/**
	 * Get the party player
	 * @param The username
	 * @return Returns the PartyPlayer object
	 */
	public PartyPlayer getPartyPlayer(String name)
	{
		// Get the PartyPlayer object
		return getPlayers().get(name.toLowerCase());
	}

	/**
	 * To Party Player
	 * @param Name of the player
	 * @param Role of the player
	 * @param Party state of the player
	 * @return Returns the PartyPlayer object
	 */
	private PartyPlayer toPartyPlayer(String name, PartyPlayerRole role, PartyPlayerState state)
	{
		// Create a new PartyPlayer object
		return new PartyPlayer(name, role, state);
	}

	/**
	 * Add a player in the party
	 * @param Name of the player
	 * @param Role of the player
	 * @param Party state of the player
	 */
	public void add(String name, PartyPlayerRole role, PartyPlayerState state)
	{
		// Set the name to lower case
		name = name.toLowerCase();
		// If the player is in the list
		if (players.containsKey(name))
		{
			// Then remove the player
			players.remove(name);
		}
		// Create a party player
		PartyPlayer partyPlayer = toPartyPlayer(name, role, state);
		// Add the party player in the map
		players.put(name, partyPlayer);
		// Save the party
		save();
	}

	/**
	 * Add a player
	 * @param Username
	 * @param Player party role
	 */
	public void add(String name, PartyPlayerRole role)
	{
		// Add the player
		add(name, role, PartyPlayerState.ACCEPTED);
	}

	/**
	 * Invite a player
	 * @param Username
	 * @param Player party role
	 */
	public void invite(String name, PartyPlayerRole role)
	{
		// Add the player
		add(name, role, PartyPlayerState.WAITING);
	}

	/**
	 * Accept a player
	 * @param Username
	 */
	public void accept(String name)
	{
		// Add the player
		add(name, getPartyPlayer(name).getRole());
	}

	/**
	 * Set user role
	 * @param name
	 * @param partyPlayerRole
	 */
	public void setRole(PartyPlayer partyPlayer, PartyPlayerRole partyPlayerRole)
	{
		// Null player
		assert partyPlayer == null;
		// Unknown player
		assert !players.containsKey(partyPlayer.getName().toLowerCase());
		// Unknown role
		assert partyPlayerRole == null;
		
		// Set player role
		partyPlayer.setRole(partyPlayerRole);
		
		// Put in map
		players.put(partyPlayer.getName().toLowerCase(), partyPlayer);
		
		// Save the party
		save();
	}
	
	/**
	 * Removethe player
	 * @param Username
	 */
	public void remove(String name)
	{
		// Set the username to lower case
		name = name.toLowerCase();
		// Remove the username from the map
		players.remove(name);
		// Save the party
		save();
	}

	/**
	 * Save the party
	 */
	public void save()
	{
		// If the unique id is null
		if (uuid == null)
		{
			// So we stop there
			return;
		}
		
		// Update the party
		PartyManager.update(this);
	}
	
	/**
	 * Remove the party
	 */
	public void remove()
	{
		// If the unique id is null
		if (uuid == null)
		{
			// So we stop there
			return;
		}
		
		// Remove the party
		PartyManager.delete(this);
	}

	/**
	 * To object
	 * @return A DBObject object
	 */
	public DBObject toObject()
	{
		/**
		 * Create a new object
		 */
		BasicDBObject object = new BasicDBObject();

		// If the unique id isn't null
		if (uuid != null)
		{
			// Put the unique id
			object.put("uuid", uuid);
		}
		
		// Create a map
		Map<String, DBObject> map = new HashMap<>();
		
		// Put all players in the map
		players.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue().toObject()));
		
		// Put the player map
		object.put("players", map);

		// Returns the object
		return object;
	}

}