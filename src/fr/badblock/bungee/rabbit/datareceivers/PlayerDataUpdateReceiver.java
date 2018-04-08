package fr.badblock.bungee.rabbit.datareceivers;

import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlayerDataUpdateReceiver
{

	private String			playerName;
	private JsonObject		data;
	
}