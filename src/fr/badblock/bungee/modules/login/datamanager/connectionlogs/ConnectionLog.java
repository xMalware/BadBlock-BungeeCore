package fr.badblock.bungee.modules.login.datamanager.connectionlogs;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author xMalware
 *
 */
@AllArgsConstructor
@Data
public class ConnectionLog {

	private String username;
	private String lastIp;
	private String date;
	private long timestamp;

	public DBObject toDatabaseObject() {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("username", username);
		dbObject.put("lastIp", lastIp);
		dbObject.put("date", date);
		dbObject.put("timestamp", timestamp);
		return dbObject;
	}

}
