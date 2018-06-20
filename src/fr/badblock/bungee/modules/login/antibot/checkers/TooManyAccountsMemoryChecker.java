package fr.badblock.bungee.modules.login.antibot.checkers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TooManyAccountsMemoryChecker extends AntiBotChecker
{

	private Map<String, Set<String>>	accounts	= new HashMap<>();
	
	@Override
	public int getId()
	{
		return 4;
	}
	
	@Override
	public boolean accept(String username, String address)
	{
		Set<String> list = null;
		if (!accounts.containsKey(address))
		{
			list = new HashSet<>();
		}
		else
		{
			list = accounts.get(address);
		}
		
		if (!list.contains(username))
		{
			list.add(username);
		}
		
		if (list.size() >= 4)
		{
			return false;
		}
		
		accounts.put(address, list);
		
		return true;
	}
	
}
