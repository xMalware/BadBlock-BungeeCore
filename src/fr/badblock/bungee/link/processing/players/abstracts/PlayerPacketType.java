package fr.badblock.bungee.link.processing.players.abstracts;

import fr.badblock.bungee.link.processing.players.*;
import lombok.Getter;

@Getter
public enum PlayerPacketType
{

	SEND_MESSAGE	  (new PlayerSendMessageProcessing()),
    SEND_JSON_MESSAGE (new PlayerSendJsonMessageProcessing()),
	SEND_SERVER		  (new PlayerSendServerProcessing()),
	RELOAD_DATA		  (new PlayerReloadDataProcessing());
	
	private _PlayerProcessing process;
	
	PlayerPacketType(_PlayerProcessing process)
	{
		this.process = process;
	}
	
}
