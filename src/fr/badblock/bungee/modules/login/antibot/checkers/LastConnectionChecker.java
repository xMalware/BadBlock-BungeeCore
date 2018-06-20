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
			
			if (list.size() >= 100)
			{
				list.poll();
			}
			
			if (list.size() > 5)
			{
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				
				if (averageTime <= 100)
				{
					return false;
				}
			}
			
			if (list.size() > 10)
			{
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				
				if (averageTime <= 200)
				{
					return false;
				}
			}
			
			if (list.size() > 20)
			{
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				
				if (averageTime <= 300)
				{
					return false;
				}
			}
			
			if (list.size() > 30)
			{
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				
				if (averageTime <= 400)
				{
					return false;
				}
			}
			
			if (list.size() > 60)
			{
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
