package fr.badblock.bungee.modules.chat;

import net.md_5.bungee.api.event.ChatEvent;

public class ReportMessageChatModule extends ChatModule {

	@Override
	public ChatEvent check(ChatEvent event)
	{
		return event;
	}

}