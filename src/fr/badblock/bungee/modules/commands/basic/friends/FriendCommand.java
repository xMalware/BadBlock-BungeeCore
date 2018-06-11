package fr.badblock.bungee.modules.commands.basic.friends;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Add friends, delete friends, view your friends list and much more.
 *
 * No permission is required to execute this command.
 * It is divided into subcommands:
 *  - /friend help/?/aide
 *  - /friend status
 *  - /friend ask/add/invite/request/accept/demander/ajouter/inviter/accepter
 *  - /friend remove/delete/cancel/supprimer/enlever/annuler
 *  - /friend list/show/lister/afficher/montrer
 * 
 * I18n key prefix: 'bungee.commands.friend.'
 * 
 * @author xMalware
 *
 */
public class FriendCommand extends BadCommand
{

	// I18n key prefix
	private String prefix = "bungee.commands.friends.";

	/**
	 * Command constructor
	 */
	public FriendCommand()
	{
		super("friend", null, "friends", "f", "ami", "amis");
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
			help(sender);
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
			// Command execution
			help(sender);
			break;

			// Status of requests
		case "status":
			// Command execution
			status(proxiedPlayer, args);
			break;

			// Add as a friend
		case "ask":
		case "add":
		case "invite":
		case "request":
		case "accept":
		case "demander":
		case "ajouter":
		case "inviter":
		case "accepter":
			// Command execution
			add(proxiedPlayer, args);
			break;

			// Remove a friend
		case "remove":
		case "delete":
		case "cancel":
		case "supprimer":
		case "enlever":
		case "annuler":
			// Command execution
			remove(proxiedPlayer, args);
			break;

			// List of friends
		case "list":
		case "show":
		case "lister":
		case "afficher":
		case "montrer":
			// Command execution
			list(proxiedPlayer, args);
			break;

			// Other subcommand
		default:
			// Command execution
			unknown(sender);
			break;
		}
	}

	/*
	 * Sending a message that the typed subcommand does not exist.
	 */
	private void unknown(CommandSender sender)
	{
		I19n.sendMessage(sender, prefix + "unknown_command", null);
	}

	/**
	 * Sending help to the player
	 * @param sender
	 */
	private void help(CommandSender sender)
	{
		I19n.sendMessage(sender, prefix + "help", null);
	}

	/**
	 * Change the mod state
	 * @param sender
	 * @param args
	 */
	private void status(ProxiedPlayer sender, String[] args)
	{
		// If less than two arguments were given
		if (args.length < 2)
		{
			// So we show him a status selector
			FriendListManager.showStatusSelector(BadPlayer.get(sender));
			// We stop there
			return;
		}
		// We change the authorization status of his friends
		FriendListManager.setQueryable(BadPlayer.get(sender), args[1]);
	}

	/**
	 * Add a player as a friend
	 * @param sender
	 * @param args
	 */
	private void add(ProxiedPlayer sender, String[] args)
	{
		// If less than two arguments were given
		if (args.length < 2)
		{
			// We send him the way to use the command
			I19n.sendMessage(sender, prefix + "add.usage", null);
			// We stop there
			return;
		}
		
		// Sending the request
		FriendListManager.request(sender.getName(), args[1]);
	}

	/**
	 * Remove a friend
	 * @param sender
	 * @param args
	 */
	private void remove(ProxiedPlayer sender, String[] args)
	{
		// If less than two arguments were given
		if (args.length < 2)
		{
			// We send him the way to use the command
			I19n.sendMessage(sender, prefix + "remove.usage", null);
			// We stop there
			return;
		}
		// Removing the friend
		FriendListManager.remove(sender.getName(), args[1]);
	}

	/**
	 * Displaying the player's friends list
	 * @param sender
	 * @param args
	 */
	private void list(ProxiedPlayer sender, String[] args)
	{
		// If less than two arguments were given
		if (args.length < 2)
		{
			// We show him the first page of his friends list
			FriendListManager.showFriendList(BadPlayer.get(sender));
			// We stop there
			return;
		}
		// We show the friend list
		FriendListManager.showFriendList(BadPlayer.get(sender), args[1]);
	}

}
