package fr.badblock.bungee.utils.logfilters;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.md_5.bungee.BungeeCord;

/**
 * A simple log filter that cancels messages stating that a client has connected
 * to InitialHandler. These messages are sent when a client sends a server list
 * request packet (i.e. evey time somebody opens their server list) and when a
 * client starts its connection to the server.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 02/10/15
 */
public class IHConnectedFilter extends PropagatingFilter {
	private boolean filterPings = true;
	private boolean filterJoins = true;

	public IHConnectedFilter() {
		super(BungeeCord.getInstance().getLogger());
	}

	IHConnectedFilter(Logger logger) { // for unit tests
		super(logger);
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		if (record.getMessage().toLowerCase().contains("reset by peer"))
			return false;
		if (record.getMessage().toLowerCase().contains("read timed out"))
			return false;
		if (record.getMessage().equals("net.md_5.bungee.connection.InitialHandler"))
			return false;
		if (record.getMessage().endsWith("has disconnected") || record.getMessage().endsWith("has connected")
				|| record.getMessage().equals("No client connected for pending server!"))
			return false;
		if ((!filterPings && !filterJoins) || !"{0} has connected".equals(record.getMessage())
				|| record.getParameters().length != 1) {
			return true;
		}

		String param1 = String.valueOf(record.getParameters()[0]);
		if (param1.endsWith("<-> InitialHandler")) {
			if (filterPings && param1.contains("/")) { // ip addresses contain / in this impl
				return false; // e.g. /127.0.0.1:17835 (Java thing)
			}
			if (filterJoins && !param1.contains("/")) { // everything else connecting to
				return false; // InitialHandler must be a join
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "IHConnectedFilter{" + "filterPings=" + filterPings + ", filterJoins=" + filterJoins + '}';
	}
}
