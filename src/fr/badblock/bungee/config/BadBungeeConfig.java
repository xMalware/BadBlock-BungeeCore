package fr.badblock.bungee.config;

import fr.badblock.api.common.tech.mongodb.setting.MongoSettings;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class BadBungeeConfig
{

	private String			bungeeName;
	private String			alertPrefix;
	private String			ipHubApiKey;
	private RabbitSettings	rabbitSettings;
	private MongoSettings	mongoSettings;
	
}
