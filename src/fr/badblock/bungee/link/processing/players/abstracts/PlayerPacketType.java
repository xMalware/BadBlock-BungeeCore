package fr.badblock.bungee.link.processing.players.abstracts;

import fr.badblock.bungee.link.processing.players.PlayerSendMessageProcessing;
import fr.badblock.bungee.link.processing.players.PlayerSendServerProcessing;
import lombok.Getter;

@Getter
public enum PlayerPacketType
{

	SEND_MESSAGE	(new PlayerSendMessageProcessing()),
	SEND_SERVER		(new PlayerSendServerProcessing());
	
	private _PlayerProcessing process;
	
	PlayerPacketType(_PlayerProcessing process)
	{
		this.process = process;
	}
	
}
