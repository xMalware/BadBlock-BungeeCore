package fr.badblock.bungee.link.bungee;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.time.TimeUtils;
import net.md_5.bungee.BungeeCord;

/**
 * 
 * Task to send data in a recurrent time frame to synchronize nodes
 * 
 * @author xMalware
 *
 */
public class BungeeTask extends Thread {

	/**
	 * Local bungee object of the node
	 * 
	 * @param New
	 *            local bungee object of the node
	 * @return The current local bungee object of the node
	 */
	public static BungeeObject bungeeObject = new BungeeObject(BadBungee.getInstance().getConfig().getBungeeName(),
			getIP(), getTimestamp(), new HashMap<>());
	/**
	 * If the task is still running
	 * 
	 * @param New
	 *            state of the task
	 * @return If the task is still running or not
	 */
	public static boolean run = true;

	/**
	 * Get the current BungeeCord node IP
	 * 
	 * @return
	 */
	public static String getIP() {
		return BungeeCord.getInstance().config.getListeners().iterator().next().getHost().getAddress().getHostAddress();
	}

	/**
	 * Get the new expire time
	 * 
	 * @return expire time in milliseconds (30+ seconds)
	 */
	public static long getTimestamp() {
		// Get with TimeUtils
		return TimeUtils.nextTimeWithSeconds(30);
	}

	/**
	 * Keep alive the current BungeeCord node
	 */
	public static void keepAlive() {
		// Get main class
		BadBungee badBungee = BadBungee.getInstance();
		// New map of players
		final Map<String, BadPlayer> players = new HashMap<>();
		// Put players in the map
		BadPlayer.getPlayers().forEach(player -> players.put(player.getName(), player));
		// Refresh with the new player list
		bungeeObject.refresh(players);
		// Get gson
		Gson gson = badBungee.getGson();
		// Deserialize the object
		String jsonFormatString = gson.toJson(bungeeObject);
		// Send KeepAlive packet
		badBungee.getRabbitService().sendPacket(new RabbitPacket(new RabbitPacketMessage(5000, jsonFormatString),
				BadBungeeQueues.BUNGEE_DATA, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER));
	}

	/**
	 * Constructor of a new Bungee task Automatic task start when instantiating
	 */
	public BungeeTask() {
		// Start the thread
		this.start();
	}

	@Override
	/**
	 * Data sending loop method
	 */
	public void run() {
		// While the task is allowed to run
		while (run) {
			// Send a keep alive packet
			keepAlive();
			// Sleep 1 second
			TimeUtils.sleepInSeconds(1);
		}
	}

}
