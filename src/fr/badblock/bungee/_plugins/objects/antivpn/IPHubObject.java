package fr.badblock.bungee._plugins.objects.antivpn;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IPHubObject
{

	private String	ip;
	private String	countryCode;
	private String	countryName;
	private int		asn;
	private String	isp;
	private int		block;
	
}
