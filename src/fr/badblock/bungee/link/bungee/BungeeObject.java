package fr.badblock.bungee.link.bungee;

import java.util.Map;

import org.bson.BSONObject;

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
	private Map<String, BSONObject>	usernames;
	private long					timestamp;
	
	public void refresh(Map<String, BSONObject> set)
	{
		setUsernames(set);
		setTimestamp(BungeeTask.getTimestamp());
	}

}
