package fr.badblock.bungee._plugins.commands.z_basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendCommand extends BadCommand {
    /**
     * friend chat (faudra que je le mette quelque part)
     * FriendList friendList = FriendListManager.getFriendList(sender.getName());
     if (friendList == null) friendList = new FriendList(sender.getName());
     if (friendList.getPlayers().size() < 1) {
     I19n.sendMessage(sender, "commands.chatfriend.nofriends");
     FriendListManager.update(friendList);
     } else {
     final FriendList finalFriendList = friendList;
     BungeeManager.getInstance().targetedTranslatedBroadcast(p -> finalFriendList.isFriend(p.getName()), "commands.chatfriend.message", sender.getName(), message);
     }
     */

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
            default:
                unknown(sender);
                break;
        }
    }

    private void unknown(CommandSender sender) {
        I19n.sendMessage(sender, prefix + "unknowncommand");
    }

    private void help(CommandSender sender) {
        I19n.sendMessage(sender, prefix + "help");
    }

    private void status(ProxiedPlayer sender, String[] args) {

    }
}
