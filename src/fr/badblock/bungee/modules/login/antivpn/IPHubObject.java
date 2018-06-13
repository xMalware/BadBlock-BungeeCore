package fr.badblock.bungee.modules.login.antivpn;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * 
 * IPHub object
 * 
 * @author xMalware
 *
 */
public class IPHubObject {

	/**
	 * IP
	 * 
	 * @param Set
	 *            the new IP
	 * @return Returns the current IP
	 */
	private String ip;

	/**
	 * Country code
	 * 
	 * @param Set
	 *            the new country code
	 * @return Returns the country code
	 */
	private String countryCode;

	/**
	 * Country name
	 * 
	 * @param Set
	 *            the new country name
	 * @return Returns the country name
	 */
	private String countryName;

	/**
	 * ASN
	 * 
	 * @param Set
	 *            the new ASN
	 * @return Returns the ASN
	 */
	private int asn;

	/**
	 * ISP
	 * 
	 * @param Set
	 *            the new ISP
	 * @return Returns the ISP
	 */
	private String isp;

	/**
	 * Block
	 * 
	 * @param Set
	 *            the new block id
	 * @return Returns the new block id
	 */
	private int block;

}