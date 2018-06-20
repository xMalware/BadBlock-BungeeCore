package fr.badblock.bungee.modules.commands.modo.punishments;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.api.CommandSender;

@AllArgsConstructor
@Data
public class PunishmentPacket
{
	
	private PunishmentType	punishmentType;
	private CommandSender	sender;
	private String			playerName;
	private String			reason;
	private boolean			isKey;
	private long			time;
	
	public void process()
	{
		assert punishmentType == null;
		
		punishmentType.process(this);
	}
	
}