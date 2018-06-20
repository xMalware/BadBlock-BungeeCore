package fr.badblock.bungee.modules.login.antibot.checkers;

import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Queues;

public class UsernameSyllablesChecker extends AntiBotChecker
{

	public Queue<Long> syllables = Queues.newLinkedBlockingDeque();

	@Override
	public int getId()
	{
		return 6;
	}
	
	@Override
	public boolean accept(String username, String address)
	{
		if (username == null)
		{
			return true;
		}
		
		if (countSyllables(username) == 0)
		{
			return true;
		}

		syllables.add(System.currentTimeMillis());
		
		if (syllables.size() >= 5)
		{
			int count = syllables.size();
			long time = System.currentTimeMillis() - syllables.poll();
			long averageTime = time / count;
			if (averageTime <= 1000)
			{
				return false;
			}
		}
		
		return true;
	}
	
	protected int countSyllables(String word)
	{
		int count = 0;
		word = word.toLowerCase();
		if (word.charAt(word.length() - 1) == 'e')
		{
			if (silente(word))
			{
				String newword = word.substring(0, word.length() - 1);
				count += countit(newword);
			}
			else
			{
				count++;
			}
		}
		else {
			count += countit(word);
		}
		return count;
	}

	private int countit(String word)
	{
		int count = 0;
		Pattern splitter = Pattern.compile("[^aeiouy]*[aeiouy]+");
		Matcher m = splitter.matcher(word);
		while (m.find()) {
			count++;
		}
		return count;
	}

	private boolean silente(String word)
	{
		word = word.substring(0, word.length() - 1);

		Pattern yup = Pattern.compile("[aeiouy]");
		Matcher m = yup.matcher(word);
		if (m.find()) {
			return true;
		}
		return false;
	}

}
