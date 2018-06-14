package fr.badblock.bungee.utils.mcjson;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * McJson element
 * 
 * @author xMalware
 *
 */
@Getter
public class McJsonElement {

	/**
	 * Attribute
	 * 
	 * @param Set
	 *            the new attribute
	 * @return Returns the current attribute
	 */
	private McJsonAttribute attribute;

	/**
	 * Value
	 * 
	 * @param Set
	 *            the new value
	 * @return Returns the current value
	 */
	@Setter
	private String value;

	/**
	 * Another element constructor
	 * 
	 * @param attribute
	 */
	public McJsonElement(McJsonAttribute attribute) {
		// use the first constructor
		this(attribute, "");
	}

	/*
	 * Element constructor
	 */
	public McJsonElement(McJsonAttribute attribute, String value) {
		// set the attribute
		this.attribute = attribute;
		// set the value
		this.value = value;
	}

	/**
	 * To String method
	 * 
	 * @return Returns a string
	 */
	@Override
	public String toString() {
		// New StrinBuilder
		StringBuilder mcjson = new StringBuilder();

		// Append the first char
		mcjson.append("\"");

		// If the attribute type is 0
		if (attribute.getType() == 0) {
			// So append the mc json tag
			mcjson.append(attribute.getMcjsontag());
		}
		// If the attribute type is 1
		else if (attribute.getType() == 1) {
			// So append the click event tag
			mcjson.append("clickEvent");
		}
		// If the attribute type is 2
		else if (attribute.getType() == 2) {
			// So append the hover event
			mcjson.append("hoverEvent");
		}

		// Append
		mcjson.append("\":");

		// If the attribute type is higher than 0
		if (attribute.getType() > 0) {
			// Append the action of the tag
			mcjson.append("{\"action\":\"" + attribute.getMcjsontag() + "\",\"value\":");
		}

		// Append
		mcjson.append("\"" + getValue().replace("\"", "\\\"") + "\"");

		// If the attribute is higher than 0
		if (attribute.getType() > 0) {
			// Append
			mcjson.append("}");
		}

		// Returns the string
		return mcjson.toString();
	}

}