package fr.badblock.bungee._plugins.commands.z_basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee._plugins.objects.party.PartyManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyCommand extends BadCommand
{
	
	public PartyCommand()
	{
		super("party", null, "groupe", "pa", "gr");
		this.setForPlayersOnly(true);
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		if (args.length == 0)
		{
			help(proxiedPlayer);
			return;
		}
		String subCommand = args[0];
		switch (subCommand)
		{
		// Help
		case "help":
		case "?":
		case "aide":
			help(proxiedPlayer);
			break;
			// Follow
		case "follow":
		case "suivi":
		case "suivre":
			PartyManager.follow(proxiedPlayer);
			break;
			// Toggle
		case "toggle":
			PartyManager.toggle(proxiedPlayer, args);
			break;
			// Invite
		case "invite":
		case "create":
		case "creer":
		case "inviter":
		case "add":
		case "ajouter":
			PartyManager.invite(proxiedPlayer, args);
			break;
			// Accept
		case "accept":
		case "accepter":
			PartyManager.accept(proxiedPlayer, args);
			break;
			// Remove
		case "remove":
		case "delete":
		case "erase":
		case "supprimer":
		case "kick":
			PartyManager.remove(proxiedPlayer, args);
			// Teleport
		case "tp":
		case "teleport":
		case "connect":
		case "c":
			PartyManager.tp(proxiedPlayer, args);
			// Default
		default:
			unknown(proxiedPlayer);
			break;
		}
	}

	public void unknown(ProxiedPlayer sender)
	{
		PartyManager.getMessages().UNKOWN_COMMAND(sender);
	}

	public void help(ProxiedPlayer sender)
	{
		PartyManager.getMessages().HELP(sender);
	}

}