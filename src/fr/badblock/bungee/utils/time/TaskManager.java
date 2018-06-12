package fr.badblock.bungee.utils.time;

import java.util.UUID;

/**
 * 
 * TaskManager
 * 
 * @author xMalware
 *
 */
public class TaskManager
{

	/**
	 * Schedule a delayed task
	 * @param The task name
	 * @param Runnable
	 * @param Delay (ms)
	 * @return the Task object
	 */
	public static Task scheduleDelayedTask(String taskName, Runnable runnable, long delay)
	{
		// Create a new task
		Task task = new Task(taskName)
		{
			/**
			 * Run
			 */
			@Override
			public void run()
			{
				// If the delay is more than 0
				if (delay > 0)
				{
					// So we try
					try
					{
						// to sleep?
						Thread.sleep(delay);
					}
					// If we can't
					catch (InterruptedException exception)
					{
						// Print a strack trace
						exception.printStackTrace();
						// And we stop there
						return;
					}
				}
				
				// 'Run the runnable', that's a match!
				runnable.run();
			}
		};
		
		// Start the task
		task.start();
		
		// Returns the task
		return task;
	}

	/**
	 * Schedule a delayed task
	 * @param Runnable
	 * @param Delay (ms)
	 * @return a Task object
	 */
	public static Task scheduleDelayedTask(Runnable runnable, long delay)
	{
		// Schedule with random name
		return scheduleDelayedTask(UUID.randomUUID().toString(), runnable, delay);
	}

	/**
	 * Schedule a repeating task
	 * @param The task name
	 * @param Runnable
	 * @param Delay (ms)
	 * @param Repeat (ms)
	 * @return a Task object
	 */
	public static Task scheduleRepeatingTask(String taskName, Runnable runnable, long delay, long repeat)
	{
		// Create a new task
		Task task = new Task(taskName)
		{
			/**
			 * Run
			 */
			@Override
			public void run()
			{
				// If the delay is more than 0
				if (delay > 0)
				{
					// So we try
					try
					{
						// to sleep?
						Thread.sleep(delay);
					}
					// If we can't
					catch (InterruptedException exception)
					{
						// So we print the stacktrace
						exception.printStackTrace();
						// And we stop there
						return;
					}
				}
				
				// While true
				while (true)
				{
					// So we try
					try
					{
						// To 'run the runnable', that's a match!
						runnable.run();
					}
					// If we can't
					catch (Exception exception)
					{
						// Print the stacktrace
						exception.printStackTrace();
					}
					
					// If the repeat integer is > than 0
					if (repeat > 0)
					{
						// So we try
						try
						{
							// to sleep?
							Thread.sleep(repeat);
						}
						// If we can't
						catch (InterruptedException exception)
						{
							// Print the stacktrace
							exception.printStackTrace();
							// And we stop there
							return;
						}
					}
				}
			}
		};
		
		// Start the task
		task.start();
		
		// Returns the task
		return task;
	}
	
	/**
	 * Schedule a new repeating task
	 * @param Runnable
	 * @param Delay (ms)
	 * @param Repeat (ms)
	 * @return a Task object
	 */
	public static Task scheduleRepeatingTask(Runnable runnable, long delay, long repeat)
	{
		// We use the other method, with random task name
		return scheduleRepeatingTask(UUID.randomUUID().toString(), runnable, delay, repeat);
	}

}
