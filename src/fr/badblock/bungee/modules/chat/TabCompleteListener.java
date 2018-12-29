package fr.badblock.bungee.modules.chat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.utils.time.TimeUtils;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class TabCompleteListener extends BadListener {

	private static Set<String> mixer = new HashSet<>();
	private Thread	thread;

	public TabCompleteListener()
	{
		thread = Thread.currentThread();
		new Thread()
		{
			@Override
			public void run()
			{
				while (true)
				{
					int current = BungeeManager.getInstance().getOnlinePlayers();
					int stoops = mixer.size() - current;
					if (stoops < 0)
					{
						TimeUtils.sleepInSeconds(1);
						continue;
					}

					synchronized (thread)
					{
						Iterator<String> iterator = mixer.iterator();
						while (iterator.hasNext() && stoops > 0)
						{
							String name = iterator.next();
							if (BungeeManager.getInstance().hasUsername(name))
							{
								continue;
							}
							iterator.remove();
							stoops--;
						}
					}

					TimeUtils.sleepInSeconds(1);
				}
			}
		}.start();
	}

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onTabComplete(TabCompleteEvent event) {
		String partialPlayerName = event.getCursor().toLowerCase();

		int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');
		if (lastSpaceIndex >= 0)
		{
			partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
		}

		for (String string : mixer)
		{
			if (string.toLowerCase().startsWith(partialPlayerName))
			{
				event.getSuggestions().add(string);
			}
		}
	}
	
	public static void put(String name)
	{
		mixer.add(name);
	}

}
