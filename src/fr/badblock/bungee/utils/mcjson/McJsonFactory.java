package fr.badblock.bungee.utils.mcjson;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Factory to create a new minecraft JSON message
 *
 * @author RedSpri
 * @see fr.badblock.bungee.utils.mcjson;
 */
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class McJsonFactory {

	/**
	 * Click element
	 * 
	 * @param Set
	 *            the new click element
	 * @return Returns the current click element
	 */
	private McJsonElement clickElement;

	/**
	 * Component list
	 * 
	 * @param Set
	 *            the new component list
	 * @return Returns the current component list
	 */
	private List<McJsonComponent> componentList = new ArrayList<>();

	/**
	 * Hover element
	 * 
	 * @param Set
	 *            the new hover element
	 * @return Returns the current hover element
	 */
	private McJsonElement hoverElement;

	/**
	 * In component
	 * 
	 * @param Set
	 *            the new component state
	 * @return Returns if it's in component
	 */
	private boolean inComponent = false;

	/**
	 * Text element
	 * 
	 * @param Set
	 *            the new text element
	 * @return Returns the current text element
	 */
	private McJsonElement textElement;

	/**
	 * Create a new factory with the content of the first component
	 *
	 * @param firstString
	 *            the content of the first component
	 */
	public McJsonFactory(String firstString) {
		// Init the new component
		initNewComponent(firstString);
	}

	/**
	 * Build the McJson object
	 *
	 * @return the McJson object
	 */
	public McJson build() {
		// If we are in a component
		if (isInComponent()) {
			// Finish the component
			finaliseComponent();
		}

		// Returns a new McJson object
		return new McJson(componentList.toArray(new McJsonComponent[0]));
	}

	/**
	 * if in component creation, finalise the current component create a new
	 * component with his content
	 *
	 * @param componentString
	 *            the content of the new component
	 * @return the factory
	 */
	public McJsonFactory finaliseAndInitNewComponent(String componentString) {
		// If we are in a component
		if (isInComponent()) {
			// So finish the component
			finaliseComponent();
		}

		// Init a new component
		initNewComponent(componentString);

		// Returns the current factory
		return this;
	}

	/**
	 * if in component creation, finalise the current component
	 *
	 * @return the factory
	 */
	public McJsonFactory finaliseComponent() {
		// If we are in a component
		if (isInComponent()) {
			// Get max elements
			int max = 1 + (getClickElement() != null ? 1 : 0) + (getHoverElement() != null ? 1 : 0);
			// Create an element array
			McJsonElement[] elements = new McJsonElement[max];
			// Set the text in the array
			elements[0] = getTextElement();
			// Create an index
			int i = 1;
			// If the click element isn't null
			if (getClickElement() != null) {
				// So insert the click element
				elements[i] = getClickElement();
				// Increment the index
				i++;
			}
			// If the hover element isn't null
			if (getHoverElement() != null) {
				// So insert the hover element
				elements[i] = getHoverElement();
				// Increment the index
				i++;
			}

			// Add the component in the list
			componentList.add(new McJsonComponent(elements));
			// Not in a component anymore
			setInComponent(false);
		}

		// Returns the current factory
		return this;
	}

	/**
	 * if not in component creation, create a new component with his content
	 *
	 * @param componentString
	 *            the content of the component
	 * @return the factory
	 */
	public McJsonFactory initNewComponent(String componentString) {
		// If we are not in a component
		if (!isInComponent()) {
			// Set the text element
			setTextElement(new McJsonElement(McJsonAttribute.TEXT, componentString));
			// Set the click element
			setClickElement(null);
			// Set the hover element
			setHoverElement(null);
			// We are in an component
			setInComponent(true);
		}

		// Returns the current factory
		return this;
	}

	/**
	 * if in component creation, set the click command of the current component the
	 * click command is executed by the player when he click on the component
	 *
	 * @param clickCommand
	 *            the click command to set
	 * @return the factory
	 */
	public McJsonFactory setClickCommand(String clickCommand) {
		// If we are in a component
		if (isInComponent()) {
			// Set the click element
			setClickElement(new McJsonElement(McJsonAttribute.EXECUTE, clickCommand));
		}

		// Returns the current factory
		return this;
	}

	/**
	 * if in component creation, set the click suggest of the current component the
	 * click suggest is written in the player's chat bar when he click on the
	 * component
	 *
	 * @param clickSuggest
	 *            the click suggest to set
	 * @return the factory
	 */
	public McJsonFactory setClickSuggest(String clickSuggest) {
		// If we are in a component
		if (isInComponent()) {
			// Set the click element
			setClickElement(new McJsonElement(McJsonAttribute.SUGGEST, clickSuggest));
		}

		// Returns the current factory
		return this;
	}

	/**
	 * if in component creation, set the click website of the current component the
	 * click website is opened to the player when he click on the component
	 *
	 * @param clickWebsite
	 *            the click website to set
	 * @return the factory
	 */
	public McJsonFactory setClickWebsite(String clickWebsite) {
		// If we are in a component
		if (isInComponent()) {
			// Set the click element
			setClickElement(new McJsonElement(McJsonAttribute.WEBSITE, clickWebsite));
		}

		// Returns the current factory
		return this;
	}

	/**
	 * if in component creation, set the hover text of the current component the
	 * hover text is displayed when the mouse passes on the component
	 *
	 * @param hoverText
	 *            the hover text to set
	 * @return the factory
	 */
	public McJsonFactory setHoverText(String hoverText) {
		// If we are in a component
		if (isInComponent()) {
			// Set the hover element
			setHoverElement(new McJsonElement(McJsonAttribute.HOVER, hoverText));
		}

		// Returns the current factory
		return this;
	}

}