package fr.badblock.bungee.modules.login.antibot.checkers;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Queues;

public class UsernameLengthChecker extends AntiBotChecker {

	public Map<Integer, Queue<Long>> characters = new HashMap<>();

	@Override
	public int getId() {
		return 5;
	}

	@Override
	public boolean accept(String username, String address) {
		if (username == null) {
			return true;
		}

		int length = username.length();
		if (!characters.containsKey(length)) {
			Queue<Long> list = Queues.newLinkedBlockingDeque();
			list.add(System.currentTimeMillis());
			characters.put(length, list);
		} else {
			Queue<Long> list = characters.get(length);
			list.add(System.currentTimeMillis());
			characters.put(length, list);

			if (list.size() >= 100) {
				list.poll();
			}

			if (list.size() > 5) {
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				if (averageTime <= 100) {
					return false;
				}
			}

			if (list.size() > 10) {
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				if (averageTime <= 200) {
					return false;
				}
			}

			if (list.size() > 20) {
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				if (averageTime <= 300) {
					return false;
				}
			}

			if (list.size() > 50) {
				long lastTime = list.peek();
				int count = list.size();
				long averageTime = (System.currentTimeMillis() - lastTime) / count;
				if (averageTime <= 500) {
					return false;
				}
			}
		}
		return true;
	}

}
