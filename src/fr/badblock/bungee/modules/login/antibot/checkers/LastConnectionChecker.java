package fr.badblock.bungee.modules.login.antibot.checkers;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Queues;

public class LastConnectionChecker extends AntiBotChecker
{

	public Map<String, Queue<Long>> connections = new HashMap<>();

	@Override
	public int getId()
	{
		return 3;
	}
	
	@Override
	public boolean accept(String username, String address)
	{
		if (!connections.containsKey(address))
		{
			Queue<Long> list = Queues.newLinkedBlockingDeque();
			list.add(System.currentTimeMillis());
			connections.put(address, list);
		}
		else
		{
			Queue<Long> list = connections.get(address);
			list.add(System.currentTimeMillis());
			connections.put(address, list);
			if (list.size() >= 30)
			{
				if (list.size() >= 100)
				{
					list.poll();
				}
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				if (averageTime <= 1000)
				{
					return false;
				}
			}
		}
		return true;
	}

}
