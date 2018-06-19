package fr.badblock.bungee.modules.login.antibot.checkers;

public abstract class AntiBotChecker
{

	public abstract boolean accept(String username, String address);
	
}
