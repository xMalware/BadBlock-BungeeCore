package fr.badblock.bungee.modules.login.antibot;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.bungee.modules.login.antibot.checkers.ASNChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.AntiBotChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.ForeignCountryChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.LastConnectionChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.TooManyAccountsMemoryChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.UsernameLengthChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.UsernameSyllablesChecker;
import fr.badblock.bungee.modules.login.antibot.checkers.VPNChecker;

public class AntiBotData
{

	public static AntiBotChecker[] checkers		= new AntiBotChecker[] {
			new ASNChecker(),
			new ForeignCountryChecker(),
			new LastConnectionChecker(),
			new TooManyAccountsMemoryChecker(),
			new UsernameLengthChecker(),
			new UsernameSyllablesChecker(),
			new VPNChecker()
	};

	public static Map<String, Long> blockedAddresses	= new HashMap<>();
	public static Map<String, Long> blockedUsernames	= new HashMap<>();
	
	public static void reject(String address, String username)
	{
		AntiBotData.blockedUsernames.put(username, System.currentTimeMillis() + 300_000L);
		AntiBotData.blockedAddresses.put(address, System.currentTimeMillis() + 300_000L);
	}
	
}
