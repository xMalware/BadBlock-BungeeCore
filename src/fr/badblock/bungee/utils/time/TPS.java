package fr.badblock.bungee.utils.time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Queues;

import fr.badblock.bungee.BadBungee;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeCord;

/**
 * 
 * TPS class
 * 
 * @author xMalware
 * 
 */
public class TPS implements Runnable
{

	/**
	 * TPS
	 * @param Set the new TPS
	 * @return Returns the current TPS
	 */
	@Getter @Setter private static double		tps		= 0.0D;

	/**
	 * Queue
	 * @param Set the new queue
	 * @return Returns the current queue
	 */
	private static Queue<Double>				queue 	= Queues.newLinkedBlockingDeque();

	/**
	 * Time
	 * @param Set the new time
	 * @return Returns the current time
	 */
	private long				time;

	/**
	 * Sec
	 * @param Set the new sec
	 * @return Returns the current sec
	 */
	private long				sec;

	/**
	 * NB
	 * @param Set the new nb
	 * @return Returns the current nb
	 */
	private int 				nb;

	/*
	 * TPS Constructor
	 */
	public TPS()
	{
		// Set the time
		this.time = System.currentTimeMillis() + 1_000L;
		// Set the time
		this.sec = System.currentTimeMillis();

		// Schedule the task
		BungeeCord.getInstance().getScheduler().schedule(BadBungee.getInstance(), this, 0L, 50, TimeUnit.MILLISECONDS);

		// Create a new thread
		new Thread("TPS check")
		{
			// On run
			public void run()
			{
				// While true
				while(true)
				{
					// If the time is over the last (expired)
					if (System.currentTimeMillis() > time)
					{
						// Set the new time (1s)
						time = System.currentTimeMillis() + 1_000L;
						// Get the lost TPS
						double p = nb / 1_000D * 20D;
						// Default nb
						nb = 0;
						// Get the current TPS
						double d = 20.0D - p;
						// Add TPS in queue
						queue.add(d);
						// Set TPS
						setTps(d);
					}

					// Sleep 250ms
					TimeUtils.sleep(250);
				}
			}
			// Start
		}.start();
	}

	/**
	 * Run
	 */
	@Override
	public void run()
	{
		// Get difference
		double v = System.currentTimeMillis() - sec;
		// 1 tick = 50ms
		if (v > 50)
		{
			// Set the diff
			nb += v - 50;
		}
		// Set the sec
		sec = System.currentTimeMillis();
	}

	/**
	 * Round a number
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places)
	{
		// Problem with place number
		if (places < 0)
		{
			// Throw an exception
			throw new IllegalArgumentException();
		}

		// Create a BigDecimal
		BigDecimal bd = new BigDecimal(value);
		// Scale & round half_up 
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		// Get double value
		return bd.doubleValue();
	}

	/**
	 * Get lost milliseconds
	 * (lag factor)
	 * @return
	 */
	public static double getLostMilliseconds()
	{
		// Returns the lost ms (with TPS)
		return (20D - TPS.tps) * 50D;
	}

	/**
	 * Get lost milliseconds
	 * @param tps
	 * @return
	 */
	public static double getLostMilliseconds(double tps)
	{
		// Returns the lost ms (with given TPS)
		return (20D - tps) * 50D;
	}

}