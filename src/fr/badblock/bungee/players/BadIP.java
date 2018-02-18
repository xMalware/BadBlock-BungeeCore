package fr.badblock.bungee.players;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class BadIP {
    private String ip;

    public static BadIP fromString(String s) {
        return new BadIP(s);
    }

    @Override
    public String toString() {
        return getIp();
    }
}
