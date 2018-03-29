package fr.badblock.bungee._plugins.commands.z_basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee._plugins.objects.friendlist.FriendListManager;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendCommand extends BadCommand {
    private String prefix = "commands.friend.";

    public FriendCommand() {
        super("friend", null, "friends", "ami", "amis");
        this.setForPlayersOnly(true);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
        if (args.length == 0) {
            help(sender);
            return;
        }
        String subCommand = args[0];
        switch (subCommand) {
            case "help":
            case "?":
            case "aide":
                help(sender);
                break;
            case "status":
            case "authorise":
            case "authoriser":
                status(proxiedPlayer, args);
                break;
            case "ask":
            case "add":
            case "invite":
            case "request":
            case "accept":
            case "demander":
            case "ajouter":
            case "inviter":
            case "accepter":
                add(proxiedPlayer, args);
                break;
            case "remove":
            case "delete":
            case "cancel":
            case "supprimer":
            case "enlever":
            case "annuler":
                remove(proxiedPlayer, args);
                break;
            case "list":
            case "show":
            case "lister":
            case "afficher":
            case "montrer":
                list(proxiedPlayer, args);
                break;
            default:
                unknown(sender);
                break;
        }
    }

    private void unknown(CommandSender sender) {
        I19n.sendMessage(sender, prefix + "unknown_command");
    }

    private void help(CommandSender sender) {
        I19n.sendMessage(sender, prefix + "help");
    }

    private void status(ProxiedPlayer sender, String[] args) {
        if (args.length < 2)
            FriendListManager.showStatusSelector(BungeeManager.getInstance().getBadPlayer(sender));
        else FriendListManager.setQueryable(BungeeManager.getInstance().getBadPlayer(sender), args[1]);
    }

    private void add(ProxiedPlayer sender, String[] args) {
        if (args.length < 2) I19n.sendMessage(sender, prefix + "add.usage");
        else FriendListManager.request(sender.getName(), args[1]);
    }

    private void remove(ProxiedPlayer sender, String[] args) {
        if (args.length < 2) I19n.sendMessage(sender, prefix + "remove.usage");
        else FriendListManager.remove(sender.getName(), args[1]);
    }

    private void list(ProxiedPlayer sender, String[] args) {
        if (args.length < 2) FriendListManager.showFriendList(BungeeManager.getInstance().getBadPlayer(sender));
        else FriendListManager.showFriendList(BungeeManager.getInstance().getBadPlayer(sender), args[1]);
    }

}
