package fr.badblock.bungee.utils.mcjson;

import lombok.Getter;

@Getter
/**
 * 
 * McJson class
 * 
 * @author xMalware
 *
 */
public class McJson {

	/**
	 * Compoonents
	 */
	private McJsonComponent[] components;

	/**
	 * Constructor of McJson
	 * 
	 * @param components
	 */
	public McJson(McJsonComponent... components) {
		// Set the components
		this.components = components;
	}

	@Override
	/**
	 * To string method?
	 */
	public String toString() {
		// Create a string builder
		StringBuilder mcjson = new StringBuilder();
		// Append to the string builder
		mcjson.append("[");

		// For each component
		for (int i = 0; i < components.length; i++) {
			// If it's not the first
			if (i > 0) {
				// Add a separator
				mcjson.append(",");
			}
			// Append the component
			mcjson.append(components[i]);
		}

		// Append?
		mcjson.append("]");

		// Returns the string
		return mcjson.toString();
	}

}