package fr.badblock.bungee.modules.chat;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.ChatEvent;

public abstract class ChatModule
{
	
	@Getter
	private static List<ChatModule>	modules	= new ArrayList<>();
	
	@SuppressWarnings("deprecation")
	public ChatModule()
	{
		// Add modules
		modules.add(this);
		// We mark in the logs that the chat module is loaded
		BungeeCord.getInstance().getConsole().sendMessage("§e[BadBungee] §aLoaded chat module: " + getClass().getSimpleName());
	}
	
	public abstract ChatEvent check(ChatEvent event);
	
}
