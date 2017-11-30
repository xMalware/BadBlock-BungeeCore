package fr.badblock.bungee.link.bungee;

import java.util.Map;

import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class BungeeObject
{

	private String					name;
	private String 		 			ip;
	private Map<String, BadPlayer>	usernames;
	private long					timestamp;
	
	public void refresh(Map<String, BadPlayer> set)
	{
		setUsernames(set);
		setTimestamp(BungeeTask.getTimestamp());
	}

}
