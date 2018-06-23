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
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * @author xMalware
 *
 */
public class RNickCommand extends BadCommand {

	private String prefix = "bungee.commands.rnick.";

	/**
	 * Command constructor
	 */
	public RNickCommand() {
		super("rnick", "bungee.command.rnick");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0) {
			I19n.sendMessage(sender, prefix + "usage", null);
			return;
		}

		String name = args[0];

		if (!name.matches("^\\w{3,16}$")) {
			I19n.sendMessage(sender, prefix + "invalidusername", null, name);
			return;
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

				try {
					if (!cursor.hasNext()) {
						I19n.sendMessage(sender, prefix + "nousername", null, name);
						return;
					}

					// Close the cursor
					cursor.close();

					DBObject dbObject = cursor.next();
					String realName = dbObject.get("name").toString();

					I19n.sendMessage(sender, prefix + "found", null, name, realName);
				} catch (Exception error) {
					error.printStackTrace();
					I19n.sendMessage(sender, prefix + "erroroccurred", null);
				}
			}
		});

	}

}