package fr.badblock.bungee.link.processing.bungee.abstracts;

import fr.badblock.bungee.link.processing.bungee.*;
import lombok.Getter;

@Getter
public enum BungeePacketType
{

	BROADCAST(new BungeeBroadcastProcessing());
	
	private _BungeeProcessing process;
	
	BungeePacketType(_BungeeProcessing process)
	{
		this.process = process;
	}
	
}
