package fr.badblock.bungee.modules.commands.modo.objects;

import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BanIndex
{

	private	int		index;
	private long	time;
	
	public BanIndex(DBObject dbObject)
	{
		index = Integer.parseInt(dbObject.get("index").toString());
		time = Long.parseLong(dbObject.get("time").toString());
	}
	
}
