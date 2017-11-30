package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerReloadDataProcessing extends _PlayerProcessing
{

	@Override
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket)
	{
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		badPlayer.reload();
	}
	
}
