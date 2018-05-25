package fr.badblock.bungee.utils.tasks;

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
