package fr.badblock.bungee.modules.commands.basic.party;

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
				initNewComponent(decline).setHoverText(decline_hover).setClickCommand("/party remove " + byPlayer).finaliseComponent().
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
		I19n.sendMessage(proxiedPlayer, prefix + "tp.playernotingroup", null);
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

}
