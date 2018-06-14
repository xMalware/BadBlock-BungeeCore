package fr.badblock.bungee.modules.commands.basic.report;

public enum ReportType {

	ANTI_GAME,

	CHEATING,

	OTHER,

	TEAMING;

	public static ReportType getFromString(String rawReportType) {
		for (ReportType reportType : values()) {
			if (reportType.name().equalsIgnoreCase(rawReportType)) {
				return reportType;
			}
		}
		return null;
	}

	public String getName() {
		return name().toLowerCase();
	}

}
