package fr.badblock.bungee.modules.commands.modo;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.api.CommandSender;

@AllArgsConstructor
@Data
public abstract class AbstractModCommand {

	private String name;
	private String[] args;

	public abstract void run(CommandSender sender, String[] args);

	public String getPermission()
	{
		return "bungee.command.mod." + getName();
	}
	
	public String getPrefix(String name) {
		return "bungee.commands.mod." + getName() + "." + name;
	}

}
