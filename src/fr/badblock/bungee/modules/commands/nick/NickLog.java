package fr.badblock.bungee.modules.commands.nick;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NickLog
{

	private String	realName;
	private String	realUuid;
	private String	date;
	private long	timestamp;
	private String	nickname;
	
}
