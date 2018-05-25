package fr.badblock.bungee.link.bungee;

import fr.badblock.bungee.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CachedPlayerToken
{

	private long	lastCalculatedToken;
	private int		lastToken;

	public boolean isExpired()
	{
		return TimeUtils.isExpired(lastCalculatedToken);
	}

	public static CachedPlayerToken generateToken()
	{
		int token = BungeeManager.getInstance().getBungees().values().parallelStream().filter(b -> b.isValid()).map(bungee -> bungee.getUsernames().size()).mapToInt(Number::intValue).sum();
		return new CachedPlayerToken(TimeUtils.nextTimeWithSeconds(1), token);
	}

}
