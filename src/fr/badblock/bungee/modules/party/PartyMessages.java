package fr.badblock.bungee.modules.party;

import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import fr.toenga.common.utils.i18n.I18n;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyMessages
{

	private String prefix = "commands.party.";

	public void UNKOWN_COMMAND(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "unknowncommand", null);
	}
	
	public void YOU_RE_NOT_IN_PARTY(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "yourenotinparty", null);
	}
	
	public void ERROR_OCCURRED(ProxiedPlayer proxiedPlayer, int errorCode)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "erroroccurred", null, errorCode);
	}
	
	public void FOLLOW(ProxiedPlayer proxiedPlayer, String followType)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "follow." + followType, null);
	}
	
	public void INVITE_USAGE(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "invite.usage", null);
	}

	public void INVITE_ALREADYINPARTY(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "invite.alreadyinparty", null);
	}
	
	public void INVITE_ALREADYINVITED(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "invite.alreadyinvited", null);
	}
	
	public void INVITE_YOUINVITED(BadPlayer badPlayer, String invitedPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix + "invite.youinvited", null, invitedPlayer);
	}
	
	public void INVITE_YOUHAVEBEENINVITED(BadPlayer otherPlayer, String byPlayer)
	{
		otherPlayer.sendTranslatedOutgoingMessage(prefix + "invite.youhavebeeninvited", null, byPlayer);
	}
	
	public void ACCEPT_USAGE(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.usage", null);
	}
	
	public void ACCEPT_MUSTBEONSAMESERVER(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.mustbeonsameserver", null);
	}
	
	public void ACCEPT_EXPIRED(ProxiedPlayer proxiedPlayer, String playerInvitation)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.expired", null, playerInvitation);
	}
	
	public void ACCEPT_ALREADYINPARTY(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.alreadyinparty", null, playerName);
	}
	
	public void ACCEPT_ACCEPTED(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "accept.accepted", null, playerName);
	}
	
	public void REMOVE_USAGE(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.usage", null);
	}
	
	public void REMOVE_YOURENOTINGROUP(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.yourenotingroup", null);
	}
	
	public void REMOVE_PLAYERNOTINGROUP(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.playernotingroup", null, partyPlayer);
	}

	public void REMOVE_CANCELLED(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.cancelled", null, partyPlayer);
	}
	
	public void REMOVE_REMOVED(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "remove.removed", null, partyPlayer);
	}
	
	public void TP_USAGE(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.usage", null);
	}
	
	public void TP_YOURENOTINGROUP(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.yourenotingroup", null);
	}
	
	public void TP_PLAYERNOTINGROUP(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.playernotingroup", null);
	}
	
	public void TP_NOTACCEPTED(ProxiedPlayer proxiedPlayer, String partyPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notaccepted", null, partyPlayer);
	}
	
	public void TP_NOTCONNECTED(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notconnected", null, playerName);
	}
	
	public void TP_UNKNOWNSERVER(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.unknownserver", null);
	}
	
	public void TP_NOTLOGGED(ProxiedPlayer proxiedPlayer, String playerName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.notlogged", null);
	}
	
	public void TP_TELEPORTED(ProxiedPlayer proxiedPlayer, String serverName)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "tp.teleported", null, serverName);
	}
	
	public void TOGGLE_USAGE(BadPlayer badPlayer)
	{
		String intro = I18n.getInstance().get(badPlayer.getLocale(), prefix + "toggle.intro")[0];
		String with_everyone = I18n.getInstance().get(badPlayer.getLocale(), prefix + "toggle.with_everyone")[0];
		String with_everyone_hover = I18n.getInstance().get(badPlayer.getLocale(), prefix + "toggle.with_everyone_hover")[0];
		String with_only_his_friends = I18n.getInstance().get(badPlayer.getLocale(), prefix + "toggle.with_only_his_friends")[0];
		String with_only_his_friends_hover = I18n.getInstance().get(badPlayer.getLocale(), prefix + "toggle.with_only_his_friends_hover")[0];
		String with_nobody = I18n.getInstance().get(badPlayer.getLocale(), prefix + "toggle.with_nobody")[0];
		String with_nobody_hover = I18n.getInstance().get(badPlayer.getLocale(), prefix + "toggle.with_nobody_hover")[0];
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
	
	public void TOGGLE(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle." + type, null);
	}
	
	public void TOGGLE_ALREADY(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.already", null, I19n.getMessage(proxiedPlayer, prefix + "toggle." + type, null));
	}
	
	public void TOGGLE_UNKNOWNTYPE(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.unknowntype", null, type);
	}
	
	public void TOGGLE_WITH(ProxiedPlayer proxiedPlayer, String type)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "toggle.with", null, I19n.getMessage(proxiedPlayer, prefix + "toggle." + type, null));
	}
	
	public void HELP(ProxiedPlayer proxiedPlayer)
	{
		I19n.sendMessage(proxiedPlayer, prefix + "help", null);
	}
	
}
