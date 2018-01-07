package fr.badblock.bungee.utils.mcjson;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
public class McJsonFactory {
    private List<McJsonComponent> componentList = new ArrayList<>();
    private boolean inComponent = false;
    private McJsonElement textElement;
    private McJsonElement clickElement;
    private McJsonElement hoverElement;

    public McJsonFactory(String firstString) {
        initNewComponent(firstString);
    }

    public McJsonFactory initNewComponent(String componentString) {
        if (!isInComponent()) {
            setTextElement(new McJsonElement(McJsonAttribute.TEXT, componentString));
            setClickElement(null);
            setHoverElement(null);
            setInComponent(true);
        }
        return this;
    }

    public McJsonFactory finaliseComponent() {
        if (isInComponent()) {
            McJsonElement[] elements = new McJsonElement[3];
            elements[0] = getTextElement();
            if (getClickElement() != null) elements[1] = getClickElement();
            if (getHoverElement() != null) elements[2] = getHoverElement();
            componentList.add(new McJsonComponent(elements));
            setInComponent(false);
        }
        return this;
    }

    public McJsonFactory finaliseAndInitNewComponent(String componentString) {
        if (isInComponent()) finaliseComponent();
        initNewComponent(componentString);
        return this;
    }

    public McJsonFactory setHoverText(String hoverText) {
        if (isInComponent()) setHoverElement(new McJsonElement(McJsonAttribute.HOVER, hoverText));
        return this;
    }

    public McJsonFactory setClickCommand(String clickCommand) {
        if (isInComponent()) setClickElement(new McJsonElement(McJsonAttribute.EXECUTE, clickCommand));
        return this;
    }

    public McJsonFactory setClickWebsite(String clickWebsite) {
        if (isInComponent()) setClickElement(new McJsonElement(McJsonAttribute.WEBSITE, clickWebsite));
        return this;
    }

    public McJsonFactory setClickSuggest(String clickSuggest) {
        if (isInComponent()) setClickElement(new McJsonElement(McJsonAttribute.SUGGEST, clickSuggest));
        return this;
    }

    public McJson build() {
        if (isInComponent()) finaliseComponent();
        return new McJson(componentList.toArray(new McJsonComponent[0]));
    }
}
