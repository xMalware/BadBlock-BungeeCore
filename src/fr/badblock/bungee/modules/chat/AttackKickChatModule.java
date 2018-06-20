package fr.badblock.bungee.modules.chat;

import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class AttackKickChatModule extends ChatModule {
	
	@SuppressWarnings("deprecation")
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

		if (badPlayer.getPunished() != null && badPlayer.getPunished().isMute())
		{
			return event;
		}
		
		if (event.getMessage().toLowerCase().contains("mcbot"))
		{
			event.setCancelled(true);
			event.getSender().disconnect(I19n.getMessage(Locale.FRENCH_FRANCE, "bungee.antibot.blocked ",null));
		}
		
		return event;
	}

}