package fr.badblock.bungee.modules.commands.basic.friends;

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
final class FriendListMessage {

	/**
	 * Get the prefix
	 * 
	 * @param code
	 * @return The prefix
	 */
	private String prefix(String code) {
		return "bungee.commands.friends." + code;
	}

	/**
	 * Send the error
	 * 
	 * @param badPlayer
	 */
	void sendError(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("unknown_error"), null);
	}

	/**
	 * Send unknown error
	 * 
	 * @param badPlayer
	 * @param player
	 */
	void sendUnknownPlayer(BadPlayer badPlayer, String player) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("unknown_player"), null, player);
	}

	/**
	 * Send operation cancelled
	 * 
	 * @param badPlayer
	 * @param reason
	 */
	void sendOperationCancelled(BadPlayer badPlayer, String reason) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("operation_cancelled"), null, reason);
	}

	/**
	 * Send can't act on yourself
	 * 
	 * @param badPlayer
	 */
	void sendCantActOnYourself(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("cant_act_on_yourself"), null);
	}

	/**
	 * Send query selector
	 * 
	 * @param badPlayer
	 */
	void sendQuerySelector(BadPlayer badPlayer) {
		// Get the intro message
		String intro = badPlayer.getTranslatedMessage(prefix("querying.selector.intro"), null);
		// Get the accept message
		String accept = badPlayer.getTranslatedMessage(prefix("querying.selector.accept"), null);
		// Accept hover
		String accept_hover = badPlayer.getTranslatedMessage(prefix("querying.selector.accept_hover"), null);
		// Get the declined message
		String refuse = badPlayer.getTranslatedMessage(prefix("querying.selector.refuse"), null);
		// Declined hover
		String refuse_hover = badPlayer.getTranslatedMessage(prefix("querying.selector.refuse_hover"), null);

		// Get the McJson
		McJson json = new McJsonFactory(intro).finaliseComponent().initNewComponent(accept).setHoverText(accept_hover)
				.setClickCommand("/friend status yes").finaliseComponent().initNewComponent(refuse)
				.setHoverText(refuse_hover).setClickCommand("/friend status no").build();

		// Send the message
		badPlayer.sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send unknown status
	 * 
	 * @param badPlayer
	 */
	void sendUnknownStatus(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.unknown"), null);
	}

	/**
	 * Send already accepted
	 * 
	 * @param badPlayer
	 */
	void sendAlreadyAccepted(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.already_accepted"), null);
	}

	/**
	 * Send already declined
	 * 
	 * @param badPlayer
	 */
	void sendAlreadyRefused(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.already_refused"), null);
	}

	/**
	 * Send now accepted
	 * 
	 * @param badPlayer
	 */
	void sendNowAccepted(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.now_accepted"), null);
	}

	/**
	 * Send now declined
	 * 
	 * @param badPlayer
	 */
	void sendNowRefused(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("querying.now_refused"), null);
	}

	/**
	 * Send already friends
	 * 
	 * @param badPlayer
	 * @param with
	 */
	void sendAlreadyFriend(BadPlayer badPlayer, BadOfflinePlayer with) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.already_friend"), null, with.getName());
	}

	/**
	 * Send request accepted
	 * 
	 * @param badPlayer
	 * @param requested
	 */
	void sendRequestAccepted(BadOfflinePlayer badPlayer, BadPlayer requested) {
		// If the player is offline
		if (!badPlayer.isOnline()) {
			// So we stop there
			return;
		}

		// Send the message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.requested_accept"), null,
				requested.getName());
	}

	/**
	 * Send accept requester
	 * 
	 * @param badPlayer
	 * @param requester
	 */
	void sendAcceptRequester(BadPlayer badPlayer, BadOfflinePlayer requester) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.accept_requester"), null, requester.getName());
	}

	/**
	 * Send already requested
	 * 
	 * @param badPlayer
	 */
	void sendAlreadyRequested(BadPlayer badPlayer) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.already_requested"), null);
	}

	/**
	 * Send request
	 * 
	 * @param badPlayer
	 * @param requester
	 */
	void sendRequest(BadOfflinePlayer badPlayer, BadPlayer requester) {
		// If the player is offline
		if (!badPlayer.isOnline()) {
			// So we stop there
			return;
		}

		// Get the intro
		String intro = badPlayer.getTranslatedMessage(prefix("requests.request.intro"), null, requester.getName());
		// Get the accept message
		String accept = badPlayer.getTranslatedMessage(prefix("requests.request.accept"), null);
		// Accept hover
		String accept_hover = badPlayer.getTranslatedMessage(prefix("requests.request.accept_hover"), null,
				requester.getName());
		// Get the declined message
		String refuse = badPlayer.getTranslatedMessage(prefix("requests.request.refuse"), null);
		// Declined hover
		String refuse_hover = badPlayer.getTranslatedMessage(prefix("requests.request.refuse_hover"), null,
				requester.getName());

		// Create MCJson object
		McJson json = new McJsonFactory(intro).finaliseComponent().initNewComponent(accept).setHoverText(accept_hover)
				.setClickCommand("/friend accept " + requester.getName()).finaliseComponent().initNewComponent(refuse)
				.setHoverText(refuse_hover).setClickCommand("/friend remove " + requester.getName()).build();

		// Send the translated message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send request received
	 * 
	 * @param badPlayer
	 * @param receiver
	 */
	void sendRequestReceived(BadPlayer badPlayer, BadOfflinePlayer receiver) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.request_received"), null, receiver.getName());
	}

	/**
	 * Send don't accepte requests
	 * 
	 * @param badPlayer
	 * @param who
	 */
	void sendDoNotAcceptRequests(BadPlayer badPlayer, BadOfflinePlayer who) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.do_not_accept_requests"), null, who.getName());
	}

	/**
	 * Send declined your request
	 * 
	 * @param badPlayer
	 * @param who
	 */
	void sendDeclinedYourRequest(BadOfflinePlayer badPlayer, BadPlayer who) {
		// If the player is offline
		if (!badPlayer.isOnline()) {
			// So we stop there
			return;
		}

		// Send the message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.declined_your_request"), null,
				who.getName());
	}

	/**
	 * Send reject request of
	 * 
	 * @param badPlayer
	 * @param of
	 */
	void sendRejectRequestOf(BadPlayer badPlayer, BadOfflinePlayer of) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.reject_request_of"), null, of.getName());
	}

	/**
	 * Send removed you from friends
	 * 
	 * @param badPlayer
	 * @param who
	 */
	void sendRemovedYouFromFriends(BadOfflinePlayer badPlayer, BadPlayer who) {
		// If the player is offline
		if (!badPlayer.isOnline()) {
			// So we stop there
			return;
		}

		// Send the message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.remove_you_from_friends"), null,
				who.getName());
	}

	/**
	 * Send now no longer friends
	 * 
	 * @param badPlayer
	 * @param with
	 */
	void sendNowNoLongerFriends(BadPlayer badPlayer, BadOfflinePlayer with) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.now_no_longer_friend"), null, with.getName());
	}

	/**
	 * Send cancel request to
	 * 
	 * @param badPlayer
	 * @param to
	 */
	void sendCancelRequestTo(BadPlayer badPlayer, BadOfflinePlayer to) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.cancel_request_to"), null, to.getName());
	}

	/**
	 * Send cancelled request
	 * 
	 * @param badPlayer
	 * @param who
	 */
	void sendCancelledRequest(BadOfflinePlayer badPlayer, BadPlayer who) {
		// If the player is offline
		if (!badPlayer.isOnline()) {
			// So we stop there
			return;
		}

		// Send the message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.cancelled_request"), null,
				who.getName());
	}

	/**
	 * Send no relationship
	 * 
	 * @param badPlayer
	 * @param with
	 */
	void sendNoRelationship(BadPlayer badPlayer, BadOfflinePlayer with) {
		// Send the message
		badPlayer.sendTranslatedOutgoingMessage(prefix("requests.no_relationship"), null, with.getName());
	}

	/**
	 * Send incorrect page number
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendIncorrectPageNumber(BadPlayer badPlayer, String page) {
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.incorrect_page_number"), null, page);
	}

	/**
	 * Send too big page number
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendTooBigPageNumber(BadPlayer badPlayer, Integer page) {
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.too_big_page_number"), null, page.toString());
	}

	/**
	 * Send intro list
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendIntroList(BadPlayer badPlayer) {
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.intro"), null);
	}

	/**
	 * Send footer list
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendFooterList(BadPlayer badPlayer) {
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.footer"), null);
	}

	/**
	 * Send offline list
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendOfflineList(BadPlayer badPlayer, BadOfflinePlayer player) {
		String disconnected_state = badPlayer.getTranslatedMessage(prefix("list.disconnected_state"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String disconnected_state_hover = badPlayer.getTranslatedMessage(prefix("list.disconnected_state_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String disconnected_intro = badPlayer.getTranslatedMessage(prefix("list.disconnected_intro"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String disconnected_intro_hover = badPlayer.getTranslatedMessage(prefix("list.disconnected_intro_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String disconnected_space1 = badPlayer.getTranslatedMessage(prefix("list.disconnected_space1"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String disconnected_pm = badPlayer.getTranslatedMessage(prefix("list.disconnected_pm"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String disconnected_pm_hover = badPlayer.getTranslatedMessage(prefix("list.disconnected_pm_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String disconnected_space2 = badPlayer.getTranslatedMessage(prefix("list.disconnected_space2"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String disconnected_party = badPlayer.getTranslatedMessage(prefix("list.disconnected_party"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String disconnected_party_hover = badPlayer.getTranslatedMessage(prefix("list.disconnected_party_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String disconnected_space3 = badPlayer.getTranslatedMessage(prefix("list.disconnected_space3"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String delete = badPlayer.getTranslatedMessage(prefix("list.delete"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String delete_hover = badPlayer.getTranslatedMessage(prefix("list.delete_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		// Create MCJson object
		McJson json = new McJsonFactory(disconnected_state).setHoverText(disconnected_state_hover).finaliseComponent()
				.initNewComponent(disconnected_intro).setHoverText(disconnected_intro_hover).finaliseComponent()
				.initNewComponent(disconnected_space1).finaliseComponent().initNewComponent(disconnected_pm)
				.setHoverText(disconnected_pm_hover).finaliseComponent().initNewComponent(disconnected_space2)
				.finaliseComponent().initNewComponent(disconnected_party).setHoverText(disconnected_party_hover)
				.finaliseComponent().initNewComponent(disconnected_space3).finaliseComponent().initNewComponent(delete)
				.setHoverText(delete_hover).setClickCommand("/friend remove " + player.getName()).build();

		// Send the translated message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send requested list
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendRequestedList(BadPlayer badPlayer, BadOfflinePlayer player) {
		String requested_state = badPlayer.getTranslatedMessage(prefix("list.requested_state"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String requested_state_hover = badPlayer.getTranslatedMessage(prefix("list.requested_state_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String requested_intro = badPlayer.getTranslatedMessage(prefix("list.requested_intro"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String requested_intro_hover = badPlayer.getTranslatedMessage(prefix("list.requested_intro_hover"), null);

		String requested_space1 = badPlayer.getTranslatedMessage(prefix("list.requested_space1"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String requested_info = badPlayer.getTranslatedMessage(prefix("list.requested_info"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String requested_info_hover = badPlayer.getTranslatedMessage(prefix("list.requested_info_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String requested_space2 = badPlayer.getTranslatedMessage(prefix("list.requested_space2"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String cancel = badPlayer.getTranslatedMessage(prefix("list.cancel"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String cancel_hover = badPlayer.getTranslatedMessage(prefix("list.cancel_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		// Create MCJson object
		McJson json = new McJsonFactory(requested_state).setHoverText(requested_state_hover).finaliseComponent()
				.initNewComponent(requested_intro).setHoverText(requested_intro_hover).finaliseComponent()
				.initNewComponent(requested_space1).finaliseComponent().initNewComponent(requested_info)
				.setHoverText(requested_info_hover).finaliseComponent().initNewComponent(requested_space2)
				.finaliseComponent().initNewComponent(cancel).setHoverText(cancel_hover)
				.setClickCommand("/friend cancel " + player.getName()).build();

		// Send the translated message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send waiting list
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendWaitingList(BadPlayer badPlayer, BadOfflinePlayer player) {
		String waiting_state = badPlayer.getTranslatedMessage(prefix("list.waiting_state"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String waiting_state_hover = badPlayer.getTranslatedMessage(prefix("list.waiting_state_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String waiting_intro = badPlayer.getTranslatedMessage(prefix("list.waiting_intro"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String waiting_intro_hover = badPlayer.getTranslatedMessage(prefix("list.waiting_intro_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String waiting_space1 = badPlayer.getTranslatedMessage(prefix("list.waiting_space1"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String waiting_info = badPlayer.getTranslatedMessage(prefix("list.waiting_info"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String waiting_info_hover = badPlayer.getTranslatedMessage(prefix("list.waiting_info_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String waiting_space2 = badPlayer.getTranslatedMessage(prefix("list.waiting_space2"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String accept = badPlayer.getTranslatedMessage(prefix("list.accept"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String accept_hover = badPlayer.getTranslatedMessage(prefix("list.accept_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String waiting_space3 = badPlayer.getTranslatedMessage(prefix("list.waiting_space3"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String decline = badPlayer.getTranslatedMessage(prefix("list.decline"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String decline_hover = badPlayer.getTranslatedMessage(prefix("list.decline_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		// Create MCJson object
		McJson json = new McJsonFactory(waiting_state).setHoverText(waiting_state_hover).finaliseComponent()
				.initNewComponent(waiting_intro).setHoverText(waiting_intro_hover).finaliseComponent()
				.initNewComponent(waiting_space1).finaliseComponent().initNewComponent(waiting_info)
				.setHoverText(waiting_info_hover).finaliseComponent().initNewComponent(waiting_space2)
				.finaliseComponent().initNewComponent(accept).setHoverText(accept_hover)
				.setClickCommand("/friend accept " + player.getName()).finaliseComponent()
				.initNewComponent(waiting_space3).finaliseComponent().initNewComponent(decline)
				.setHoverText(decline_hover).setClickCommand("/friend remove " + player.getName()).build();

		// Send the translated message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send online list
	 * 
	 * @param badPlayer
	 * @param page
	 */
	void sendOnlineList(BadPlayer badPlayer, BadPlayer player) {
		String connected_state = badPlayer.getTranslatedMessage(prefix("list.connected_state"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String connected_state_hover = badPlayer.getTranslatedMessage(prefix("list.connected_state_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String connected_intro = badPlayer.getTranslatedMessage(prefix("list.connected_intro"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String connected_intro_hover = badPlayer.getTranslatedMessage(prefix("list.connected_intro_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String connected_space1 = badPlayer.getTranslatedMessage(prefix("list.connected_space1"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String connected_pm = badPlayer.getTranslatedMessage(prefix("list.connected_pm"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName(), player.getCurrentServer());
		String connected_pm_hover = badPlayer.getTranslatedMessage(prefix("list.connected_pm_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName(), player.getCurrentServer(), player.getVersion());

		String connected_space2 = badPlayer.getTranslatedMessage(prefix("list.connected_space2"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String connected_party = badPlayer.getTranslatedMessage(prefix("list.connected_party"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String connected_party_hover = badPlayer.getTranslatedMessage(prefix("list.connected_party_hover"),
				new int[] { 0 }, player.getRawChatPrefix(), player.getName());

		String connected_space3 = badPlayer.getTranslatedMessage(prefix("list.connected_space3"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		String delete = badPlayer.getTranslatedMessage(prefix("list.delete"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());
		String delete_hover = badPlayer.getTranslatedMessage(prefix("list.delete_hover"), new int[] { 0 },
				player.getRawChatPrefix(), player.getName());

		// Create MCJson object
		McJson json = new McJsonFactory(connected_state).setHoverText(connected_state_hover).finaliseComponent()
				.initNewComponent(connected_intro).setHoverText(connected_intro_hover).finaliseComponent()
				.initNewComponent(connected_space1).finaliseComponent().initNewComponent(connected_pm)
				.setHoverText(connected_pm_hover).setClickSuggest("/msg " + player.getName() + " ").finaliseComponent()
				.initNewComponent(connected_space2).finaliseComponent().initNewComponent(connected_party)
				.setHoverText(connected_party_hover).setClickCommand("/party invite " + player.getName())
				.finaliseComponent().initNewComponent(connected_space3).finaliseComponent().initNewComponent(delete)
				.setHoverText(delete_hover).setClickCommand("/friend remove " + player.getName()).build();

		// Send the translated message
		badPlayer.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
	}

	/**
	 * Send no friends
	 * 
	 * @param badPlayer
	 */
	public void sendNoFriends(BadPlayer badPlayer) {
		// Send message
		badPlayer.sendTranslatedOutgoingMessage(prefix("list.nofriends"), null);
	}

}
