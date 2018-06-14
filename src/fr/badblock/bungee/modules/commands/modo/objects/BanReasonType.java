package fr.badblock.bungee.modules.commands.modo.objects;

public enum BanReasonType {

	ADVERTISING,

	ANTI_GAMING,

	BAN_EVASION,

	CHEATING,

	COMPROMISED_ACCOUNT,

	EXPLOIT,

	FRAUD,

	HACK_THREATS,

	INAPPROPRIATE_BEHAVIOUR,

	INAPPROPRIATE_NAME_OR_SKIN,

	VERBAL_ABUSE;

	public static BanReasonType getFromString(String string) {
		for (BanReasonType banReason : values()) {
			if (banReason.name().equalsIgnoreCase(string)) {
				return banReason;
			}
		}
		return null;
	}

	public String getName() {
		return name().toLowerCase();
	}

}