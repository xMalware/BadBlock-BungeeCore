package fr.badblock.bungee.modules.chat;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;

public class AntiSpamChatModule extends ChatModule {

	private String[]		applicableCommands	= new String[]
			{
					"msg",
					"whisper",
					"mp",
					"w",
					"tellraw",
					"tell",
					"minecraft:tell",
					"minecraft:tellraw",
					"minecraft:whisper",
					"minecraft:w",
					"pm",
					"adminchat",
					"ac",
					"chatfriend",
					"cf",
					"r",
					"reply",
					"party",
					"groupe",
					"pa",
					"gr",
					"modo",
					"chatstaff",
					"cs"
			};
	
	private double 			timeBetweenEachMessage = 0L;
	private double 			timeBetweenSameMessage = 0L;

	public AntiSpamChatModule()
	{
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
				DBCollection collection = db.getCollection("customData");
				// Create query
				DBObject query = new BasicDBObject();

				query.put("dataIdentifier", "antispam");

				// Get results
				DBCursor cursor = collection.find(query);

				try
				{
					while (cursor.hasNext())
					{
						DBObject data = cursor.next();

						timeBetweenEachMessage = Double.parseDouble(data.get("timeBetweenEachMessage").toString());
						timeBetweenSameMessage = Double.parseDouble(data.get("timeBetweenSameMessage").toString());

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

		// Remove flood characters
		removeFloodCharacters(event, proxiedPlayer);

		if (event.isCancelled())
		{
			return event;
		}
		
		avoidSameMessages(event, proxiedPlayer, badPlayer);

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
		}

		return false;
	}

	public void removeFloodCharacters(ChatEvent chatEvent, ProxiedPlayer proxiedPlayer)
	{
		int nb = 0;
		Character lastCharacter = null;
		int upperCase = 0;
		String okMessage = "";
		for (Character character : chatEvent.getMessage().toCharArray()) {
			if (lastCharacter != null && character.toString().equals(lastCharacter.toString())
					&& (lastCharacter.toString().equals("!") || lastCharacter.toString().equals("?"))) {
				nb++;
				if (nb >= 2 && (lastCharacter.toString().equals("!") || lastCharacter.toString().equals("?"))) {
					if (nb == 2) {
						if (lastCharacter.toString().equals("w")) {
							continue;
						}
					}
					I19n.sendMessage(proxiedPlayer, "bungee.chat.antispam.flood", null);
					chatEvent.setCancelled(true);
					return ;
				}
			} else {
				nb = 0;
				if (Character.isUpperCase(character)) {
					if (upperCase >= 5) {
						character = Character.toLowerCase(character);
					}
					upperCase++;
				}
			}
			lastCharacter = character;
			okMessage += character.toString();
		}
		chatEvent.setMessage(okMessage);
	}

	public void avoidSameMessages(ChatEvent chatEvent, ProxiedPlayer proxiedPlayer, BadPlayer badPlayer)
	{
		String filteredMessage = applyFilter(chatEvent.getMessage());
		if (badPlayer.getLastMessage() != null && badPlayer.getLastMessageTime() > System.currentTimeMillis()) {
			chatEvent.setCancelled(true);
			I19n.sendMessage(proxiedPlayer, "bungee.chat.antispam.waitbetweenmessages", null);
			return;
		}

		if (badPlayer.getSpamMessages().containsKey(filteredMessage)) {
			long time = badPlayer.getSpamMessages().get(filteredMessage);
			if (time > System.currentTimeMillis()) {
				chatEvent.setCancelled(true);
				I19n.sendMessage(proxiedPlayer, "bungee.chat.antispam.spam", null);
				return;
			}
		}

		badPlayer.getSpamMessages().put(filteredMessage, (long) (System.currentTimeMillis() + timeBetweenSameMessage));
		badPlayer.setLastMessageTime((long) (System.currentTimeMillis() + timeBetweenEachMessage));
		badPlayer.setLastMessage(filteredMessage);
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