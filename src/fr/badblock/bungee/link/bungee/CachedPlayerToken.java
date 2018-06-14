package fr.badblock.bungee.link.bungee;

import fr.badblock.bungee.utils.time.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
/**
 * Players token cache
 * 
 * @author xMalware
 */
public class CachedPlayerToken {

	/**
	 * Generate a new token
	 * 
	 * @return
	 */
	public static CachedPlayerToken generateToken() {
		// Get the token ID
		int token = BungeeManager.getInstance().getBungees().values().parallelStream().filter(b -> b.isValid())
				.map(bungee -> bungee.getUsernames().size()).mapToInt(Number::intValue).sum();
		// Returns the new token
		return new CachedPlayerToken(TimeUtils.nextTimeWithSeconds(1), token);
	}
	/**
	 * Last calculated token timestamp
	 * 
	 * @param Set
	 *            the new calculated token timestamp
	 * @return Returns the last calculated token timestamp
	 */
	private long lastCalculatedToken;

	/**
	 * Last token
	 * 
	 * @param Set
	 *            the new token
	 * @return Returns the last token
	 */
	private int lastToken;

	/**
	 * If the token is expired
	 * 
	 * @return if the token is expired or not
	 */
	public boolean isExpired() {
		// Check with the timestamp
		return TimeUtils.isExpired(lastCalculatedToken);
	}

}
