package fr.badblock.bungee.modules.commands;

import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.api.common.utils.i18n.I18n;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.time.ThreadRunnable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * 
 * Abstract class that makes it easier to create commands on the server without
 * actually having to save them.
 * 
 * @author xMalware
 *
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public abstract class BadCommand extends Command {

	// Boolean that lets you know if the command is only allowed for players or also
	// for the console.
	private boolean forPlayersOnly;

	/**
	 * Typical command builder
	 * 
	 * @param name
	 *            > name of the command
	 */
	public BadCommand(String name) {
		// Sends the name of the command to the superclass
		super(name);
		// Load the command when it is instantiated.
		this.load();
	}

	public BadCommand(String name, String permission, String... aliases) {
		// Sends the details of the command to the superclass
		super(name, permission, aliases);
		// Load the command when it is instantiated.
		this.load();
	}

	@SuppressWarnings("deprecation")
	/**
	 * Method executed when the command is sended
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		// If this command is for players only
		if (isForPlayersOnly()) {
			// So we check if the sender isn't a player
			if (!(sender instanceof ProxiedPlayer)) {
				// The sender is not a player, so we let him know that he cannot execute the
				// command.
				sender.sendMessages(I18n.getInstance().get("bungee.commands.playersonly"));
				// We stop there
				return;
			}
		}
		// Logging
		BungeeManager.getInstance()
				.log(sender.getName() + " issued command /" + getName() + " " + StringUtils.join(args, " "));
		// Execute the command in a separate thread
		ThreadRunnable.run(() -> BadCommand.this.run(sender, args));
	}

	/**
	 * Abstract method that allows BadBlock commands to differentiate themselves
	 * from others by being implemented in the classes of commands
	 * 
	 * @param sender
	 * @param args
	 */
	public abstract void run(CommandSender sender, String[] args);

	/**
	 * Registration of the command
	 */
	private void load() {
		// We get the plugin manager
		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
		// We register the command
		pluginManager.registerCommand(BadBungee.getInstance(), this);
		// We say that the command has been registered
		BadBungee.log("§aLoaded command: " + getClass().getSimpleName());
	}

}