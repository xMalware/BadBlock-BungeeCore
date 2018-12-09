package fr.badblock.bungee.modules.commands.admin;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.api.common.utils.TimeUtils;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.permissions.PermissionsManager;
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * @author xMalware
 *
 */
public class PermissionsCommand extends BadCommand {

	private static String prefix = "bungee.commands.permissions.";

	/**
	 * Command constructor
	 */
	public PermissionsCommand() {
		super("permissions", "bungee.command.permissions", "perms");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0) {
			I19n.sendMessage(sender, prefix + "usage", null);
		}

		String subcommand = args[0];

		switch (subcommand) {
		case "help":
		case "?":
		case "aide":
			I19n.sendMessage(sender, prefix + "usage", null);
			break;
		case "user":
			user(sender, args);
			break;
		default:
			I19n.sendMessage(sender, prefix + "unknowncommand", null);
			break;
		}
	}

	public void user(CommandSender sender, String[] args) {
		if (args.length < 2) {
			I19n.sendMessage(sender, prefix + "user.usage", null);
			return;
		}

		String playerName = args[1];

		if (args.length < 3) {
			I19n.sendMessage(sender, prefix + "user.usage", null);
			return;
		}

		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isFound() || !badOfflinePlayer.isLoaded()) {
			I19n.sendMessage(sender, prefix + "user.unknownplayer", null);
			return;
		}

		String subcommand = args[2];

		switch (subcommand) {
		case "group":
		case "groups":
			userGroup(sender, args, badOfflinePlayer);
			break;
		case "destroy":
		case "d":
			userDestroy(sender, args, badOfflinePlayer);
			break;
		default:
			I19n.sendMessage(sender, prefix + "user.unknowncommand", null);
			break;
		}
	}

	private void userGroup(CommandSender sender, String[] args, BadOfflinePlayer badOfflinePlayer) {
		// perms user <pseudo> groups <something>
		if (args.length < 4) {
			I19n.sendMessage(sender, prefix + "user.group.usage", null);
			return;
		}

		String subcommand = args[3];

		switch (subcommand) {
		case "add":
		case "ajouter":
		case "a":
			userGroupAdd(sender, args, badOfflinePlayer);
			break;
		case "remove":
		case "rm":
		case "del":
		case "delete":
		case "supprimer":
			userGroupRemove(sender, args, badOfflinePlayer);
			break;
		default:
			I19n.sendMessage(sender, prefix + "user.group.unknowncommand", null);
			break;
		}
	}

	public void userGroupAdd(CommandSender sender, String[] args, BadOfflinePlayer badOfflinePlayer) {
		// perms user <pseudo> group add <server> <permission> <time>
		if (args.length < 6) {
			I19n.sendMessage(sender, prefix + "user.groups.add.usage", null);
			return;
		}

		long time = Long.MIN_VALUE;

		if (args.length == 7) {
			try {
				if (args[6].equalsIgnoreCase("-1")) {
					time = -1;
				} else {
					time = Time.MILLIS_SECOND.matchTime(args[6]);
				}
			} catch (Exception error) {
				I19n.sendMessage(sender, prefix + "user.groups.add.errortime", null);
				return;
			}
		} else {
			time = -1L;
		}

		if (time == Long.MIN_VALUE) {
			I19n.sendMessage(sender, prefix + "user.groups.add.errortime", null);
			return;
		}

		String server = args[4];
		String group = args[5];

		Permissible permissible = PermissionsManager.getManager().getGroup(group);

		if (permissible == null) {
			I19n.sendMessage(sender, prefix + "user.groups.add.unknowngroup", null);
			return;
		}

		Map<String, Long> map = null;
		if (!badOfflinePlayer.getPermissions().getGroups().containsKey(server)) {
			map = new HashMap<>();
		} else {
			map = badOfflinePlayer.getPermissions().getGroups().get(server);
		}

		long currentTime = 0L;
		if (map.containsKey(permissible.getName())) {
			currentTime = map.get(permissible.getName());
		}

		if (currentTime == -1 || !TimeUtils.isExpired(currentTime)) {
			I19n.sendMessage(sender, prefix + "user.groups.add.alreadyingroup", null);
			return;
		}

		map.put(permissible.getName(), System.currentTimeMillis() + time);
		badOfflinePlayer.getPermissions().getGroups().put(server, map);
		save(badOfflinePlayer);
		I19n.sendMessage(sender, prefix + "user.groups.add.added", null, badOfflinePlayer.getName(),
				permissible.getName(), server, Time.MILLIS_SECOND.toFrench(time));
	}

	public void userGroupRemove(CommandSender sender, String[] args, BadOfflinePlayer badOfflinePlayer) {
		// perms user <pseudo> group remove <server> <permission>
		if (args.length != 6) {
			I19n.sendMessage(sender, prefix + "user.groups.remove.usage", null);
			return;
		}

		String server = args[4];
		String group = args[5];

		Permissible permissible = PermissionsManager.getManager().getGroup(group);

		if (permissible == null) {
			I19n.sendMessage(sender, prefix + "user.groups.remove.unknowngroup", null);
			return;
		}

		Map<String, Long> map = null;
		if (badOfflinePlayer.getPermissions().getGroups().containsKey(server)) {
			map = badOfflinePlayer.getPermissions().getGroups().get(server);
		}

		if (map == null || !map.containsKey(permissible.getName())) {
			I19n.sendMessage(sender, prefix + "user.groups.remove.notingroup", null);
			return;
		}

		map.remove(permissible.getName());
		badOfflinePlayer.getPermissions().getGroups().put(server, map);
		save(badOfflinePlayer);
		I19n.sendMessage(sender, prefix + "user.groups.remove.removed", null, badOfflinePlayer.getName(),
				permissible.getName(), server);
	}

	public void userDestroy(CommandSender sender, String[] args, BadOfflinePlayer badOfflinePlayer) {
		// perms user <pseudo> destroy
		if (args.length != 3) {
			I19n.sendMessage(sender, prefix + "user.destroy.usage", null);
			return;
		}

		PermissionUser permissions = new PermissionUser();
		// Set default
		HashMap<String, Long> defaultBungeePermissions = new HashMap<>();
		defaultBungeePermissions.put("default", -1L);
		permissions.getGroups().put("bungee", defaultBungeePermissions);

		badOfflinePlayer.setPermissions(permissions);

		save(badOfflinePlayer);
		I19n.sendMessage(sender, prefix + "user.destroy.destroyed", null, badOfflinePlayer.getName());
	}

	public void save(BadOfflinePlayer badOfflinePlayer) {
		if (badOfflinePlayer.isOnline()) {
			BadPlayer targetPlayer = badOfflinePlayer.getOnlineBadPlayer();
			if (targetPlayer == null) {
				try {
					badOfflinePlayer.saveData();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}

			targetPlayer.setPermissions(badOfflinePlayer.getPermissions());
			targetPlayer.sendOnlineTempSyncUpdate();

			try {
				targetPlayer.saveData();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			badOfflinePlayer.saveData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}