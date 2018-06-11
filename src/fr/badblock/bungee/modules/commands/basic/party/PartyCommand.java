package fr.badblock.bungee.modules.commands.basic.party;

import fr.badblock.bungee.modules.commands.BadCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Allows to create groups to be able to play together, between friends
 * 
 * List of subcommands:
 *  - /party help/?/aide
 *  - /party follow/suivi/suivre
 *  - /party toggle
 *  - /party invite/create/creer/inviter/add/ajouter
 *  - /party accept/accepter
 *  - /party remove/delete/erase/supprimer/kick
 *  - /party tp/teleport/connect/c
 * 
 * @author xMalware
 *
 */
public class PartyCommand extends BadCommand
{
	
	/**
	 * Command constructor
	 */
	public PartyCommand()
	{
		super("party", null, "groupe", "pa", "gr");
		// Allow access to the command for players only
		this.setForPlayersOnly(true);
	}
	
	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// We get the player from the sender
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		// If no argument has been entered
		if (args.length == 0)
		{
			// We give him help.
			help(proxiedPlayer);
			// We stop there.
			return;
		}

		// We're getting the subcommand
		String subCommand = args[0];

		// We're checking the subcommand
		switch (subCommand)
		{
		
		// Request for help
		case "help":
		case "?":
		case "aide":
			help(proxiedPlayer);
			break;
			
		// Follow the party
		case "follow":
		case "suivi":
		case "suivre":
			PartyManager.follow(proxiedPlayer);
			break;
			
		// Toggle parties
		case "toggle":
			PartyManager.toggle(proxiedPlayer, args);
			break;
		
		// Invite a player
		case "invite":
		case "create":
		case "creer":
		case "inviter":
		case "add":
		case "ajouter":
			PartyManager.invite(proxiedPlayer, args);
			break;
			
		// Accept a player
		case "accept":
		case "accepter":
			PartyManager.accept(proxiedPlayer, args);
			break;
			
		// Remove a player
		case "remove":
		case "delete":
		case "erase":
		case "supprimer":
		case "kick":
			PartyManager.remove(proxiedPlayer, args);
			break;
			
		// Set as modo
		case "modo":
		case "mod":
			PartyManager.modo(proxiedPlayer, args);
			break;
			
		// List
		case "list":
		case "liste":
			PartyManager.list(proxiedPlayer);
			break;
			
		// Leave
		case "leave":
		case "quit":
		case "quitter":
			PartyManager.leave(proxiedPlayer, args);
			break;
		
		// Teleport a player
		case "tp":
		case "teleport":
		case "connect":
		case "c":
			PartyManager.tp(proxiedPlayer, args);
			break;

		// Unknown subcommand
		default:
			unknown(proxiedPlayer);
			break;
		}
	}

	/**
	 * Unknown subcommand
	 * @param sender
	 */
	public void unknown(ProxiedPlayer sender)
	{
		// We tell him this command doesn't exist.
		PartyManager.getMessages().sendUnknownMessage(sender);
	}

	/**
	 * Sending help
	 * @param sender
	 */
	public void help(ProxiedPlayer sender)
	{
		// We give him the list of subcommands
		PartyManager.getMessages().sendHelp(sender);
	}

}