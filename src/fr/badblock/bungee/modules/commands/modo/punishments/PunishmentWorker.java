package fr.badblock.bungee.modules.commands.modo.punishments;

import net.md_5.bungee.api.CommandSender;

public abstract class PunishmentWorker
{

	public abstract PunishmentType getType();
	
	public String getPermission()
	{
		return "bungee.command.mod." + getType().name().toLowerCase();
	}
	
	public String getPrefix(String name)
	{
		return "bungee.commands.mod." + getType().name().toLowerCase() + "." + name;
	}
	
	public abstract void process(CommandSender sender, String playerName, String reason, boolean isKey, long time);
	
}