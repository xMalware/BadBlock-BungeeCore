package fr.badblock.bungee.loader;

import fr.badblock.bungee.BadBungee;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
/**
 * 
 * BungeeCord unloader
 * 
 * @author xMalware
 *
 */
public class BungeeUnloader
{

	private BadBungee		badBungee;

	public BungeeUnloader(BadBungee badBungee)
	{
		setBadBungee(badBungee);
		setUnload();
		unloadMongo();
		unloadRabbit();
	}

	private void setUnload()
	{
		getBadBungee().setUnloaded(true);
	}

	private void unloadMongo()
	{
		getBadBungee().getMongoService().remove();
	}

	private void unloadRabbit()
	{
		getBadBungee().getRabbitService().remove();
	}

}
