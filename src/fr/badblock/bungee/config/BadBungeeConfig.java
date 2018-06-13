package fr.badblock.bungee.config;

import fr.badblock.api.common.tech.mongodb.setting.MongoSettings;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)

/**
 * 
 * This class is serializable. It contains all necessary configurations for the
 * network
 * 
 * @author xMalware
 *
 */
public class BadBungeeConfig {

	/**
	 * Unique bungee name.
	 * 
	 * @param The
	 *            new bungee name
	 * @return The unique bungee name
	 */
	@Getter
	@Setter
	private String bungeeName;

	/**
	 * Alert prefix.
	 * 
	 * @param The
	 *            new alert prefix
	 * @return Alert prefix
	 * @deprecated i18n
	 */
	@Deprecated
	@Getter
	@Setter
	private String alertPrefix;

	/**
	 * API key from IPHub to check VPNs
	 * 
	 * @param New
	 *            IPHub API key
	 * @return Current IPHub API key
	 */
	@Getter
	@Setter
	private String ipHubApiKey;

	/**
	 * Rabbit settings to connect the network
	 * 
	 * @param New
	 *            Rabbit settings
	 * @return Current rabbit settings
	 */
	@Getter
	@Setter
	private RabbitSettings rabbitSettings;

	/**
	 * Mongo settings to connect the network
	 * 
	 * @param New
	 *            Mongo settings
	 * @return Current mongo settings
	 */
	@Getter
	@Setter
	private MongoSettings mongoSettings;

}