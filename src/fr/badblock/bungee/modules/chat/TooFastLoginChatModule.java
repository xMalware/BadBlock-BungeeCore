package fr.badblock.bungee.modules.chat;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.bungee.modules.login.antibot.AntiBotData;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class TooFastLoginChatModule extends ChatModule {
	
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
		
		return event;
	}

}