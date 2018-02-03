package fr.badblock.bungee._plugins.objects.party;

public enum Partyable
{

	WITH_EVERYONE,
	WITH_ONLY_HIS_FRIENDS,
	WITH_NOBODY;
	
	public static Partyable getByString(String string)
	{
		for (Partyable partyable : values())
		{
			if (string.equalsIgnoreCase(partyable.name()))
			{
				return partyable;
			}
		}
		return null;
	}
	
}
