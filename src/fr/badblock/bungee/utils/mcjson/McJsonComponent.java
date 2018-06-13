package fr.badblock.bungee.utils.mcjson;

import lombok.Getter;

/**
 * 
 * McJson component
 * 
 * @author xMalware
 *
 */
@Getter
public class McJsonComponent {

	// Elements of the component
	private McJsonElement[] elements;

	/**
	 * Component constructor
	 * 
	 * @param elements
	 *            > basic elements
	 */
	public McJsonComponent(McJsonElement... elements) {
		// Set the elements
		this.elements = elements;
	}

	/**
	 * To string method
	 */
	@Override
	public String toString() {
		// Create a StringBuilder
		StringBuilder mcjson = new StringBuilder();

		// Append
		mcjson.append("{");

		// For each element
		for (int i = 0; i < elements.length; i++) {
			// If it's not the first element
			if (i > 0) {
				// So append a 'spacer'
				mcjson.append(",");
			}
			// Append the element
			mcjson.append(elements[i]);
		}

		// Append
		mcjson.append("}");

		// Returns the string
		return mcjson.toString();
	}

}