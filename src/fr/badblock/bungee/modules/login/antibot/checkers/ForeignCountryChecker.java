package fr.badblock.bungee.modules.login.antibot.checkers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.Queues;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;

public class ForeignCountryChecker extends AntiBotChecker
{

	public Queue<Long> 		unknownCountryIPs	= Queues.newLinkedBlockingDeque();
	public Queue<Long> 		invalidCountryIPs	= Queues.newLinkedBlockingDeque();

	public List<String> 	allowedCountryFlows = Arrays.asList("BE", "FR", "CA", "CH");

	private	DatabaseReader	reader;

	public ForeignCountryChecker()
	{
		File database = new File("geoip/GeoIP2-City.mmdb");

		try {
			reader = new DatabaseReader.Builder(database).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getId()
	{
		return 2;
	}
	
	@Override
	public boolean accept(String username, String address)
	{
		try {
			InetAddress ipAddress = InetAddress.getByName(address);

			CityResponse response = reader.city(ipAddress);

			Country country = response.getCountry();
			String countryCode = country.getIsoCode();

			if (!allowedCountryFlows.contains(countryCode))
			{
				if (unknownCountryIPs.size() >= 60)
				{
					long time = System.currentTimeMillis() - unknownCountryIPs.poll();
					int size = unknownCountryIPs.size();
					if (time / size < 1000)
					{
						return false;
					}
				}
				unknownCountryIPs.add(System.currentTimeMillis());
			}

			return true;
		} catch (Exception e) {
			if (invalidCountryIPs.size() >= 5)
			{
				int count = invalidCountryIPs.size();
				long time = System.currentTimeMillis() - invalidCountryIPs.poll();
				if (time / count <= 600)
				{
					return false;
				}
			}
			invalidCountryIPs.add(System.currentTimeMillis());
			return true;
		}
	}

}
