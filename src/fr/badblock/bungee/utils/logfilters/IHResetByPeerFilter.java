package fr.badblock.bungee.utils.logfilters;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.md_5.bungee.BungeeCord;

/**
 * A simple log filter that cancels messages stating that an InitialHandler
 * connection has been reset due to an IOException with the message "connection
 * reset by peer" (or a localised equivalent specified by the user).
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 02/10/15
 */
public class IHResetByPeerFilter extends PropagatingFilter {
	public static final String DEFAULT_MESSAGE = "connection reset by peer";
	private boolean filterResetByPeer = true;
	private String resetByPeerMessage = DEFAULT_MESSAGE;

	public IHResetByPeerFilter() {
		super(BungeeCord.getInstance().getLogger());
	}

	IHResetByPeerFilter(Logger logger) { // for unit tests
		super(logger);
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		// BungeeCord source:
		// https://github.com/SpigotMC/BungeeCord/blob/master/proxy/src/main/java/net/md_5/bungee/netty/HandlerBoss.java#L109
		return !filterResetByPeer || // we might not even be filtering
				!"{0} - IOException: {1}".equals(record.getMessage()) || // wrong message
				record.getParameters().length != 2 || // that message always has exactly two arguments
				!String.valueOf(record.getParameters()[0]).endsWith("InitialHandler") || // first arg is handler
				!resetByPeerMessage.equals(String.valueOf(record.getParameters()[1])); // second arg is message
	}

	@Override
	public String toString() {
		return "IHResetByPeerFilter{" + "message=" + resetByPeerMessage + '}';
	}
}
