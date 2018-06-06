package fr.badblock.bungee.modules.friends;

import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;

/**
 * FriendList messages
 * 
 * @author xMalware
 *
 */
final class FriendListMessage
{

	/**
	 * Get the prefix
	 * @param code
	 * @return The prefix
	 */
	private String prefix(String code)
	{
		return "objects.friendlist." + code;
	}

	/**
	 * Send the error
	 * @param badPlayer
	 */
	void sendError(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("unknown_error"), null);
	}

	/**
	 * Send unknown error
	 * @param badPlayer
	 * @param player
	 */
	void sendUnknownPlayer(BadPlayer badPlayer, String player)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("unknown_player"), null, player);
	}

	/**
	 * Send operation cancelled
	 * @param badPlayer
	 * @param reason
	 */
	void sendOperationCancelled(BadPlayer badPlayer, String reason)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("operation_cancelled"), null, reason);
	}

	/**
	 * Send can't act on yourself
	 * @param badPlayer
	 */
	void sendCantActOnYourself(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("cant_act_on_yourself"), null);
	}

	/**
	 * Send query selector
	 * @param badPlayer
	 */
	void sendQuerySelector(BadPlayer badPlayer)
	{
		// Get the intro message
		String intro = badPlayer.getTranslatedMessage("querying.selector.intro", null);
		// Get the accept message
		String accept = badPlayer.getTranslatedMessage("querying.selector.accept", null);
		// Accept hover
		String accept_hover = badPlayer.getTranslatedMessage("querying.selector.accept_hover", null);
		// Get the declined message
		String refuse = badPlayer.getTranslatedMessage("querying.selector.refuse", null);
		// Declined hover
		String refuse_hover = badPlayer.getTranslatedMessage("querying.selector.refuse_hover", null);

		// Get the McJson
		McJson json = new McJsonFactory(intro).
				finaliseAndInitNewComponent("\n\n     ").finaliseComponent().
				initNewComponent(accept).setHoverText(accept_hover).setClickCommand("/friend status yes").
				finaliseAndInitNewComponent("     ").finaliseComponent().
				initNewComponent(refuse).setHoverText(refuse_hover).setClickCommand("/friend status no").
				build();

		// Send the message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	
	/**
	 * Send unknown status
	 * @param badPlayer
	 */
	void sendUnknownStatus(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.unknown"), null);
	}

	/**
	 * Send already accepted
	 * @param badPlayer
	 */
	void sendAlreadyAccepted(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.already_accepted"), null);
	}

	/**
	 * Send already declined
	 * @param badPlayer
	 */
	void sendAlreadyRefused(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.already_refused"), null);
	}

	/**
	 * Send now accepted
	 * @param badPlayer
	 */
	void sendNowAccepted(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.now_accepted"), null);
	}

	/**
	 * Send now declined
	 * @param badPlayer
	 */
	void sendNowRefused(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.now_refused"), null);
	}

	/**
	 * Send already friends
	 * @param badPlayer
	 * @param with
	 */
	void sendAlreadyFriend(BadPlayer badPlayer, BadOfflinePlayer with)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.already_friend"), null, with.getName());
	}

	/**
	 * Send request accepted
	 * @param badPlayer
	 * @param requested
	 */
	void sendRequestAccepted(BadOfflinePlayer badPlayer, BadPlayer requested)
	{
		// If the player is online
		if (badPlayer.isOnline())
		{
			// Send the message
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.requested_accept"), null, requested.getName());
		}
	}

	/**
	 * Send accept requester
	 * @param badPlayer
	 * @param requester
	 */
	void sendAcceptRequester(BadPlayer badPlayer, BadOfflinePlayer requester)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.accept_requester"), null, requester.getName());
	}

	/**
	 * Send already requested
	 * @param badPlayer
	 */
	void sendAlreadyRequested(BadPlayer badPlayer)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.already_requested"), null);
	}

	/**
	 * Send request
	 * @param badPlayer
	 * @param requester
	 */
	void sendRequest(BadOfflinePlayer badPlayer, BadPlayer requester)
	{
		// Get the intro
		String intro = badPlayer.getTranslatedMessage("requests.request.intro", null, requester.getName());
		// Get the accept message
		String accept = badPlayer.getTranslatedMessage("requests.request.accept", null);
		// Accept hover
		String accept_hover = badPlayer.getTranslatedMessage("requests.request.accept_hover", null, requester.getName());
		// Get the declined message
		String refuse = badPlayer.getTranslatedMessage("requests.request.refuse", null);
		// Declined hover
		String refuse_hover = badPlayer.getTranslatedMessage("requests.request.refuse_hover", null, requester.getName());

		// Create MCJson object
		McJson json = new McJsonFactory(intro).
				finaliseAndInitNewComponent("\n\n     ").finaliseComponent().
				initNewComponent(accept).setHoverText(accept_hover).setClickCommand("/friend accept " + requester.getName()).
				finaliseAndInitNewComponent("     ").finaliseComponent().
				initNewComponent(refuse).setHoverText(refuse_hover).setClickCommand("/friend accept " + requester.getName()).
				build();

		// If the player is online
		if (badPlayer.isOnline())
		{
			// Send the translated message
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
		}
	}

	/**
	 * Send request received
	 * @param badPlayer
	 * @param receiver
	 */
	void sendRequestReceived(BadPlayer badPlayer, BadOfflinePlayer receiver)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.request_received"), null, receiver.getName());
	}

	/**
	 * Send don't accepte requests
	 * @param badPlayer
	 * @param who
	 */
	void sendDoNotAcceptRequests(BadPlayer badPlayer, BadOfflinePlayer who)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.do_not_accept_requests"), null, who.getName());
	}

	/**
	 * Send declined your request
	 * @param badPlayer
	 * @param who
	 */
	void sendDeclinedYourRequest(BadOfflinePlayer badPlayer, BadPlayer who)
	{
		// If the player is online
		if (badPlayer.isOnline())
		{
			// Send the message
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.declined_your_request"), null, who.getName());
		}
	}

	/**
	 * Send reject request of
	 * @param badPlayer
	 * @param of
	 */
	void sendRejectRequestOf(BadPlayer badPlayer, BadOfflinePlayer of)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.reject_request_of"), null, of.getName());
	}

	/**
	 * Send removed you from friends
	 * @param badPlayer
	 * @param who
	 */
	void sendRemovedYouFromFriends(BadOfflinePlayer badPlayer, BadPlayer who)
	{
		// If the player is online
		if (badPlayer.isOnline())
		{
			// Send the message
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.remove_you_from_friends"), null, who.getName());
		}
	}

	/**
	 * Send now no longer friends
	 * @param badPlayer
	 * @param with
	 */
	void sendNowNoLongerFriends(BadPlayer badPlayer, BadOfflinePlayer with)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.now_no_longer_friend"), null, with.getName());
	}

	/**
	 * Send cancel request to
	 * @param badPlayer
	 * @param to
	 */
	void sendCancelRequestTo(BadPlayer badPlayer, BadOfflinePlayer to)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.cancel_request_to"), null, to.getName());
	}

	/**
	 * Send cancelled request
	 * @param badPlayer
	 * @param who
	 */
	void sendCancelledRequest(BadOfflinePlayer badPlayer, BadPlayer who)
	{
		// If the player is online
		if (badPlayer.isOnline())
		{
			// Send the message
			badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.cancelled_request"), null, who.getName());
		}
	}

	/**
	 * Send no relationship
	 * @param badPlayer
	 * @param with
	 */
	void sendNoRelationship(BadPlayer badPlayer, BadOfflinePlayer with)
	{
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.no_relationship"), null, with.getName());
	}

	/**
	 * Send incorrect page number
	 * @param badPlayer
	 * @param page
	 */
	void sendIncorrectPageNumber(BadPlayer badPlayer, String page)
	{
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.incorrect_page_number"), null, page);
	}

	/**
	 * Send too big page number
	 * @param badPlayer
	 * @param page
	 */
	void sendTooBigPageNumber(BadPlayer badPlayer, Integer page)
	{
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.too_big_page_number"), null, page.toString());
	}

}
