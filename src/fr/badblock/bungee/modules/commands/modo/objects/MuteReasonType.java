package fr.badblock.bungee.modules.commands.modo.objects;

public enum MuteReasonType {

	SPAM(),

	INAPPROPRIATE,

	RUDE,

	SWEARING,

	DISRESPECTFUL,

	VERBAL_ABUSE,

	NEGATIVE_BEHAVIOUR,

	SOCIAL_MEDIA_ADVERTISING,

	OTHER;

	public String getName() {
		return name().toLowerCase();
	}

	public static MuteReasonType getFromString(String string) {
		for (MuteReasonType muteReason : values()) {
			if (muteReason.name().equalsIgnoreCase(string)) {
				return muteReason;
			}
		}
		return null;
	}

}