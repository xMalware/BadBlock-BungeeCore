package fr.badblock.bungee._plugins.objects.antivpn;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mongodb.DB;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.utils.NetworkUtils;
import fr.badblock.bungee.utils.TimeUtils;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.utils.general.GsonUtils;

public class AntiVPN extends Thread
{

	public static AntiVPN	instance		= new AntiVPN();

	private Queue<String>	vpn				= new LinkedBlockingQueue<>();
	private String			apiKey			= BadBungee.getInstance().getConfig().getIpHubApiKey();

	public AntiVPN()
	{
		start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			while (!vpn.isEmpty())
			{
				String ip = vpn.poll();
				work(ip);
				TimeUtils.sleepInSeconds(5);
			}
			TimeUtils.sleepInSeconds(5);
		}
	}
	
	private boolean work(String ip)
	{
		BadIP badIp = new BadIP(ip);
		
		if (badIp.isVpn())
		{
			return true;
		}
		
		String url = "http://v2.api.iphub.info/ip/" + ip;
		String sourceCode = NetworkUtils.fetchSourceCodeWithAPI(url, apiKey);
		
		IPHubObject object = GsonUtils.getGson().fromJson(sourceCode, IPHubObject.class);
		
		if (object != null && object.getBlock() == 1)
		{
			badIp.setVpn(true);
			badIp.updateVPN();
			return false;
		}
		
		// TODO Check ISP
		return true;
	}
	
	public void addToCheck(String ip)
	{
		vpn.add(ip);
	}
	
	public static AntiVPN getInstance()
	{
		return instance;
	}

}