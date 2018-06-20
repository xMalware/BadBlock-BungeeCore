package fr.badblock.bungee.modules.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBList;
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

public class AdvertisingChatModule extends ChatModule {

	private List<String>	blockedFirstTopLevelDomains	= new ArrayList<>();
	private List<String>	excludedWebsites			= new ArrayList<>();
	private List<String>	blockedWebsites				= new ArrayList<>();

	public AdvertisingChatModule()
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
				DBCollection collection = db.getCollection("chatFilters");
				// Create query
				DBObject query = new BasicDBObject();

				query.put("dataIdentifier", "advertising");

				// Get results
				DBCursor cursor = collection.find(query);

				try
				{
					while (cursor.hasNext())
					{
						DBObject data = cursor.next();

						BasicDBList blockedFirstTopLevelDomainList = (BasicDBList) data.get("blockedFirstTopLevelDomains");

						blockedFirstTopLevelDomainList.forEach(object ->
						{
							String name = object.toString();
							blockedFirstTopLevelDomains.add(name);
						});

						BasicDBList excludedWebsiteList = (BasicDBList) data.get("excludedWebsites");

						excludedWebsiteList.forEach(object ->
						{
							String name = object.toString();
							excludedWebsites.add(name);
						});

						BasicDBList blockedWebsitesList = (BasicDBList) data.get("blockedWebsites");

						blockedWebsitesList.forEach(object ->
						{
							String name = object.toString();
							blockedWebsites.add(name);
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

		// Check IP
		hasIP(event, proxiedPlayer, badPlayer);

		if (event.isCancelled())
		{
			return event;
		}
		
		// Check blocked website
		hasBlockedWebsite(event, proxiedPlayer, badPlayer);

		if (event.isCancelled())
		{
			return event;
		}

		// Check blocked first top level domain
		hasBlockedFirstTopLevelDomain(event, proxiedPlayer, badPlayer);
		
		return event;
	}

	public void hasIP(ChatEvent chatEvent, ProxiedPlayer proxiedPlayer, BadPlayer badPlayer)
	{
		String message = chatEvent.getMessage();
		String filteredMessage = applyFilter(message);

		if (hasIPv4IP(message) || hasIPv4IP(filteredMessage)) {
			chatEvent.setCancelled(true);
			I19n.sendMessage(proxiedPlayer, "bungee.chat.advertising", null);
			return;
		}
	}

	public void hasBlockedWebsite(ChatEvent chatEvent, ProxiedPlayer proxiedPlayer, BadPlayer badPlayer)
	{
		String message = chatEvent.getMessage();
		String filteredMessage = applyFilter(message);

		for (String blockedWebsite : blockedWebsites)
		{
			if (message.contains(blockedWebsite) || filteredMessage.contains(blockedWebsite)) {
				chatEvent.setCancelled(true);
				I19n.sendMessage(proxiedPlayer, "bungee.chat.advertising", null);
				return;
			}
		}
	}

	public void hasBlockedFirstTopLevelDomain(ChatEvent chatEvent, ProxiedPlayer proxiedPlayer, BadPlayer badPlayer)
	{
		String message = chatEvent.getMessage();
		String filteredMessage = applyFilterN(message);

		for (String blockedFirstTopLevelDomain : blockedFirstTopLevelDomains) {
			if (message.contains(blockedFirstTopLevelDomain) || filteredMessage.contains(blockedFirstTopLevelDomain)) {
				if (message.equalsIgnoreCase(blockedFirstTopLevelDomain) || filteredMessage.equalsIgnoreCase(blockedFirstTopLevelDomain)) {
					chatEvent.setCancelled(true);
					I19n.sendMessage(proxiedPlayer, "bungee.chat.advertising", null);
					return;
				}
				
				String[] splitter = filteredMessage.split(blockedFirstTopLevelDomain);
				String domain = splitter[0];
				boolean cancelled = true;
				
				for (String excludedWebsite : excludedWebsites) {
					if (domain.endsWith(excludedWebsite)) {
						cancelled = false;
						break;
					}
				}
				
				if (cancelled) {
					chatEvent.setCancelled(cancelled);
					I19n.sendMessage(proxiedPlayer, "bungee.chat.advertising", null);
					return;
				}
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

	public String applyFilterN(String string) {
		string = string.toLowerCase().replaceAll("[^.,a-zA-Z]", "").replace(" ", "").replace("_", "");
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


	public static boolean hasIPv4IP(String text) {
		Pattern p = Pattern.compile(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Matcher m = p.matcher(text);
		return m.find();
	}

}