package fr.badblock.bungee.utils.time;

import java.util.UUID;

/**
 * 
 * A task
 * 
 * @author xMalware
 *
 */
public class Task extends Thread
{

	/**
	 * Constructor
	 * @param name
	 */
	public Task(String name)
	{
		// Set the name
		setName("task-" + name);
	}
	
	/**
	 * Constructor with random name
	 */
	public Task()
	{
		// Use the first constructor
		this(UUID.randomUUID().toString());
	}
	
}