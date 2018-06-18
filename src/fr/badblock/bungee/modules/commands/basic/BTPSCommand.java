package fr.badblock.bungee.modules.commands.basic;

import fr.badblock.api.common.utils.general.MathUtils;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.config.BadBungeeConfig;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.time.TPS;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Get the TPS. Usage: /btps
 * 
 * @author xMalware
 *
 */
public class BTPSCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public BTPSCommand() {
		super("btps", "", "bungee", "bungeecord", "abs", "absorbance");
		// New tps monitor
		new TPS();
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		BadBungee badBungee = BadBungee.getInstance();
		BadBungeeConfig badConfig = null;
		if (badBungee != null) {
			badConfig = badBungee.getConfig();
		}
		int ping = sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getPing() : 0;
		// Bungee name
		String bungeeName = badConfig != null ? badConfig.getBungeeName() : "unknown";
		double percent = (double) TPS.getTps() / 20.0D * 100.0D;
		I19n.sendMessage(sender, "bungee.commands.btps.message", null, DateUtils.getHourDate(), bungeeName,
				MathUtils.round(percent, 2), TPS.getTps(), ping);
	}

}