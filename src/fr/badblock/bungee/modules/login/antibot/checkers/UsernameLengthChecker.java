package fr.badblock.bungee.modules.login.antibot.checkers;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Queues;

public class UsernameLengthChecker extends AntiBotChecker
{

	public Map<Integer, Queue<Long>> characters = new HashMap<>();

	@Override
	public boolean accept(String username, String address)
	{
		int length = username.length();
		if (!characters.containsKey(length))
		{
			Queue<Long> list = Queues.newLinkedBlockingDeque();
			list.add(System.currentTimeMillis());
			characters.put(length, list);
		}
		else
		{
			Queue<Long> list = characters.get(length);
			list.add(System.currentTimeMillis());
			characters.put(length, list);
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
