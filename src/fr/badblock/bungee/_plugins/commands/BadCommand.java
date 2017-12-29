package fr.badblock.bungee._plugins.commands;

import fr.badblock.bungee.BadBungee;
import fr.toenga.common.utils.i18n.I18n;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class BadCommand extends Command
{

	private boolean forPlayersOnly;

	public BadCommand(String name) {
		super(name);
		this.load();
	}

	public BadCommand(String name, String permission, String... aliases) {
		super(name, permission, aliases);
		this.load();
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (isForPlayersOnly())
		{
			if (!(sender instanceof ProxiedPlayer))
			{
				sender.sendMessages(I18n.getInstance().get("commands.playersonly"));
				return;
			}
		}
		run(sender, args);
	}

	public abstract void run(CommandSender sender, String[] args);

	private void load()
	{
		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
		pluginManager.registerCommand(BadBungee.getInstance(), this);
		BadBungee.log("§aLoaded command: " + getClass().getSimpleName());
	}

}
