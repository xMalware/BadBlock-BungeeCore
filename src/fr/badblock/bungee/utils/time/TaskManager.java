package fr.badblock.bungee.utils.time;

import java.util.UUID;

public class TaskManager
{

	public static Task scheduleDelayedTask(String taskName, Runnable runnable, long delay)
	{
		Task task = new Task(taskName)
		{
			@Override
			public void run()
			{
				if (delay > 0)
				{
					try
					{
						Thread.sleep(delay);
					}
					catch (InterruptedException exception)
					{
						exception.printStackTrace();
						return;
					}
				}
				runnable.run();
			}
		};
		task.start();
		return task;
	}

	public static Task scheduleDelayedTask(Runnable runnable, long delay)
	{
		return scheduleDelayedTask(UUID.randomUUID().toString(), runnable, delay);
	}

	public static Task scheduleRepeatingTask(String taskName, Runnable runnable, long delay, long repeat)
	{
		Task task = new Task(taskName)
		{
			@Override
			public void run()
			{
				if (delay > 0)
				{
					try
					{
						Thread.sleep(delay);
					}
					catch (InterruptedException exception)
					{
						exception.printStackTrace();
						return;
					}
				}
				while (true)
				{
					try
					{
						runnable.run();
					}
					catch (Exception exception)
					{
						exception.printStackTrace();
					}
					if (repeat > 0)
					{
						try
						{
							Thread.sleep(repeat);
						}
						catch (InterruptedException exception)
						{
							exception.printStackTrace();
							return;
						}
					}
				}
			}
		};
		task.start();
		return task;
	}
	

	public static Task scheduleRepeatingTask(Runnable runnable, long delay, long repeat)
	{
		return scheduleRepeatingTask(UUID.randomUUID().toString(), runnable, delay, repeat);
	}

}
