package fr.badblock.bungee.utils.premium;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.BadOfflinePlayer;

public class PremiumCheck
{

	public static boolean premiumAutoSet(BadOfflinePlayer badPlayer)
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new SetPremiumTask(badPlayer));

        try {
        	String s = future.get(2, TimeUnit.SECONDS);
            return s != null;
        } catch (Exception e) {
        	BadBungee.log("§c[PremiumCheck] Unable to finish at time.");
            future.cancel(true);
        }

        executor.shutdownNow();
        return false;
	}


}
