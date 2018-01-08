package fr.badblock.bungee.utils.mcjson;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create a new minecraft JSON message
 *
 * @author RedSpri
 * @see fr.badblock.bungee.utils.mcjson;
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class McJsonFactory {
    private List<McJsonComponent> componentList = new ArrayList<>();
    private boolean inComponent = false;
    private McJsonElement textElement;
    private McJsonElement clickElement;
    private McJsonElement hoverElement;

    /**
     * Create a new factory with the content of the first component
     *
     * @param firstString the content of the first component
     */
    public McJsonFactory(String firstString) {
        initNewComponent(firstString);
    }

    /**
     * if not in component creation, create a new component with his content
     *
     * @param componentString the content of the component
     * @return the factory
     */
    public McJsonFactory initNewComponent(String componentString) {
        if (!isInComponent()) {
            setTextElement(new McJsonElement(McJsonAttribute.TEXT, componentString));
            setClickElement(null);
            setHoverElement(null);
            setInComponent(true);
        }
        return this;
    }

    /**
     * if in component creation, finalise the current component
     *
     * @return the factory
     */
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

    /**
     * if in component creation, finalise the current component
     * create a new component with his content
     *
     * @param componentString the content of the new component
     * @return the factory
     */
    public McJsonFactory finaliseAndInitNewComponent(String componentString) {
        if (isInComponent()) finaliseComponent();
        initNewComponent(componentString);
        return this;
    }

    /**
     * if in component creation, set the hover text of the current component
     * the hover text is displayed when the mouse passes on the component
     *
     * @param hoverText the hover text to set
     * @return the factory
     */
    public McJsonFactory setHoverText(String hoverText) {
        if (isInComponent()) setHoverElement(new McJsonElement(McJsonAttribute.HOVER, hoverText));
        return this;
    }

    /**
     * if in component creation, set the click command of the current component
     * the click command is executed by the player when he click on the component
     *
     * @param clickCommand the click command to set
     * @return the factory
     */
    public McJsonFactory setClickCommand(String clickCommand) {
        if (isInComponent()) setClickElement(new McJsonElement(McJsonAttribute.EXECUTE, clickCommand));
        return this;
    }

    /**
     * if in component creation, set the click website of the current component
     * the click website is opened to the player when he click on the component
     *
     * @param clickWebsite the click website to set
     * @return the factory
     */
    public McJsonFactory setClickWebsite(String clickWebsite) {
        if (isInComponent()) setClickElement(new McJsonElement(McJsonAttribute.WEBSITE, clickWebsite));
        return this;
    }

    /**
     * if in component creation, set the click suggest of the current component
     * the click suggest is written in the player's chat bar when he click on the component
     *
     * @param clickSuggest the click suggest to set
     * @return the factory
     */
    public McJsonFactory setClickSuggest(String clickSuggest) {
        if (isInComponent()) setClickElement(new McJsonElement(McJsonAttribute.SUGGEST, clickSuggest));
        return this;
    }

    /**
     * Build the McJson object
     *
     * @return the McJson object
     */
    public McJson build() {
        if (isInComponent()) finaliseComponent();
        return new McJson(componentList.toArray(new McJsonComponent[0]));
    }
}
