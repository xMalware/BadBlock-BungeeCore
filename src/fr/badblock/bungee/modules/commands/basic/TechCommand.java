package fr.badblock.bungee.modules.commands.basic;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.config.BadBungeeConfig;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.TechPing;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 *
 * @author xMalware
 *
 */
public class TechCommand extends BadCommand {

	private TechPing	techPing	= new TechPing();
	
	/**
	 * Command constructor
	 */
	public TechCommand() {
		super("tech");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		BadBungee badBungee = BadBungee.getInstance();
		BadBungeeConfig badConfig = null;
		if (badBungee != null)
		{
			badConfig = badBungee.getConfig();
		}
		// Bungee name
		String bungeeName = badConfig != null ? badConfig.getBungeeName() : "unknown";
		I19n.sendMessage(sender, "bungee.commands.tech.message", null, DateUtils.getHourDate(), bungeeName,
				techPing.getMongoPing(), techPing.getRabbitPing());
	}

}