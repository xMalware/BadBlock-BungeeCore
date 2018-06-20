package fr.badblock.bungee.modules.login.antibot.checkers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Queues;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.AsnResponse;

public class ASNChecker extends AntiBotChecker
{

	public Map<Integer, Queue<Long>> 	asn				= new HashMap<>();
	public Map<Integer, Queue<Long>>	blockedAsn		= new HashMap<>();

	public Queue<Long>					invalidASNIPs	= Queues.newLinkedBlockingDeque();

	private	DatabaseReader	reader;

	public ASNChecker()
	{
		File database = new File("geoip/GeoIP2-ASN.mmdb");

		try {
			reader = new DatabaseReader.Builder(database).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getId()
	{
		return 1;
	}
	
	@Override
	public boolean accept(String username, String address)
	{
		try {
			InetAddress ipAddress = InetAddress.getByName(address);

			AsnResponse response = reader.asn(ipAddress);

			int id = response.getAutonomousSystemNumber();

			if (!asn.containsKey(id))
			{
				Queue<Long> queue = Queues.newLinkedBlockingDeque();
				asn.put(id, queue);
				return true;
			}

			Queue<Long> queue = asn.get(id);

			queue.add(System.currentTimeMillis());
			asn.put(id, queue);

			if (blockedAsn.containsKey(id))
			{
				return false;
			}

			if (queue.size() >= 60)
			{
				long time = System.currentTimeMillis() - queue.poll();
				int count = queue.size();
				
				if (time / count < 500)
				{
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			if (invalidASNIPs.size() >= 5)
			{
				int count = invalidASNIPs.size();
				long time = System.currentTimeMillis() - invalidASNIPs.poll();
				if (time / count <= 600)
				{
					return false;
				}
			}
			
			invalidASNIPs.add(System.currentTimeMillis());
			return true;
		}
	}

}
