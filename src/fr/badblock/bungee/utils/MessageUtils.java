package fr.badblock.bungee.utils;

public class MessageUtils
{

	public static String getFullMessage(String[] messages)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (String message : messages)
		{
			stringBuilder.append(message + "\n");
		}
		return stringBuilder.toString();
	}
	
}