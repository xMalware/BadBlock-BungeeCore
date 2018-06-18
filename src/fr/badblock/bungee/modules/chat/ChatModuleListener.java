package fr.badblock.bungee.modules.chat;

import fr.badblock.bungee.modules.abstracts.BadListener;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatModuleListener extends BadListener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(ChatEvent event) {
		for (ChatModule chatModule : ChatModule.getModules()) {
			event = chatModule.check(event);

			if (event.isCancelled()) {
				break;
			}
		}
	}

}
