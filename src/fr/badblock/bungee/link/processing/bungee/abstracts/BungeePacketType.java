package fr.badblock.bungee.link.processing.bungee.abstracts;

import fr.badblock.bungee.link.processing.bungee.BungeeBroadcastProcessing;
import fr.badblock.bungee.link.processing.bungee.BungeeLogProcessing;
import lombok.Getter;

@Getter
public enum BungeePacketType
{

	BROADCAST(new BungeeBroadcastProcessing()),
	LOG(new BungeeLogProcessing());
	
	private _BungeeProcessing process;
	
	BungeePacketType(_BungeeProcessing process)
	{
		this.process = process;
	}
	
}
