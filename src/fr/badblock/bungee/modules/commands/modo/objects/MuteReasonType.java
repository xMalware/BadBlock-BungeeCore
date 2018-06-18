package fr.badblock.bungee.modules.commands.modo.objects;

public enum MuteReasonType {

	DISRESPECTFUL,

	INAPPROPRIATE,

	NEGATIVE_BEHAVIOUR,

	RUDE,

	SOCIAL_MEDIA_ADVERTISING,

	SPAM,

	SWEARING,

	VERBAL_ABUSE,

	OTHER;

	public static MuteReasonType getFromString(String string) {
		for (MuteReasonType muteReason : values()) {
			if (muteReason.name().equalsIgnoreCase(string)) {
				return muteReason;
			}
		}
		return null;
	}

	public String getName() {
		return name().toLowerCase();
	}

}