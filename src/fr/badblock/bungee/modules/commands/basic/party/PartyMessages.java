package fr.badblock.bungee.modules.commands.basic.party;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Party messages
 * 
 * @author xMalware
 *
 */
public class PartyMessages
{

	/**
	 * Prefix key
	 * @param Set the prefix key
	 * @return Returns the prefix key
	 */
	@Getter @Setter private String prefix = "bungee.commands.party.";

	/**
	 * Send unknown message
	 * @param The ProxiedPlayer object
	 */
	public void sendUnknownMessage(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "unknowncommand", null);
	}

	/**
	 * Send can't act on yourself
	 * @param The ProxiedPlayer object
	 */
	public void sendCantActOnYourself(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "cantactonyourself", null);
	}

	/**
	 * Send not in party message
	 * @param The ProxiedPlayer object
	 */
	public void sendYouAreNotInParty(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "youarenotinparty", null);
	}

	/**
	 * Send party list admin
	 * @param Can remove the player
	 * @param The ProxiedPlayer object
	 * @param The player name
	 * @param Follow
	 */
	public void sendPartyListAdmin(BadPlayer badPlayer, boolean canRemove, String playerName, boolean follow)
	{
		// Create the state name
		String stateName = "";
		// Create state message
		String state = "";
		// Create state hover
		String state_hover = "";

		// If he's online
		if (BungeeManager.getInstance().hasUsername(playerName))
		{
			// Set as online
			stateName = "online";
		}
		else
		{
			// Set as offline
			stateName = "offline";
		}

		// Set state
		state = badPlayer.getTranslatedMessage(prefix + "list." + stateName, null, playerName);
		// Set state hover
		state_hover = badPlayer.getTranslatedMessage(prefix + "list." + stateName + "_hover", null, playerName);

		// Get the admin message
		String admin = badPlayer.getTranslatedMessage(prefix + "list.admin", null, playerName);
		// Get 'follow_hover' message
		String mfollow = badPlayer.getTranslatedMessage(prefix + "list." + (!follow ? "dont" : "") + "follow", null, playerName);
		// Get 'decline' message
		String mfollow_hover = badPlayer.getTranslatedMessage(prefix + "list." + (!follow ? "dont" : "") + "follow_hover", null, playerName);

		// Create McJson message
		McJsonFactory jsonFactory = new McJsonFactory(state).setHoverText(state_hover).finaliseComponent().initNewComponent(admin).finaliseComponent().
				initNewComponent(mfollow).setHoverText(mfollow_hover).finaliseComponent();

		// If he can remove
		if (canRemove)
		{
			// Create spacer message
			String spacer = badPlayer.getTranslatedMessage(prefix + "list.spacer", null);
			// Create remove message
			String remove = badPlayer.getTranslatedMessage(prefix + "list.remove", null, playerName);
			// Create remove hover message
			String remove_hover = badPlayer.getTranslatedMessage(prefix + "list.remove_hover", null, playerName);
			// Set command
			String command = badPlayer.getName().equalsIgnoreCase(playerName) ? "/party leave" : "/party remove " + playerName;
			// Append to the message
			jsonFactory = jsonFactory.initNewComponent(spacer).finaliseComponent().initNewComponent(remove).setHoverText(remove_hover)
					.setClickCommand(command).finaliseComponent();
		}

		// Build
		McJson json = jsonFactory.build();

		// Send the translated message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send party list modo
	 * @param Can remove the player
	 * @param The ProxiedPlayer object
	 * @param The player name
	 * @param Follow
	 */
	public void sendPartyListModo(BadPlayer badPlayer, boolean canRemove, String playerName, boolean follow)
	{
		// Create the state name
		String stateName = "";
		// Create state message
		String state = "";
		// Create state hover
		String state_hover = "";

		// If he's online
		if (BungeeManager.getInstance().hasUsername(playerName))
		{
			// Set as online
			stateName = "online";
		}
		else
		{
			// Set as offline
			stateName = "offline";
		}

		// Set state
		state = badPlayer.getTranslatedMessage(prefix + "list." + stateName, null, playerName);
		// Set state hover
		state_hover = badPlayer.getTranslatedMessage(prefix + "list." + stateName + "_hover", null, playerName);

		// Get the modo message
		String modo = badPlayer.getTranslatedMessage(prefix + "list.modo", null, playerName);
		// Get 'follow_hover' message
		String mfollow = badPlayer.getTranslatedMessage(prefix + "list.follow", null, playerName);
		// Get 'decline' message
		String mfollow_hover = badPlayer.getTranslatedMessage(prefix + "list.follow_hover", null, playerName);

		// Create McJson message
		McJsonFactory jsonFactory = new McJsonFactory(state).setHoverText(state_hover).finaliseComponent().initNewComponent(modo).finaliseComponent().
				initNewComponent(mfollow).setHoverText(mfollow_hover).finaliseComponent();

		// If he can remove
		if (canRemove)
		{
			// Create spacer message
			String spacer = badPlayer.getTranslatedMessage(prefix + "list.spacer", null);
			// Create remove message
			String remove = badPlayer.getTranslatedMessage(prefix + "list.remove", null, playerName);
			// Create remove hover message
			String remove_hover = badPlayer.getTranslatedMessage(prefix + "list.remove_hover", null, playerName);
			// Set command
			String command = badPlayer.getName().equalsIgnoreCase(playerName) ? "/party leave" : "/party remove " + playerName;
			// Append to the message
			jsonFactory = jsonFactory.initNewComponent(spacer).finaliseComponent().initNewComponent(remove).setHoverText(remove_hover)
					.setClickCommand(command).finaliseComponent();
		}

		// Build
		McJson json = jsonFactory.build();

		// Send the translated message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send party list waiting
	 * @param Can remove the player
	 * @param The ProxiedPlayer object
	 * @param The player name
	 * @param Follow
	 */
	public void sendPartyListWaiting(BadPlayer badPlayer, boolean canRemove, String playerName)
	{
		// Create the state name
		String stateName = "";
		// Create state message
		String state = "";
		// Create state hover
		String state_hover = "";

		// If he's online
		if (BungeeManager.getInstance().hasUsername(playerName))
		{
			// Set as online
			stateName = "online";
		}
		else
		{
			// Set as offline
			stateName = "offline";
		}

		// Set state
		state = badPlayer.getTranslatedMessage(prefix + "list." + stateName, null, playerName);
		// Set state hover
		state_hover = badPlayer.getTranslatedMessage(prefix + "list." + stateName + "_hover", null, playerName);

		// Get the waiting message
		String waiting = badPlayer.getTranslatedMessage(prefix + "list.waiting", null, playerName);

		// Create McJson message
		McJsonFactory jsonFactory = new McJsonFactory(state).setHoverText(state_hover).finaliseComponent().initNewComponent(waiting).finaliseComponent();

		// If he can remove
		if (canRemove)
		{
			// Create spacer message
			String spacer = badPlayer.getTranslatedMessage(prefix + "list.spacer", null);
			// Create remove message
			String remove = badPlayer.getTranslatedMessage(prefix + "list.cancel", null, playerName);
			// Create remove hover message
			String remove_hover = badPlayer.getTranslatedMessage(prefix + "list.cancel_hover", null, playerName);
			// Set command
			String command = badPlayer.getName().equalsIgnoreCase(playerName) ? "/party leave" : "/party remove " + playerName;
			// Append to the message
			jsonFactory = jsonFactory.initNewComponent(spacer).finaliseComponent().initNewComponent(remove).setHoverText(remove_hover)
					.setClickCommand(command).finaliseComponent();
		}

		// Build
		McJson json = jsonFactory.build();

		// Send the translated message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send party list accepted
	 * @param Can remove the player
	 * @param The ProxiedPlayer object
	 * @param The player name
	 * @param Follow
	 */
	public void sendPartyListAccepted(BadPlayer badPlayer, boolean canRemove, String playerName, boolean follow)
	{
		// Create the state name
		String stateName = "";
		// Create state message
		String state = "";
		// Create state hover
		String state_hover = "";

		// If he's online
		if (BungeeManager.getInstance().hasUsername(playerName))
		{
			// Set as online
			stateName = "online";
		}
		else
		{
			// Set as offline
			stateName = "offline";
		}

		// Set state
		state = badPlayer.getTranslatedMessage(prefix + "list." + stateName, null, playerName);
		// Set state hover
		state_hover = badPlayer.getTranslatedMessage(prefix + "list." + stateName + "_hover", null, playerName);

		// Get the accepted message
		String accepted = badPlayer.getTranslatedMessage(prefix + "list.accepted", null, playerName);
		// Get 'follow_hover' message
		String mfollow = badPlayer.getTranslatedMessage(prefix + "list.follow", null, playerName);
		// Get 'decline' message
		String mfollow_hover = badPlayer.getTranslatedMessage(prefix + "list.follow_hover", null, playerName);

		// Create McJson message
		McJsonFactory jsonFactory = new McJsonFactory(state).setHoverText(state_hover).finaliseComponent().initNewComponent(accepted).finaliseComponent().
				initNewComponent(mfollow).setHoverText(mfollow_hover).finaliseComponent();

		// If he can remove
		if (canRemove)
		{
			// Create spacer message
			String spacer = badPlayer.getTranslatedMessage(prefix + "list.spacer", null);
			// Create remove message
			String remove = badPlayer.getTranslatedMessage(prefix + "list.remove", null, playerName);
			// Create remove hover message
			String remove_hover = badPlayer.getTranslatedMessage(prefix + "list.remove_hover", null, playerName);
			// Set command
			String command = badPlayer.getName().equalsIgnoreCase(playerName) ? "/party leave" : "/party remove " + playerName;
			// Append to the message
			jsonFactory = jsonFactory.initNewComponent(spacer).finaliseComponent().initNewComponent(remove).setHoverText(remove_hover)
					.setClickCommand(command).finaliseComponent();
		}

		// Build
		McJson json = jsonFactory.build();

		// Send the translated message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send party list admin
	 * @param The ProxiedPlayer object
	 * @param The error code
	 */
	public void sendIntroList(ProxiedPlayer proxiedPlayer, int count)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "list.intro", null, count);
	}

	/**
	 * Send error occurred
	 * @param The ProxiedPlayer object
	 * @param The error code
	 */
	public void sendErrorOccurred(ProxiedPlayer proxiedPlayer, int errorCode)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "erroroccurred", null, errorCode);
	}

	/**
	 * Send follow
	 * @param The ProxiedPlayer object
	 * @param The Follow Type
	 */
	public void sendFollow(ProxiedPlayer proxiedPlayer, String followType)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "follow." + followType, null);
	}

	/**
	 * Send follow broadcast
	 * @param The ProxiedPlayer object
	 * @param The Follow Type
	 */
	public void sendFollowBroadcast(BadPlayer badPlayer, String playerName, String followType)
	{
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix + "follow.broadcast." + followType, null, playerName);
	}

	/**
	 * Send invite usage
	 * @param The ProxiedPlayer object
	 */
	public void sendInviteUsage(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "invite.usage", null);
	}

	/**
	 * Send invite already in party
	 * @param The ProxiedPlayer object
	 */
	public void sendInviteAlreadyInParty(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "invite.alreadyinparty", null);
	}

	/**
	 * Send invite wait
	 * @param The ProxiedPlayer object
	 */
	public void sendInviteWait(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "invite.wait", null);
	}

	/**
	 * Send invite already invited
	 * @param The ProxiedPlayer object
	 */
	public void sendInviteAlreadyInvited(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "invite.alreadyinvited", null);
	}

	/**
	 * Send invite you invited
	 * @param The BadPlayer object
	 * @param The invited player username
	 */
	public void sendInviteYouInvited(BadPlayer badPlayer, String invitedPlayer)
	{
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix + "invite.youinvited", null, invitedPlayer);
	}

	/**
	 * Send invite you have been invited
	 * @param The BadPlayer object
	 * @param The 'byPlayer' username
	 */
	public void sendInviteYouHaveBeenInvited(BadPlayer otherPlayer, String byPlayer)
	{
		// Get the intro message
		String intro = otherPlayer.getTranslatedMessage(prefix + "invite.receive.intro", null, byPlayer);
		// Get 'accept' message
		String accept = otherPlayer.getTranslatedMessage(prefix + "invite.receive.accept", null, byPlayer);
		// Get 'accept_hover' message
		String accept_hover = otherPlayer.getTranslatedMessage(prefix + "invite.receive.accept_hover", null, byPlayer);
		// Get 'decline' message
		String decline = otherPlayer.getTranslatedMessage(prefix + "invite.receive.decline", null, byPlayer);
		// Get 'decline_hover' message
		String decline_hover = otherPlayer.getTranslatedMessage(prefix + "invite.receive.decline_hover", null, byPlayer);

		// Create McJson message
		McJson json = new McJsonFactory(intro).finaliseComponent().
				initNewComponent(accept).setHoverText(accept_hover).setClickCommand("/party accept " + byPlayer).finaliseComponent().
				initNewComponent(decline).setHoverText(decline_hover).setClickCommand("/party leave").finaliseComponent().
				build();

		// Send the translated message
		otherPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send accept usage
	 * @param The ProxiedPlayer object
	 */
	public void sendAcceptUsage(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "accept.usage", null);
	}

	/**
	 * Send accept must be on same server
	 * @param The ProxiedPlayer object
	 */
	public void sendAcceptMustBeOnSameServer(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "accept.mustbeonsameserver", null);
	}

	/**
	 * Send accept expired
	 * @param The ProxiedPlayer object
	 * @param Player invitation
	 */
	public void sendAcceptExpired(ProxiedPlayer proxiedPlayer, String playerInvitation)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "accept.expired", null, playerInvitation);
	}

	/**
	 * Send accept already in party
	 * @param The ProxiedPlayer object
	 * @param The player username
	 */
	public void sendAcceptAlreadyInParty(ProxiedPlayer proxiedPlayer, String playerName)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "accept.alreadyinparty", null, playerName);
	}

	/**
	 * Send 'accept accepted'
	 * @param The ProxiedPlayer object
	 * @param The player username
	 */
	public void sendAcceptAccepted(ProxiedPlayer proxiedPlayer, String playerName)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "accept.accepted", null, playerName);
	}

	/**
	 * Send remove usage
	 * @param The ProxiedPlayer object
	 */
	public void sendRemoveUsage(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "remove.usage", null);
	}

	/**
	 * Send remove you are not in group
	 * @param The ProxiedPlayer object
	 */
	public void sendRemoveYouAreNotInGroup(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "remove.youarenotingroup", null);
	}

	/**
	 * Send remove player not in group
	 * @param The ProxiedPlayer object
	 * @param Party player username
	 */
	public void sendRemovePlayerNotInGroup(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "remove.playernotingroup", null, partyPlayer);
	}

	/**
	 * Send remove cancelled
	 * @param The ProxiedPlayer object
	 * @param Party player username
	 */
	public void sendRemoveCancelled(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "remove.cancelled", null, partyPlayer);
	}

	/**
	 * Send removed broadcast
	 * @param The ProxiedPlayer object
	 * @param Deleted
	 * @param Deleter
	 */
	public void sendRemovedBroadcast(BadPlayer badPlayer, String deleted, String deleter)
	{
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix + "remove.broadcast", null, deleted, deleter);
	}

	/**
	 * Send leave left
	 * @param The ProxiedPlayer object
	 */
	public void sendLeaveLeft(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "leave.left", null);
	}

	/**
	 * Send leave left other
	 * @param The ProxiedPlayer object
	 * @param Party player username
	 */
	public void sendLeaveLeftOther(BadPlayer badPlayer, String partyPlayer)
	{
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix + "leave.leftother", null, partyPlayer);
	}

	/**
	 * Send owner quit
	 * @param The BadPlayer object
	 * @param Party owner username
	 */
	public void sendOwnerQuit(BadPlayer badPlayer, String partyPlayer)
	{
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix + "leave.ownerquit", null, partyPlayer);
	}

	/**
	 * Send remove 'removed'
	 * @param The ProxiedPlayer object
	 * @param Party player username
	 */
	public void sendRemoveRemoved(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "remove.removed", null, partyPlayer);
	}

	/**
	 * Send tp usage
	 * @param The ProxiedPlayer object
	 */
	public void sendTpUsage(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.usage", null);
	}

	/**
	 * Send tp you're not in group
	 * @param The ProxiedPlayer object
	 */
	public void sendTpYouAreNotInGroup(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.yourenotingroup", null);
	}

	/**
	 * Send tp player not in group
	 * @param The ProxiedPlayer object
	 * @param Party player username
	 */
	public void sendTpPlayerNotInGroup(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.playernotingroup", null, partyPlayer);
	}

	/**
	 * Send tp not accepted
	 * @param The ProxiedPlayer object
	 * @param Party player username
	 */
	public void sendTpNotAccepted(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notaccepted", null, partyPlayer);
	}

	/**
	 * Send tp not connected
	 * @param The ProxiedPlayer object
	 * @param The player username
	 */
	public void sendTpNotConnected(ProxiedPlayer proxiedPlayer, String playerName)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notconnected", null, playerName);
	}

	/**
	 * Send tp unknown server
	 * @param The ProxiedPlayer object
	 */
	public void sendTpUnknownServer(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.unknownserver", null);
	}

	/**
	 * Send tp can't act on yourself
	 * @param The ProxiedPlayer object
	 */
	public void sendTpCantActOnYourself(ProxiedPlayer proxiedPlayer)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.cantactonyourself", null);
	}

	/**
	 * Send tp same server
	 * @param The ProxiedPlayer object
	 */
	public void sendTpSameServer(ProxiedPlayer proxiedPlayer, String playerName)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.sameserver", null, playerName);
	}

	/**
	 * Send tp not logged
	 * @param The ProxiedPlayer object
	 * @param Username
	 */
	public void sendTpNotLogged(ProxiedPlayer proxiedPlayer, String playerName)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notlogged", null, playerName);
	}

	/**
	 * Send tp teleported
	 * @param The ProxiedPlayer object
	 * @param Username
	 */
	public void sendTpTeleported(ProxiedPlayer proxiedPlayer, String serverName)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "tp.teleported", null, serverName);
	}

	/**
	 * Send toggle usage
	 * @param The BadPlayer object
	 */
	public void sendToggleUsage(BadPlayer badPlayer)
	{
		// Get the intro message
		String intro = badPlayer.getTranslatedMessage(prefix + "toggle.set.intro", null);
		// Get 'with_everyone' message
		String with_everyone = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_everyone", null);
		// Get 'with_everyone_hover' message
		String with_everyone_hover = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_everyone_hover", null);
		// Get 'with_only_his_friends' message
		String with_only_his_friends = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_only_his_friends", null);
		// Get 'with_only_his_friends_hover' message
		String with_only_his_friends_hover = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_only_his_friends_hover", null);
		// Get 'with_nobody' message
		String with_nobody = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_nobody", null);
		// Get 'with_nobody_hover' message
		String with_nobody_hover = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_nobody_hover", null);

		// Create McJson message
		McJson json = new McJsonFactory(intro).finaliseComponent().
				initNewComponent(with_everyone).setHoverText(with_everyone_hover).setClickCommand("/party toggle with_everyone").finaliseComponent().
				initNewComponent(with_only_his_friends).setHoverText(with_only_his_friends_hover).setClickCommand("/party toggle with_only_his_friends").finaliseComponent().
				initNewComponent(with_nobody).setHoverText(with_nobody_hover).setClickCommand("/party toggle with_nobody").
				build();

		// Send the translated message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send list
	 * @param The BadPlayer object
	 */
	public void sendList(BadPlayer badPlayer)
	{
		// Get the intro message
		String intro = badPlayer.getTranslatedMessage(prefix + "list.set.intro", null);
		// Get 'with_everyone' message
		String with_everyone = badPlayer.getTranslatedMessage(prefix + "list.set.with_everyone", null);
		// Get 'with_everyone_hover' message
		String with_everyone_hover = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_everyone_hover", null);
		// Get 'with_only_his_friends' message
		String with_only_his_friends = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_only_his_friends", null);
		// Get 'with_only_his_friends_hover' message
		String with_only_his_friends_hover = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_only_his_friends_hover", null);
		// Get 'with_nobody' message
		String with_nobody = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_nobody", null);
		// Get 'with_nobody_hover' message
		String with_nobody_hover = badPlayer.getTranslatedMessage(prefix + "toggle.set.with_nobody_hover", null);

		// Create McJson message
		McJson json = new McJsonFactory(intro).finaliseComponent().
				initNewComponent(with_everyone).setHoverText(with_everyone_hover).setClickCommand("/party toggle with_everyone").finaliseComponent().
				initNewComponent(with_only_his_friends).setHoverText(with_only_his_friends_hover).setClickCommand("/party toggle with_only_his_friends").finaliseComponent().
				initNewComponent(with_nobody).setHoverText(with_nobody_hover).setClickCommand("/party toggle with_nobody").
				build();

		// Send the translated message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send toggle
	 * @param The ProxiedPlayer object
	 * @param type
	 */
	public void sendToggle(ProxiedPlayer proxiedPlayer, String type)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "toggle." + type, null);
	}

	/**
	 * Send toggle already
	 * @param The ProxiedPlayer object
	 * @param The type
	 */
	public void sendToggleAlready(ProxiedPlayer proxiedPlayer, String type)
	{
		// Send message
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.already", new int[] { 0 },
				I19n.getMessage(proxiedPlayer, prefix + "toggle." + type, null));
	}

	/**
	 * Send toggle unknown type
	 * @param The ProxiedPlayer object
	 * @param The type
	 */
	public void sendToggleUnknownType(ProxiedPlayer proxiedPlayer, String type)
	{
		// Send username
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.unknowntype", null, type);
	}

	/**
	 * Send toggle with
	 * @param The ProxiedPlayer object
	 * @param The type
	 */
	public void sendToggleWith(ProxiedPlayer proxiedPlayer, String type)
	{
		// Send username
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.with", null, I19n.getMessage(proxiedPlayer, prefix + "toggle." + type, null));
	}

	/**
	 * Send help
	 * @param The ProxiedPlayer object
	 */
	public void sendHelp(ProxiedPlayer proxiedPlayer)
	{
		// Send username
		I19n.sendMessage(proxiedPlayer, prefix + "help", null);
	}

	/**
	 * Send not enough permissions
	 * @param The ProxiedPlayer object
	 */
	public void sendNotEnoughPermissions(ProxiedPlayer proxiedPlayer)
	{
		// Send username
		I19n.sendMessage(proxiedPlayer, prefix + "remove.notenoughpermissions", null);
	}

}
