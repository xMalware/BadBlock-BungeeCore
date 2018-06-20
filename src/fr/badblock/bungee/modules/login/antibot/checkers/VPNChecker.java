package fr.badblock.bungee.modules.login.antibot.checkers;

import java.net.UnknownHostException;

import fr.badblock.bungee.modules.login.antivpn.AntiVPN;

public class VPNChecker extends AntiBotChecker
{
	
	@Override
	public int getId()
	{
		return 7;
	}
	
	@Override
	public boolean accept(String username, String address)
	{
		if (username == null)
		{
			return true;
		}
		
		AntiVPN antiVpn = AntiVPN.getInstance();
		
		try {
			return !antiVpn.isAVPN(address);
		} catch (UnknownHostException e) {
			return false;
		}
	}

}
