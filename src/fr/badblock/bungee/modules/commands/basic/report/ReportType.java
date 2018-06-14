package fr.badblock.bungee.modules.commands.basic.report;

public enum ReportType
{

	TEAMING,
	
	CHEATING,
	
	ANTI_GAME,
	
	OTHER;

	public String getName()
	{
		return name().toLowerCase();
	}

	public static ReportType getFromString(String rawReportType)
	{
		for (ReportType reportType : values())
		{
			if (reportType.name().equalsIgnoreCase(rawReportType))
			{
				return reportType;
			}
		}
		return null;
	}
	
}
