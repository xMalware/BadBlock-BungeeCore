package fr.badblock.bungee.modules.friends;

import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;

final class FriendListMessage
{

	private String prefix(String code)
	{
		return "objects.friendlist." + code;
	}

	void sendError(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("unknown_error"), null);
	}

	void sendUnknownPlayer(BadPlayer badPlayer, String player)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("unknown_player"), null, player);
	}

	void sendOperationCancelled(BadPlayer badPlayer, String reason)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("operation_cancelled"), null, reason);
	}

	void sendCantActOnYourself(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("cant_act_on_yourself"), null);
	}

	void sendQuerySelector(BadPlayer badPlayer)
	{
		String intro = badPlayer.getTranslatedMessage("querying.selector.intro", null);
		String accept = badPlayer.getTranslatedMessage("querying.selector.accept", null);
		String accept_hover = badPlayer.getTranslatedMessage("querying.selector.accept_hover", null);
		String refuse = badPlayer.getTranslatedMessage("querying.selector.refuse", null);
		String refuse_hover = badPlayer.getTranslatedMessage("querying.selector.refuse_hover", null);

		McJson json = new McJsonFactory(intro).
				finaliseAndInitNewComponent("\n\n     ").finaliseComponent().
				initNewComponent(accept).setHoverText(accept_hover).setClickCommand("/friend status yes").
				finaliseAndInitNewComponent("     ").finaliseComponent().
				initNewComponent(refuse).setHoverText(refuse_hover).setClickCommand("/friend status no").
				build();

		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	void sendUnknownStatus(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.unknown"), null);
	}

	void sendAlreadyAccepted(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.already_accepted"), null);
	}

	void sendAlreadyRefused(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.already_refused"), null);
	}

	void sendNowAccepted(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.now_accepted"), null);
	}

	void sendNowRefused(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.now_refused"), null);
	}

	void sendAlreadyFriend(BadPlayer badPlayer, BadOfflinePlayer with)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.already_friend"), null, with.getName());
	}

	void sendRequestAccepted(BadOfflinePlayer badPlayer, BadPlayer requested)
	{
		if (badPlayer.isOnline())
		{
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.requested_accept"), null, requested.getName());
		}
	}

	void sendAcceptRequester(BadPlayer badPlayer, BadOfflinePlayer requester)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.accept_requester"), null, requester.getName());
	}

	void sendAlreadyRequested(BadPlayer badPlayer)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.already_requested"), null);
	}

	void sendRequest(BadOfflinePlayer badPlayer, BadPlayer requester)
	{
		String intro = badPlayer.getTranslatedMessage("requests.request.intro", null, requester.getName());
		String accept = badPlayer.getTranslatedMessage("requests.request.accept", null);
		String accept_hover = badPlayer.getTranslatedMessage("requests.request.accept_hover", null, requester.getName());
		String refuse = badPlayer.getTranslatedMessage("requests.request.refuse", null);
		String refuse_hover = badPlayer.getTranslatedMessage("requests.request.refuse_hover", null, requester.getName());

		McJson json = new McJsonFactory(intro).
				finaliseAndInitNewComponent("\n\n     ").finaliseComponent().
				initNewComponent(accept).setHoverText(accept_hover).setClickCommand("/friend accept " + requester.getName()).
				finaliseAndInitNewComponent("     ").finaliseComponent().
				initNewComponent(refuse).setHoverText(refuse_hover).setClickCommand("/friend accept " + requester.getName()).
				build();

		if (badPlayer.isOnline())
		{
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
		}
	}

	void sendRequestReceived(BadPlayer badPlayer, BadOfflinePlayer receiver)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.request_received"), null, receiver.getName());
	}

	void sendDoNotAcceptRequests(BadPlayer badPlayer, BadOfflinePlayer who)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.do_not_accept_requests"), null, who.getName());
	}

	void sendDeclinedYourRequest(BadOfflinePlayer badPlayer, BadPlayer who)
	{
		if (badPlayer.isOnline())
		{
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.declined_your_request"), null, who.getName());
		}
	}

	void sendRejectRequestOf(BadPlayer badPlayer, BadOfflinePlayer of)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.reject_request_of"), null, of.getName());
	}

	void sendRemovedYouFromFriends(BadOfflinePlayer badPlayer, BadPlayer who)
	{
		if (badPlayer.isOnline())
		{
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.remove_you_from_friends"), null, who.getName());
		}
	}

	void sendNowNoLongerFriends(BadPlayer badPlayer, BadOfflinePlayer with)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.now_no_longer_friend"), null, with.getName());
	}

	void sendCancelRequestTo(BadPlayer badPlayer, BadOfflinePlayer to)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.cancel_request_to"), null, to.getName());
	}

	void sendCancelledRequest(BadOfflinePlayer badPlayer, BadPlayer who)
	{
		if (badPlayer.isOnline())
		{
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.cancelled_request"), null, who.getName());
		}
	}

	void sendNoRelationship(BadPlayer badPlayer, BadOfflinePlayer with)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.no_relationship"), null, with.getName());
	}

	void sendIncorrectPageNumber(BadPlayer badPlayer, String page)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.incorrect_page_number"), null, page);
	}

	void sendTooBigPageNumber(BadPlayer badPlayer, Integer page)
	{
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.too_big_page_number"), null, page.toString());
	}

}
