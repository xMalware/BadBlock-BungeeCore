package fr.badblock.bungee.modules.login.antivpn;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.utils.NetworkUtils;
import fr.badblock.bungee.utils.time.TimeUtils;
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
				try
				{
					String ip = vpn.poll();
					work(ip);
				}
				catch (Exception error)
				{
					error.printStackTrace();
				}
				//TODO
				TimeUtils.sleepInSeconds(5);
			}
			TimeUtils.sleepInSeconds(5);
		}
	}

	private boolean work(String ip) throws UnknownHostException
	{
		if (isThisLocal(InetAddress.getByName(ip)))
		{
			return true;
		}

		BadIP badIp = new BadIP(ip);

		if (badIp.isVpn())
		{
			return false;
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

	public boolean isThisLocal(InetAddress addr)
	{
		// Check if the address is a valid special local or loop back
		if (addr.isAnyLocalAddress() || addr.isLoopbackAddress())
		{
			return true;
		}

		// Check if the address is defined on any interface
		try
		{
			return NetworkInterface.getByInetAddress(addr) != null;
		}
		catch (SocketException e)
		{
			return false;
		}
	}

	public static AntiVPN getInstance()
	{
		return instance;
	}

}