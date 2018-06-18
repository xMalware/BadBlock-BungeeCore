package fr.badblock.bungee.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.bungee.BadBungee;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class TechPing extends Thread
{

	private long mongoPing	= -1;
	private long rabbitPing	= -1;

	public TechPing()
	{
		this.start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			BadBungee badBungee = BadBungee.getInstance();
			// Mongo ping
			long pingStart = TimeUtils.time();
			MongoService mongoService = badBungee.getMongoService();
			DBObject ping = new BasicDBObject("ping", "1");
			try {
				mongoService.getDb().command(ping);
				mongoPing = TimeUtils.time() - pingStart;
			} catch (MongoException e) {
				e.printStackTrace();
				mongoPing = -1;
			}
			// Rabbit ping
			RabbitService rabbitService = badBungee.getRabbitService();
			pingStart = TimeUtils.time();
			rabbitService.getConnection().isOpen();
			rabbitPing = TimeUtils.time() - pingStart;
			TimeUtils.sleepInSeconds(2);
		}
	}

}
