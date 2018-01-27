package fr.badblock.bungee._plugins.commands.z_basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee._plugins.objects.party.Party;
import fr.badblock.bungee._plugins.objects.party.PartyManager;
import fr.badblock.bungee._plugins.objects.party.PartyPlayer;
import fr.badblock.bungee._plugins.objects.party.PartyPlayerRole;
import fr.badblock.bungee._plugins.objects.party.PartyPlayerState;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.toenga.common.utils.data.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyCommand extends BadCommand
{

	private String prefix = "commands.party.";

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
			help(sender);
			return;
		}
		String subCommand = args[0];
		switch (subCommand)
		{
		// Help
		case "help":
		case "?":
		case "aide":
			help(sender);
			break;
			// Follow
		case "follow":
		case "toggle":
		case "suivi":
		case "suivre":
			follow(sender);
			// Invite
		case "invite":
		case "create":
		case "creer":
		case "inviter":
		case "add":
		case "ajouter":
			invite(proxiedPlayer, args);
			break;
			// Accept
		case "accept":
		case "accepter":
			accept(proxiedPlayer, args);
			break;
			// Remove
		case "remove":
		case "delete":
		case "erase":
		case "supprimer":
		case "kick":
			remove(proxiedPlayer, args);
			// Teleport
		case "tp":
		case "teleport":
		case "connect":
		case "c":
			tp(proxiedPlayer, args);
			// Default
		default:
			unknown(sender);
			break;
		}
	}

	public void unknown(CommandSender sender)
	{
		I19n.sendMessage(sender, prefix + "unknowncommand");
	}

	public void follow(CommandSender sender)
	{
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				if (party == null)
				{
					I19n.sendMessage(sender, prefix + "yourenotinparty");
				}
				else
				{
					if (party.getPlayers() == null)
					{
						I19n.sendMessage(sender, prefix + "erroroccurred", 1);
						return;
					}
					PartyPlayer partyPlayer = party.getPlayers().get(sender.getName());
					if (partyPlayer == null)
					{
						I19n.sendMessage(sender, prefix + "erroroccurred", 2);
						return;
					}
					boolean follow = partyPlayer.isFollow();
					String message = follow ? "disabled" : "enabled";
					I19n.sendMessage(sender, prefix + "follow." + message, sender.getName());
					partyPlayer.setFollow(!follow);
					party.save();
				}
			}

		});
	}

	public void invite(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			I19n.sendMessage(sender, prefix + "invite.usage");
			return;
		}
		String invited = args[1];
		BungeeManager bungeeManager = BungeeManager.getInstance();
		BadPlayer otherPlayer = bungeeManager.getBadPlayer(invited);
		BadPlayer currPlayer = BadPlayer.get(sender);
		if (otherPlayer == null || !currPlayer.getLastServer().equals(otherPlayer.getLastServer()))
		{
			I19n.sendMessage(sender, prefix + "invite.mustbeonsameserver");
			return;
		}
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				if (party == null)
				{
					// Create party
					party = new Party(sender.getName(), invited);
					PartyManager.insert(party);
					_inviteMessage(currPlayer, otherPlayer);
				}
				else
				{
					PartyPlayer partyPlayer = party.getPartyPlayer(otherPlayer.getName());
					if (partyPlayer != null)
					{
						String message = partyPlayer.getState().equals(PartyPlayerState.ACCEPTED) ? "invite.alreadyinparty" : "invite.alreadyinvited";
						currPlayer.sendTranslatedOutgoingMessage(prefix + "invite." + message, otherPlayer.getName());
					}
					else
					{
						party.invite(otherPlayer.getName(), PartyPlayerRole.DEFAULT);
						_inviteMessage(currPlayer, otherPlayer);
					}
				}
			}

		});
	}

	private void _inviteMessage(BadPlayer currPlayer, BadPlayer otherPlayer)
	{
		currPlayer.sendTranslatedOutgoingMessage(prefix + "invite.youinvited", otherPlayer.getName());
		otherPlayer.sendTranslatedOutgoingMessage(prefix + "invite.youhavebeeninvited", currPlayer.getName());
	}

	public void accept(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			I19n.sendMessage(sender, prefix + "accept.usage");
			return;
		}
		String ownerParty = args[1];
		BungeeManager bungeeManager = BungeeManager.getInstance();
		BadPlayer otherPlayer = bungeeManager.getBadPlayer(ownerParty);
		BadPlayer currPlayer = BadPlayer.get(sender);
		if (otherPlayer == null || !currPlayer.getLastServer().equals(otherPlayer.getLastServer()))
		{
			I19n.sendMessage(sender, prefix + "accept.mustbeonsameserver");
			return;
		}
		PartyManager.getParty(otherPlayer.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				if (party == null)
				{
					currPlayer.sendTranslatedOutgoingMessage(prefix + "accept.expired", otherPlayer.getName());
					return;
				}
				PartyPlayer partyPlayer = party.getPartyPlayer(sender.getName());
				if (partyPlayer == null)
				{
					currPlayer.sendTranslatedOutgoingMessage(prefix + "accept.expired", otherPlayer.getName());
					return;
				}
				if (!partyPlayer.getState().equals(PartyPlayerState.WAITING))
				{
					currPlayer.sendTranslatedOutgoingMessage(prefix + "accept.alreadyinparty", otherPlayer.getName());
					return;
				}
				party.accept(sender.getName());
				currPlayer.sendTranslatedOutgoingMessage(prefix + "accept.accepted", otherPlayer.getName());
			}

		});
	}
	
	public void remove(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			I19n.sendMessage(sender, prefix + "remove.usage");
			return;
		}
		String toRemove = args[1];
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				
				// Not in group
				if (party == null)
				{
					I19n.sendMessage(sender, prefix + "remove.yourenotingroup");
					return;
				}
				
				// Player not in group
				PartyPlayer partyPlayer = party.getPartyPlayer(toRemove);
				if (partyPlayer == null)
				{
					I19n.sendMessage(sender, prefix + "remove.playernotingroup");
					return;
				}

				// Remove player
				party.remove(toRemove);
				
				// Send message
				if (partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
				{
					I19n.sendMessage(sender, prefix + "remove.cancelled", partyPlayer.getName());
				}
				else
				{
					I19n.sendMessage(sender, prefix + "remove.removed", partyPlayer.getName());	
				}
				
			}

		});
	}
	
	public void tp(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 2)
		{
			I19n.sendMessage(sender, prefix + "tp.usage");
			return;
		}
		String toTp = args[1];
		BungeeManager bungeeManager = BungeeManager.getInstance();
		PartyManager.getParty(sender.getName(), new Callback<Party>()
		{

			@Override
			public void done(Party party, Throwable error) {
				
				// Sender not in group
				if (party == null)
				{
					I19n.sendMessage(sender, prefix + "tp.yourenotingroup");
					return;
				}
				
				// Player not in group
				PartyPlayer partyPlayer = party.getPartyPlayer(toTp);
				if (partyPlayer == null)
				{
					I19n.sendMessage(sender, prefix + "tp.playernotingroup");
					return;
				}
				
				// Player not accepted yet in group
				if (!partyPlayer.getState().equals(PartyPlayerState.ACCEPTED))
				{
					I19n.sendMessage(sender, prefix + "tp.playernotaccepted");
					return;
				}
				
				// Not connected on the server
				BadPlayer badPlayer = bungeeManager.getBadPlayer(toTp);
				if (badPlayer == null)
				{
					I19n.sendMessage(sender, prefix + "tp.notconnected");
					return;
				}

				// Get last server
				String lastServer = badPlayer.getLastServer();
				
				// Unknown server to teleport (null)
				if (lastServer == null)
				{
					I19n.sendMessage(sender, prefix + "tp.unknownserver");
					return;
				}

				// Unknown server to teleport (empty)
				if (lastServer.isEmpty())
				{
					I19n.sendMessage(sender, prefix + "tp.unknownserver");
					return;
				}

				// The player isn't logged
				if (!badPlayer.isLogged())
				{
					I19n.sendMessage(sender, prefix + "tp.notlogged");
					return;
				}
				
				// Get server
				ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(lastServer);
				
				// Unknown server
				if (serverInfo == null)
				{
					I19n.sendMessage(sender, prefix + "tp.unknownserver");
					return;
				}
				
				// Teleport to server
				sender.connect(serverInfo);
				I19n.sendMessage(sender, prefix + "tp.teleported", toTp);
				
			}

		});
	}

	public void help(CommandSender sender)
	{
		I19n.sendMessage(sender, prefix + "help");
	}

}