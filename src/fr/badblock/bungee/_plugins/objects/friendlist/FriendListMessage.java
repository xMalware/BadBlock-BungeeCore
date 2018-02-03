package fr.badblock.bungee._plugins.objects.friendlist;

import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import fr.toenga.common.utils.i18n.I18n;

class FriendListMessage {
    private String prefix(String code) {
        return "objects.friendlist." + code;
    }

    void ERROR(BadPlayer p) {
        p.sendTranslatedOutgoingJsonMessage(prefix("unknown_error"));
    }

    void SCHIZOPHRENIA_IS_BAD(BadPlayer p) {
        p.sendTranslatedOutgoingJsonMessage("schizophrenia_is_bad");
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

    void ALREADY_ACCEPT(BadPlayer p) {
        p.sendTranslatedOutgoingJsonMessage(prefix("querying.already_accept"));
    }

    void ALREADY_REFUSE(BadPlayer p) {
        p.sendTranslatedOutgoingJsonMessage(prefix("querying.already_refuse"));
    }

    void NOW_ACCEPT(BadPlayer p) {
        p.sendTranslatedOutgoingJsonMessage(prefix("querying.now_accept"));
    }

    void NOW_REFUSE(BadPlayer p) {
        p.sendTranslatedOutgoingJsonMessage(prefix("querying.now_refuse"));
    }

    void ALREADY_FRIEND(BadPlayer p, BadPlayer with) {
        p.sendTranslatedOutgoingJsonMessage(prefix("requests.already_friend"), with.getName());
    }

    void REQUESTED_ACCEPT(BadPlayer p, BadPlayer requested) {
        p.sendTranslatedOutgoingJsonMessage(prefix("requests.requested_accept"), requested.getName());
    }

    void ACCEPT_REQUESTER(BadPlayer p, BadPlayer requester) {
        p.sendTranslatedOutgoingJsonMessage(prefix("requests.accept_requester"), requester.getName());
    }

    void ALREADY_REQUESTED(BadPlayer p) {
        p.sendTranslatedOutgoingJsonMessage(prefix("requests.already_requested"));
    }

    void REQUEST(BadPlayer p, BadPlayer requester) {
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
        p.sendTranslatedOutgoingMCJson(json);
    }

    void REQUEST_RECEIVED(BadPlayer p, BadPlayer receiver) {
        p.sendTranslatedOutgoingJsonMessage(prefix("requests.request_received"), receiver.getName());
    }

    void DONT_ACCEPT_REQUESTS(BadPlayer p, BadPlayer who) {
        p.sendTranslatedOutgoingJsonMessage(prefix("requests.dont_accept_requests"), who.getName());
    }


}
