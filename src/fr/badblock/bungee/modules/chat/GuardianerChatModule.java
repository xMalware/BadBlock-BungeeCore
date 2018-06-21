package fr.badblock.bungee.modules.chat;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.chat.badwords.BadWord;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.DateUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class GuardianerChatModule extends ChatModule {

	private List<String>	words = new ArrayList<>();
	private List<String>	applicableCommands = new ArrayList<>();

	public GuardianerChatModule()
	{
		reload();
	}

	@Override
	public void reload()
	{
		words.clear();
		applicableCommands.clear();
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
				DBCollection collection = db.getCollection("chatFilters");
				// Create query
				DBObject query = new BasicDBObject();

				query.put("dataIdentifier", "guardianer");

				// Get results
				DBCursor cursor = collection.find(query);

				try
				{
					while (cursor.hasNext())
					{
						DBObject data = cursor.next();

						BasicDBList wordlist = (BasicDBList) data.get("words");

						wordlist.forEach(object ->
						{
							String name = object.toString();
							words.add(name);
						});

						BasicDBList applicableCommandlist = (BasicDBList) data.get("applicableCommands");

						applicableCommandlist.forEach(object ->
						{
							String name = object.toString();
							applicableCommands.add(name);
						});

					}
				}
				catch (Exception error)
				{
					error.printStackTrace();
				}

				// Close the cursor
				cursor.close();
			}
		});
	}

	@Override
	public ChatEvent check(ChatEvent event)
	{
		if (event.isCancelled())
		{
			return event;
		}

		if (!(event.getSender() instanceof ProxiedPlayer))
		{
			return event;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (badPlayer.getPunished() != null && badPlayer.getPunished().isMute())
		{
			return event;
		}

		if (proxiedPlayer.hasPermission("chat.bypass"))
		{
			return event;
		}

		if (!isApplicable(event))
		{
			return event;
		}

		// Check badwords
		checkBadwords(event, proxiedPlayer, badPlayer);

		return event;
	}

	private boolean isApplicable(ChatEvent chatEvent)
	{
		String message = chatEvent.getMessage();

		if (message.startsWith("%"))
		{
			return true;
		}

		if (message.startsWith("/"))
		{
			for (String command : applicableCommands)
			{
				if (message.startsWith("/" + command + " "))
				{
					return true;
				}
			}

			return false;
		}

		return true;
	}

	public void checkBadwords(ChatEvent chatEvent, ProxiedPlayer proxiedPlayer, BadPlayer badPlayer)
	{
		String message = chatEvent.getMessage();
		String filteredMessage = applyFilter(message);

		for (String badword : words) {
			if (filteredMessage.contains(badword) || message.contains(badword))
			{
				BadWord badWord = new BadWord("Guardianer", proxiedPlayer.getName(), message, System.currentTimeMillis(), DateUtils.getHourDate(), false, false);

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
						DBCollection collection = db.getCollection("reportmessages");
						// Create query
						DBObject query = badWord.toDatabaseObject();
						collection.insert(query);
					}
				});

				// We send the message and the sender to all concerned
				BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.chat.reportbadword", "bungee.chat.reportbadword",
						null, "Guardianer", badPlayer.getName(), message);

				break;
				// player.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(this.insultError));
			}
		}
	}

	public String applyFilter(String string) {
		string = string.toLowerCase().replaceAll("\\W", "").replace(" ", "").replace("_", "");
		String o = "";
		Character lastCharacter = null;
		for (Character character : string.toCharArray()) {
			if (lastCharacter == null || !lastCharacter.toString().equals(character.toString()))
				o += character.toString();
			lastCharacter = character;
		}
		string = o;
		return o;
	}

}