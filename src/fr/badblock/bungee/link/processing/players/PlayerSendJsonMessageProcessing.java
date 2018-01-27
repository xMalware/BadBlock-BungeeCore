package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.utils.ChatColorUtils;
import fr.badblock.bungee.utils.mcjson.McJsonUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerSendJsonMessageProcessing extends _PlayerProcessing {

	@SuppressWarnings("deprecation")
	@Override
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket) {
        McJsonUtils.sendJsons(proxiedPlayer, McJsonUtils.parseMcJsons(ChatColorUtils.translateColors('&', playerPacket.getContent().split(System.lineSeparator()))));
	}

}
