package fr.badblock.bungee._plugins.objects.friendlist;

public enum FriendListable {
    YES,
    NO;

    public static FriendListable getByString(String string) {
        for (FriendListable friendListable : values()) {
            if (string.equalsIgnoreCase(friendListable.name())) {
                return friendListable;
            }
        }
        return null;
    }
}
