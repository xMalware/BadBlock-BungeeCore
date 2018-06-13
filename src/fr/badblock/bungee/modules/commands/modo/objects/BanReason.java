package fr.badblock.bungee.modules.commands.modo.objects;

public enum BanReason
{

	CHEATING,
	
	COMPROMISED_ACCOUNT,

	ADVERTISING,

	INAPPROPRIATE_NAME_OR_SKIN,
	
	INAPPROPRIATE_BEHAVIOUR,

	VERBAL_ABUSE,

	BAN_EVASION,

	FRAUD,

	HACK_THREATS,

	ANTI_GAMING,

	EXPLOIT;
	
	public String getName()
	{
		return name().toLowerCase();
	}
	
	public static BanReason getFromString(String string)
	{
		for (BanReason banReason : values())
		{
			if (banReason.name().equalsIgnoreCase(string))
			{
				return banReason;
			}
		}
		return null;
	}
	
}