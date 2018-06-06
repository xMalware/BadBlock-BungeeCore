package fr.badblock.bungee.link.processing.bungee.abstracts;

import fr.badblock.bungee.link.processing.bungee.BungeeBroadcastProcessing;
import fr.badblock.bungee.link.processing.bungee.BungeeLogProcessing;
import lombok.Getter;

@Getter
/**
 * 
 * Bungee packet types
 * 
 * @author xMalware
 *
 */
public enum BungeePacketType
{

	/**
	 * Broadcast packet
	 */
	BROADCAST(new BungeeBroadcastProcessing()),
	/**
	 * Logging packet
	 */
	LOG(new BungeeLogProcessing());
	
	/**
	 * Custom packet processing
	 * @return The custom packet processing
	 */
	private _BungeeProcessing process;
	
	/**
	 * Constructor of a new Bungee packet tpye
	 * @param custom packet processing
	 */
	BungeePacketType(_BungeeProcessing process)
	{
		// Set the custom packet processing
		this.process = process;
	}
	
}
