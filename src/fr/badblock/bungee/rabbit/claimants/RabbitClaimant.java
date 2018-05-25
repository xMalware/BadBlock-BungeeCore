package fr.badblock.bungee.rabbit.claimants;

import fr.badblock.bungee.utils.tasks.TaskManager;

public abstract class RabbitClaimant implements Runnable
{

	public RabbitClaimant(String name, long delay)
	{
		TaskManager.scheduleDelayedTask("rabbitClaimant-" + name, this, delay);
	}
	
	public RabbitClaimant(String name, long delay, long repeat)
	{
		TaskManager.scheduleRepeatingTask("rabbitClaimant-" + name, this, delay, repeat);
	}
	
}
