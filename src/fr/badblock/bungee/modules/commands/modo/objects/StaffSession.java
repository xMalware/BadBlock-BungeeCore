package fr.badblock.bungee.modules.commands.modo.objects;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StaffSession
{
	
/*
 * long timestamp = System.currentTimeMillis();
		long endTime = timestamp;
		long startTime = friendPlayer.startTime;
		if (startTime != -1 && player.hasPermission("others.tracksessions")) {
			long totalTime = (endTime - startTime) / 1000;
			long sanctions = friendPlayer.sanctions;
			long sanctionsTime = sanctions * (60 * 5); // 5 min par sanction
			if (sanctionsTime > totalTime) sanctionsTime = totalTime; // wtf
			BadblockDatabase.getInstance().addRequest(new Request("INSERT INTO staffSessions(playerName, timestamp, startTime, endTime, totalTime, sanctions, sanctionsTime) VALUES('" + BadblockDatabase.getInstance().mysql_real_escape_string(player.getName()) + "', '" + timestamp + "', '" + startTime + "', '" + endTime + "', '" + totalTime + "', '" + sanctions + "', '" + sanctionsTime + "')", RequestType.SETTER));
		}
 */
	
	private String	playerName;
	private UUID	playerUuid;
	private long	timestamp;
	private long	startTime;
	private long	totalTime;
	private long	punishments;
	private long	punishmentTime;
	
}
