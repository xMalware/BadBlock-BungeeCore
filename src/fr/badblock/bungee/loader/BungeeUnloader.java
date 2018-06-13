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
public class BungeeUnloader {

	/**
	 * BadBungee object
	 * 
	 * @param Set
	 *            the new BadBungee object
	 * @return Returns the current BadBungee object
	 */
	private BadBungee badBungee;

	/**
	 * Constructor of the BungeeUnloader object
	 * 
	 * @param BadBungee
	 *            object
	 */
	public BungeeUnloader(BadBungee badBungee) {
		// Set BadBungee
		setBadBungee(badBungee);
		// Set unload state
		setUnload();
		// Unload MongoDB
		unloadMongo();
		// Unload RabbitMQ
		unloadRabbit();
	}

	/**
	 * Set the unload state
	 */
	private void setUnload() {
		// Set unloaded
		getBadBungee().setUnloaded(true);
	}

	/**
	 * Unload MongoDB (remove the service)
	 */
	private void unloadMongo() {
		// Remove the service
		getBadBungee().getMongoService().remove();
	}

	/**
	 * Unload RabbitMQ (remove the service)
	 */
	private void unloadRabbit() {
		// Remove the service
		getBadBungee().getRabbitService().remove();
	}

}