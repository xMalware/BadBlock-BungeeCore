package fr.badblock.bungee.modules.friends;

import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import fr.toenga.common.utils.i18n.I18n;

final class FriendListMessage {
    private String prefix(String code) {
        return "objects.friendlist." + code;
    }

    void ERROR(BadPlayer p) {
        p.sendTranslatedOutgoingMessage(prefix("unknown_error"));
    }

    void UNKNOWN_PLAYER(BadPlayer p, String player) {
        p.sendTranslatedOutgoingMessage(prefix("unknown_player"), player);
    }

    void OPERATION_CANCELLED(BadPlayer p, String reason) {
        p.sendTranslatedOutgoingMessage(prefix("operation_cancelled"), reason);
    }

    void SCHIZOPHRENIA_IS_BAD(BadPlayer p) {
        p.sendTranslatedOutgoingMessage("schizophrenia_is_bad");
    }

    void QUERY_SELECTOR(BadPlayer p) {
        String intro = I18n.getInstance().get(p.getLocale(), "querying.selector.intro")[0];
        String accept = I18n.getInstance().get(p.getLocale(), "querying.selector.accept")[0];
        String accept_hover = I18n.getInstance().get(p.getLocale(), "querying.selector.accept_hover")[0];
        String refuse = I18n.getInstance().get(p.getLocale(), "querying.selector.refuse")[0];
        String refuse_hover = I18n.getInstance().get(p.getLocale(), "querying.selector.refuse_hover")[0];
        McJson json = new McJsonFactory(intro).
                finaliseAndInitNewComponent("\n\n     ").finaliseComponent().
                initNewComponent(accept).setHoverText(accept_hover).setClickCommand("/friend status yes").
                finaliseAndInitNewComponent("     ").finaliseComponent().
                initNewComponent(refuse).setHoverText(refuse_hover).setClickCommand("/friend status no").
                build();
        p.sendTranslatedOutgoingMCJson(json);
    }

    void UNKNOWN_STATUS(BadPlayer p) {
        p.sendTranslatedOutgoingMessage(prefix("querying.unknown"));
    }

    void ALREADY_ACCEPT(BadPlayer p) {
        p.sendTranslatedOutgoingMessage(prefix("querying.already_accept"));
    }

    void ALREADY_REFUSE(BadPlayer p) {
        p.sendTranslatedOutgoingMessage(prefix("querying.already_refuse"));
    }

    void NOW_ACCEPT(BadPlayer p) {
        p.sendTranslatedOutgoingMessage(prefix("querying.now_accept"));
    }

    void NOW_REFUSE(BadPlayer p) {
        p.sendTranslatedOutgoingMessage(prefix("querying.now_refuse"));
    }

    void ALREADY_FRIEND(BadPlayer p, BadOfflinePlayer with) {
        p.sendTranslatedOutgoingMessage(prefix("requests.already_friend"), with.getName());
    }

    void REQUESTED_ACCEPT(BadOfflinePlayer p, BadPlayer requested) {
        if (p.isOnline())
            p.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.requested_accept"), requested.getName());
    }

    void ACCEPT_REQUESTER(BadPlayer p, BadOfflinePlayer requester) {
        p.sendTranslatedOutgoingMessage(prefix("requests.accept_requester"), requester.getName());
    }

    void ALREADY_REQUESTED(BadPlayer p) {
        p.sendTranslatedOutgoingMessage(prefix("requests.already_requested"));
    }

    void REQUEST(BadOfflinePlayer p, BadPlayer requester) {
        String intro = I18n.getInstance().get(p.getLocale(), "requests.request.intro", requester.getName())[0];
        String accept = I18n.getInstance().get(p.getLocale(), "requests.request.accept")[0];
        String accept_hover = I18n.getInstance().get(p.getLocale(), "requests.request.accept_hover", requester.getName())[0];
        String refuse = I18n.getInstance().get(p.getLocale(), "requests.request.refuse")[0];
        String refuse_hover = I18n.getInstance().get(p.getLocale(), "requests.request.refuse_hover", requester.getName())[0];
        McJson json = new McJsonFactory(intro).
                finaliseAndInitNewComponent("\n\n     ").finaliseComponent().
                initNewComponent(accept).setHoverText(accept_hover).setClickCommand("/friend accept " + requester.getName()).
                finaliseAndInitNewComponent("     ").finaliseComponent().
                initNewComponent(refuse).setHoverText(refuse_hover).setClickCommand("/friend accept " + requester.getName()).
                build();
        if (p.isOnline()) p.getOnlineBadPlayer().sendTranslatedOutgoingMCJson(json);
    }

    void REQUEST_RECEIVED(BadPlayer p, BadOfflinePlayer receiver) {
        p.sendTranslatedOutgoingMessage(prefix("requests.request_received"), receiver.getName());
    }

    void DO_NOT_ACCEPT_REQUESTS(BadPlayer p, BadOfflinePlayer who) {
        p.sendTranslatedOutgoingMessage(prefix("requests.do_not_accept_requests"), who.getName());
    }

    void DECLINED_YOUR_REQUEST(BadOfflinePlayer p, BadPlayer who) {
        if (p.isOnline())
            p.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.declined_your_requested"), who.getName());
    }

    void REJECT_REQUEST_OF(BadPlayer p, BadOfflinePlayer of) {
        p.sendTranslatedOutgoingMessage(prefix("requests.reject_request_of"), of.getName());
    }

    void REMOVED_YOU_FROM_FRIENDS(BadOfflinePlayer p, BadPlayer who) {
        if (p.isOnline())
            p.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.remove_you_from_friends"), who.getName());
    }

    void NOW_NO_LONGER_FRIEND(BadPlayer p, BadOfflinePlayer with) {
        p.sendTranslatedOutgoingMessage(prefix("requests.now_no_longer_friend"), with.getName());
    }

    void CANCEL_REQUEST_TO(BadPlayer p, BadOfflinePlayer to) {
        p.sendTranslatedOutgoingMessage(prefix("requests.cancel_request_to"), to.getName());
    }

    void CANCELLED_REQUEST(BadOfflinePlayer p, BadPlayer who) {
        if (p.isOnline())
            p.getOnlineBadPlayer().sendTranslatedOutgoingMessage(prefix("requests.cancelled_request"), who.getName());
    }

    void NO_RELATIONSHIP(BadPlayer p, BadOfflinePlayer with) {
        p.sendTranslatedOutgoingMessage(prefix("requests.no_relationship"), with.getName());
    }

    void INCORRECT_PAGE_NUMBER(BadPlayer p, String page) {
        p.sendTranslatedOutgoingMessage(prefix("list.incorrect_page_number"), page);
    }

    void TOO_BIG_PAGE_NUMBER(BadPlayer p, Integer page) {
        p.sendTranslatedOutgoingMessage(prefix("list.too_big_page_number"), page.toString());
    }

}
