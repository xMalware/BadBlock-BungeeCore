package fr.badblock.bungee.modules.party;

import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyMessages
{

	private String prefix = "commands.party.";

	public void sendUnknownMessage(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "unknowncommand", null);
	}
	
	public void sendYouAreNotInParty(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "youarenotinparty", null);
	}
	
	public void sendErrorOccurred(ProxiedPlayer proxiedPlayer, int errorCode)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "erroroccurred", null, errorCode);
	}
	
	public void sendFollow(ProxiedPlayer proxiedPlayer, String followType)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "follow." + followType, null);
	}
	
	public void sendInviteUsage(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "invite.usage", null);
	}

	public void sendInviteAlreadyInParty(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "invite.alreadyinparty", null);
	}
	
	public void sendInviteAlreadyInvited(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "invite.alreadyinvited", null);
	}
	
	public void sendInviteYouInvited(BadPlayer badPlayer, String invitedPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix + "invite.youinvited", null, invitedPlayer);
	}
	
	public void sendInviteYouHaveBeenInvited(BadPlayer otherPlayer, String byPlayer)
	{
		otherPlayer.sendTranslatedOutgoingMessage(prefix + "invite.youhavebeeninvited", null, byPlayer);
	}
	
	public void sendAcceptUsage(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.usage", null);
	}
	
	public void sendAcceptMustBeOnSameServer(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.mustbeonsameserver", null);
	}
	
	public void sendAcceptExpired(ProxiedPlayer proxiedPlayer, String playerInvitation)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.expired", null, playerInvitation);
	}
	
	public void sendAcceptAlreadyInParty(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.alreadyinparty", null, playerName);
	}
	
	public void sendAcceptAccepted(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.accepted", null, playerName);
	}
	
	public void sendRemoveUsage(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.usage", null);
	}
	
	public void sendRemoveYouAreNotInGroup(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.youarenotingroup", null);
	}
	
	public void sendRemovePlayerNotInGroup(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.playernotingroup", null, partyPlayer);
	}

	public void sendRemoveCancelled(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.cancelled", null, partyPlayer);
	}
	
	public void sendRemoveRemoved(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.removed", null, partyPlayer);
	}
	
	public void sendTpUsage(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.usage", null);
	}
	
	public void sendTpYouAreNotInGroup(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.yourenotingroup", null);
	}
	
	public void sendTpPlayerNotInGroup(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.playernotingroup", null);
	}
	
	public void sendTpNotAccepted(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notaccepted", null, partyPlayer);
	}
	
	public void sendTpNotConnected(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notconnected", null, playerName);
	}
	
	public void sendTpUnknownServer(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.unknownserver", null);
	}
	
	public void sendTpNotLogged(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notlogged", null);
	}
	
	public void sendTpTeleported(ProxiedPlayer proxiedPlayer, String serverName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.teleported", null, serverName);
	}
	
	public void sendToggleUsage(BadPlayer badPlayer)
	{
		String intro = badPlayer.getTranslatedMessage(prefix + "toggle.intro", null);
		String with_everyone = badPlayer.getTranslatedMessage(prefix + "toggle.with_everyone", null);
		String with_everyone_hover = badPlayer.getTranslatedMessage(prefix + "toggle.with_everyone_hover", null);
		String with_only_his_friends = badPlayer.getTranslatedMessage(prefix + "toggle.with_only_his_friends", null);
		String with_only_his_friends_hover = badPlayer.getTranslatedMessage(prefix + "toggle.with_only_his_friends_hover", null);
		String with_nobody = badPlayer.getTranslatedMessage(prefix + "toggle.with_nobody", null);
		String with_nobody_hover = badPlayer.getTranslatedMessage(prefix + "toggle.with_nobody_hover", null);
		
		McJson json = new McJsonFactory(intro).
				finaliseAndInitNewComponent("\n\n     ").finaliseComponent().
				initNewComponent(with_everyone).setHoverText(with_everyone_hover).setClickCommand("/party toggle with_everyone").
				finaliseAndInitNewComponent("     ").finaliseComponent().
				initNewComponent(with_only_his_friends).setHoverText(with_only_his_friends_hover).setClickCommand("/party toggle with_only_his_friends").
				finaliseAndInitNewComponent("     ").finaliseComponent().
				initNewComponent(with_nobody).setHoverText(with_nobody_hover).setClickCommand("/party toggle with_nobody").
				build();
		
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}
	
	public void sendToggle(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle." + type, null);
	}
	
	public void sendToggleAlready(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.already", null, I19n.getMessage(proxiedPlayer, prefix + "toggle." + type, null));
	}
	
	public void sendToggleUnknownType(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.unknowntype", null, type);
	}
	
	public void sendToggleWith(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.with", null, I19n.getMessage(proxiedPlayer, prefix + "toggle." + type, null));
	}
	
	public void sendHelp(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "help", null);
	}
	
}
