package fr.badblock.bungee.modules.commands.nick;

import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * @author xMalware
 *
 */
public class GNickCommand extends BadCommand {

	private String prefix = "bungee.commands.gnick.";

	/**
	 * Command constructor
	 */
	public GNickCommand() {
		super("gnick", "bungee.command.gnick");
		this.setForPlayersOnly(true);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0)
		{
			I19n.sendMessage(sender, prefix + "usage", null);
			return;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		String name = args[0];

		if (name.equalsIgnoreCase("off"))
		{
			if (badPlayer.getNickname() == null || badPlayer.getNickname().isEmpty())
			{
				I19n.sendMessage(sender, prefix + "nonickname", null);
			}
			else
			{
				badPlayer.setNickname(null);
				badPlayer.updateNickname();
				I19n.sendMessage(sender, prefix + "removed", null);
			}
			return;
		}

		if (!name.matches("^\\w{3,16}$"))
		{
			I19n.sendMessage(sender, prefix + "invalidusername", null, name);
			return;
		}

		if (badPlayer.getNickname().equals(name))
		{
			I19n.sendMessage(sender, prefix + "youalreadyhavethisnickname", null, name);
		}

		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Use async mongo
		mongoService.useAsyncMongo(new MongoMethod(mongoService) {
			/**
			 * Use asynchronously
			 */
			@Override
			public void run(MongoService mongoService) {
				// Get the database
				DB db = mongoService.getDb();
				// Get the collection
				DBCollection collection = db.getCollection("players");
				// Create query
				DBObject query = new BasicDBObject();

				query.put("nickname", Pattern.compile(name, Pattern.CASE_INSENSITIVE));

				// Get results
				DBCursor cursor = collection.find(query);

				try
				{
					if (!cursor.hasNext())
					{
						I19n.sendMessage(sender, prefix + "alreadynickname", null, name);
						return;
					}

					// Close the cursor
					cursor.close();
					
					badPlayer.setNickname(name);
					badPlayer.updateNickname();
					
					NickLog nickLog = new NickLog(badPlayer.getName().toLowerCase(), badPlayer.getUniqueId().toString(),
							DateUtils.getHourDate(), System.currentTimeMillis(), name);
					
					// Get the database
					DB logDb = mongoService.getDb();
					// Get the collection
					DBCollection logCollection = logDb.getCollection("players");
					// Create query
					DBObject logQuery = new BasicDBObject();

					logQuery.put("realName", nickLog.getRealName());
					logQuery.put("realUuid", nickLog.getRealUuid());
					logQuery.put("date", nickLog.getDate());
					logQuery.put("timestamp", nickLog.getTimestamp());
					logQuery.put("nickname", nickLog.getNickname());

					logCollection.insert(logQuery);
					
					I19n.sendMessage(sender, prefix + "set", null, name);
				}
				catch (Exception error)
				{
					error.printStackTrace();
					I19n.sendMessage(sender, prefix + "erroroccurred", null);
				}
			}
		});

	}

}