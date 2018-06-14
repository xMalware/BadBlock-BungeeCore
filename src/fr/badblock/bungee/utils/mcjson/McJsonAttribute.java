package fr.badblock.bungee.utils.mcjson;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * 
 * Attributes of a McJson message
 * 
 * @author xMalware
 * 
 */
@AllArgsConstructor
@Getter
public enum McJsonAttribute {

	/*
	 * Execute attribute
	 */
	EXECUTE("do", "run_command", 1),

	/**
	 * Hover attribute
	 */
	HOVER("hover", "show_text", 2),

	/**
	 * Suggest attribute
	 */
	SUGGEST("insert", "suggest_command", 1),

	/**
	 * Text attribute
	 */
	TEXT("text", "text", 0),

	/**
	 * Website attribute
	 */
	WEBSITE("site", "open_url", 1);

	// Tags
	private String decodetag, mcjsontag;

	// Type
	private int type;

	/**
	 * Get a McJson attribute
	 * 
	 * @param with
	 *            the decode tag
	 * @return the mcJson attribute
	 */
	public McJsonAttribute get(String decodetag) {
		// For each
		for (McJsonAttribute attribute : values()) {
			// If the name is the same
			if (attribute.getDecodetag().equalsIgnoreCase(decodetag)) {
				// Returns the attribute
				return attribute;
			}
		}
		// Returns null
		return null;
	}

}
