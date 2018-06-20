package fr.badblock.bungee.modules.login.antivpn;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.utils.NetworkUtils;
import fr.badblock.bungee.utils.time.TimeUtils;

/**
 * 
 * AntiVPN module
 * 
 * @author xMalware
 *
 */
public class AntiVPN extends Thread {

	/**
	 * AntiVPN instance
	 */
	public static AntiVPN instance = new AntiVPN();

	/**
	 * Get the AntiVPN instance
	 * 
	 * @return
	 */
	public static AntiVPN getInstance() {
		return instance;
	}

	/**
	 * API key
	 */
	private String apiKey = BadBungee.getInstance().getConfig().getIpHubApiKey();

	/**
	 * VPN queue
	 */
	private Queue<String> vpn = new LinkedBlockingQueue<>();

	/**
	 * Constructor Starts the module as soon as it is instantiated
	 */
	public AntiVPN() {
		// Start
		start();
	}

	/**
	 * Add the IP to the check queue
	 * 
	 * @param ip
	 */
	public void addToCheck(String ip) {
		// Add the IP to the check queue
		vpn.add(ip);
	}

	/**
	 * If the address is local
	 * 
	 * @param addr
	 * @return
	 */
	public boolean isThisLocal(InetAddress addr) {
		// Check if the address is a valid special local or loop back
		if (addr.isAnyLocalAddress() || addr.isLoopbackAddress()) {
			// Yes
			return true;
		}

		// Check if the address is defined on any interface
		try {
			// Defined on any interface?
			return NetworkInterface.getByInetAddress(addr) != null;
		}
		// Error case
		catch (SocketException e) {
			// No
			return false;
		}
	}

	@Override
	/**
	 * Run
	 */
	public void run() {
		// Always running
		while (true) {
			// While the queue isn't empty
			while (!vpn.isEmpty()) {
				// Try to
				try {
					// Get the IP
					String ip = vpn.poll();
					// Check the IP
					work(ip);
				}
				// Error case
				catch (Exception error) {
					// Print the stack trace
					error.printStackTrace();
				}
				// Wait five seconds
				TimeUtils.sleepInSeconds(5);
			}
			// Wait five seconds
			TimeUtils.sleepInSeconds(5);
		}
	}

	/**
	 * Work the IP
	 * 
	 * @param ip
	 * @return
	 * @throws UnknownHostException
	 */
	public void work(String ip) throws UnknownHostException {
		// If the IP is local
		if (isThisLocal(InetAddress.getByName(ip))) {
			// Accepted
			return;
		}

		// Get the BadIP object
		BadIP badIp = new BadIP(ip);

		// If it's a VPN
		if (badIp.isVpn()) {
			// Declined
			return;
		}

		// Get the url
		String url = "http://v2.api.iphub.info/ip/" + ip;
		// Get the source code
		String sourceCode = NetworkUtils.fetchSourceCodeWithAPI(url, apiKey);
		
		if (sourceCode == null)
		{
			BadBungee.log("§c[VPN] Unable to check IP.");
			return;
		}
		
		// Get the IPHub object
		IPHubObject object = GsonUtils.getGson().fromJson(sourceCode, IPHubObject.class);

		badIp.setApiData(object);

		// If it's a VPN
		if (object != null && object.getBlock() == 1) {
			// Set as a VPN
			badIp.setVpn(true);
			// Kick VPN
			badIp.kick("VPN");
		}

		try {
			badIp.saveData();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		// TODO Check ISP
		return;
	}

}