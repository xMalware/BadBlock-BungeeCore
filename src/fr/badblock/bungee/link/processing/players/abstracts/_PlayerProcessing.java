package fr.badblock.bungee.link.processing.players.abstracts;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Player Packet Processing Abstract Class
 * 
 * @author xMalware
 *
 */
public abstract class _PlayerProcessing {

	/**
	 * Message processing
	 * 
	 * @param The
	 *            ProxiedPlayer object
	 * @param The
	 *            PlayerPacket object
	 */
	public abstract void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket);

	/**
	 * Get the proxied player by getting the packet
	 * 
	 * @param player
	 *            packet object
	 * @return the proxied player
	 */
	protected ProxiedPlayer getProxiedPlayerByPacket(PlayerPacket playerPacket) {
		// Getting the player name
		String playerName = playerPacket.getPlayerName();
		// Get BungeeCord Instance
		BungeeCord bungeeCord = BungeeCord.getInstance();
		// Get the ProxiedPlayer object with the playr name
		return bungeeCord.getPlayer(playerName);
	}

	/**
	 * Work with player packets
	 * 
	 * @param player
	 *            packet object
	 */
	public void work(PlayerPacket playerPacket) {
		// Get the proxied player from the player packet
		ProxiedPlayer proxiedPlayer = getProxiedPlayerByPacket(playerPacket);
		System.out.println("B : " + playerPacket.getPlayerName());
		// If the player isn't online on this node
		if (proxiedPlayer == null) {
			// So we stop there
			System.out.println("C : " + playerPacket.getPlayerName());
			return;
		}
		System.out.println("D : " + playerPacket.getPlayerName());
		// Work
		done(proxiedPlayer, playerPacket);
	}

}
