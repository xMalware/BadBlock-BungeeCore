package fr.badblock.bungee.utils.time;

import java.util.UUID;

public class Task extends Thread
{

	public Task(String name)
	{
		setName("task-" + name);
	}
	
	public Task()
	{
		this(UUID.randomUUID().toString());
	}
	
}
