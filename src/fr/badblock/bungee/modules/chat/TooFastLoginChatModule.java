package fr.badblock.bungee.modules.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Queues;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.bungee.modules.login.antibot.AntiBotData;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class TooFastLoginChatModule extends ChatModule {
	
	private Map<String, Queue<Long>> map = new HashMap<>();
	
	@Override
	public ChatEvent check(ChatEvent event)
	{
		if (event.isCancelled())
		{
			return event;
		}

		if (!(event.getSender() instanceof ProxiedPlayer))
		{
			return event;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (badPlayer.isLoginStepOk() || badPlayer.isOnlineMode())
		{
			return event;
		}
		
		long time = TimeUtils.time() - badPlayer.getLoginTimestamp();
		
		if (time <= 1000)
		{
			event.setCancelled(true);
			
			String address = badPlayer.getLastIp();
			AntiBotData.reject(address, badPlayer.getName());
			return event;	
		}
		
		String message = event.getMessage();
		Queue<Long> queue = !map.containsKey(message) ? Queues.newLinkedBlockingDeque() : map.get(message);
		queue.add(TimeUtils.time());
		
		map.put(message, queue);
		
		if (queue.size() >= 5)
		{
			long between = TimeUtils.time() - queue.poll();
			if (between <= 3000)
			{
				event.setCancelled(true);
				
				String address = badPlayer.getLastIp();
				AntiBotData.reject(address, badPlayer.getName());
				return event;
			}
		}
		
		return event;
	}

}