package fr.badblock.bungee.rabbit.claimants;

import fr.badblock.bungee.utils.time.TaskManager;

/**
 * 
 * A Rabbit claimant
 * 
 * @author xMalware
 *
 */
public abstract class RabbitClaimant implements Runnable
{

	/**
	 * Constructor with only a delay
	 * @param Claimant name
	 * @param Delay (milliseconds)
	 */
	public RabbitClaimant(String name, long delay)
	{
		// Run the task
		TaskManager.scheduleDelayedTask("rabbitClaimant-" + name, this, delay);
	}
	
	/**
	 * Constructor with a delay & repeat time
	 * @param Claimant name
	 * @param Delay (milliseconds)
	 * @param Repeat (milliseconds)
	 */
	public RabbitClaimant(String name, long delay, long repeat)
	{
		// Run the task
		TaskManager.scheduleRepeatingTask("rabbitClaimant-" + name, this, delay, repeat);
	}
	
}
