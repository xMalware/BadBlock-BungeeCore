package fr.badblock.bungee.link.processing.bungee;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.bungee.link.bungee.BungeeManager;

/**
 * 
 * Server broadcast BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeBroadcastPermissionProcessing extends _BungeeProcessing {

	@Override
	/**
	 * Server broadcast processing
	 * 
	 * @param Messages
	 *            to broadcast
	 */
	public void done(String message)
	{
		try
		{
			String[] splitter = message.split(";");
			String permission = splitter[0];
			String broadcast = splitter[1];

			// brodcast ptdr => mais flemme de changer la faute d'orthographe
			BungeeManager.getInstance().targetedBrodcast(permission, broadcast);
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}
	}

	@Override
	public BungeePacketType getPacketType() {
		return BungeePacketType.PERMISSION_BROADCAST;
	}

}
