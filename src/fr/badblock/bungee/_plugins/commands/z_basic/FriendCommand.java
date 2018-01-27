package fr.badblock.bungee._plugins.commands.z_basic;

public class FriendCommand {
    /**
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
}
