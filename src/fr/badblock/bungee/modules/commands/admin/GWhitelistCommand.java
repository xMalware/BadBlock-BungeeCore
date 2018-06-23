package fr.badblock.bungee.modules.commands.admin;

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
public class GWhitelistCommand extends BadCommand {

	private static String prefix = "bungee.commands.gwhitelist.";

	/**
	 * Command constructor
	 */
	public GWhitelistCommand() {
		super("gwhitelist", "bungee.command.gwhitelist");
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

		String subcommand = args[0];

		switch (subcommand) {
		case "?":
		case "help":
		case "aide":
			I19n.sendMessage(sender, prefix + "usage", null);
			break;
		case "add":
		case "ajouter":
		case "ajout":
			add(sender, args);
			break;
		case "remove":
		case "supprimer":
		case "retirer":
		case "enlever":
		case "delete":
		case "rm":
			remove(sender, args);
			break;
		case "list":
		case "l":
		case "liste":
			list(sender, args);
			break;
		default:
			I19n.sendMessage(sender, prefix + "unknowncommand", null);
			break;
		}
	}

	public void add(CommandSender sender, String[] args) {
		if (args.length != 2) {
			I19n.sendMessage(sender, prefix + "add.usage", null);
			return;
		}

		String username = args[1];

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
				DBCollection collection = db.getCollection("whitelist");
				// Create query
				DBObject query = new BasicDBObject();

				// Put the receiver unique id
				query.put("name", username.toLowerCase());

				// Get results
				DBCursor cursor = collection.find(query);

				try {
					if (cursor.hasNext()) {
						I19n.sendMessage(sender, prefix + "add.alreadyexists", null, username);
					} else {
						// Create query
						DBObject adder = new BasicDBObject();

						// Put the receiver unique id
						adder.put("name", username.toLowerCase());

						collection.insert(adder);

						I19n.sendMessage(sender, prefix + "add.added", null, username);
					}
				} catch (Exception error) {
					error.printStackTrace();
					I19n.sendMessage(sender, prefix + "add.erroroccurred", null, username);
				}

				// Close the cursor
				cursor.close();
			}
		});
	}

	public void remove(CommandSender sender, String[] args) {
		if (args.length != 2) {
			I19n.sendMessage(sender, prefix + "remove.usage", null);
			return;
		}

		String username = args[1];

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
				DBCollection collection = db.getCollection("whitelist");
				// Create query
				DBObject query = new BasicDBObject();

				// Put the receiver unique id
				query.put("name", username.toLowerCase());

				// Get results
				DBCursor cursor = collection.find(query);

				try {
					if (!cursor.hasNext()) {
						I19n.sendMessage(sender, prefix + "remove.notinwhitelist", null, username);
					} else {
						// Create query
						DBObject remover = new BasicDBObject();

						// Put the receiver unique id
						remover.put("name", username.toLowerCase());

						collection.remove(remover);

						I19n.sendMessage(sender, prefix + "remove.removed", null, username);
					}
				} catch (Exception error) {
					error.printStackTrace();
					I19n.sendMessage(sender, prefix + "remove.erroroccurred", null, username);
				}

				// Close the cursor
				cursor.close();
			}
		});
	}

	public void list(CommandSender sender, String[] args) {
		I19n.sendMessage(sender, prefix + "list.intro", null);
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
				DBCollection collection = db.getCollection("whitelist");
				// Create query
				DBObject query = new BasicDBObject();

				// Get results
				DBCursor cursor = collection.find(query);

				boolean c = true;
				try {
					while (cursor.hasNext()) {
						c = false;
						DBObject dbObject = cursor.next();
						String username = dbObject.get("name").toString();
						I19n.sendMessage(sender, prefix + "list.message", null, username);
					}

					if (c) {
						I19n.sendMessage(sender, prefix + "list.nobody", null);
					}
				} catch (Exception error) {
					error.printStackTrace();
					I19n.sendMessage(sender, prefix + "list.erroroccurred", null);
				}

				// Close the cursor
				cursor.close();
			}
		});
	}

}