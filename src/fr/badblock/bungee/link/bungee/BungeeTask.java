package fr.badblock.bungee.link.bungee;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.rabbit.BadBungeeQueues;
import fr.badblock.bungee.utils.time.TimeUtils;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacket;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketType;
import net.md_5.bungee.BungeeCord;

public class BungeeTask extends Thread
{

	public static boolean		run				= true;
	public static BungeeObject	bungeeObject	= new BungeeObject(BadBungee.getInstance().getConfig().getBungeeName(), getIP(), new HashMap<>(), getTimestamp());
	
	public BungeeTask()
	{
		this.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		// While true
		while (true)
		{
			// Useless.
			if (!run)
			{
				stop();
				return;
			}
			// Waiting
			try {
				keepAlive();
				sleep(1000);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public static long getTimestamp()
	{
		return TimeUtils.nextTimeWithSeconds(30);
	}
	
	public static String getIP()
	{
		return BungeeCord.getInstance().config.getListeners().iterator().next().getHost().getAddress().getHostAddress();
	}
	
	public static void keepAlive()
	{
		BadBungee badBungee = BadBungee.getInstance();
		final Map<String, BadPlayer> players = new HashMap<>();
		BadPlayer.getPlayers().forEach(player -> players.put(player.getName(), player));
		bungeeObject.refresh(players);
		Gson gson = badBungee.getGson();
		String jsonFormatString = gson.toJson(bungeeObject);
		badBungee.getRabbitService().sendPacket(new RabbitPacket(new RabbitPacketMessage(5000, jsonFormatString), 
				BadBungeeQueues.BUNGEE_DATA, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER));
	}

}
