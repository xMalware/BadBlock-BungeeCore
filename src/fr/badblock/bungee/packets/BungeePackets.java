package fr.badblock.bungee.packets;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.protocol.*;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import fr.badblock.bungee.packets.listener.ConnectionListener;
import fr.badblock.bungee.packets.listener.PacketEventListener;
import fr.badblock.bungee.packets.packets.Packet;
import fr.badblock.bungee.packets.packets.protocol.game.client.OpenWindowPacket;
import fr.badblock.bungee.packets.packets.protocol.game.client.SetWindowContentsPacket;
import fr.badblock.bungee.packets.packets.protocol.game.client.SetWindowSlotPacket;
import fr.badblock.bungee.packets.packets.protocol.game.server.ClickWindowPacket;
import fr.badblock.bungee.packets.packets.protocol.game.shared.CloseWindowPacket;
import fr.badblock.bungee.packets.packets.protocol.game.shared.ConfirmTransactionPacket;
import fr.badblock.bungee.packets.util.ProtocolMapping;

public class BungeePackets {

    public static BungeePackets instance;

    /**
     * The logger for BungeeNet
     */
    public static Logger logger;

    /**
     * Registers a packet in the Bungeecord packet registries
     * @param protocol The protocol to register the packet at
     * @param packetClazz The class of the packet
     * @param mappings The mappings that are related to this packet
     * @throws ReflectiveOperationException When an exception occurs in registering the packet due to reflection-related errors
     */
    public static void registerPacket(Protocol.DirectionData protocol, Class<? extends Packet> packetClazz, ProtocolMapping... mappings) throws ReflectiveOperationException {
        Method mapMethod = protocol.getClass().getDeclaredMethod("registerPacket", Class.class, Class.forName("[Lnet.md_5.bungee.protocol.Protocol$ProtocolMapping;"));
        mapMethod.setAccessible(true);
        mapMethod.invoke(protocol, packetClazz, ProtocolMapping.toBungeecordMappingList(mappings));
    }

    /**
     * Registers the "default" packets
     */
    private static void registerPackets() throws ReflectiveOperationException {
        
        registerPacket(Protocol.GAME.TO_CLIENT, ConfirmTransactionPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x11));
        registerPacket(Protocol.GAME.TO_CLIENT, CloseWindowPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x12));
        registerPacket(Protocol.GAME.TO_CLIENT, OpenWindowPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x13));
        registerPacket(Protocol.GAME.TO_CLIENT, SetWindowContentsPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x14));
        registerPacket(Protocol.GAME.TO_CLIENT, SetWindowSlotPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x16));

        registerPacket(Protocol.GAME.TO_SERVER, ConfirmTransactionPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x05));
        registerPacket(Protocol.GAME.TO_SERVER, ClickWindowPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x07));
        registerPacket(Protocol.GAME.TO_SERVER, CloseWindowPacket.class, new ProtocolMapping(ProtocolConstants.MINECRAFT_1_12_2, 0x08));
    }

    public BungeePackets(Plugin plugin) {
        instance = this;
        logger = plugin.getLogger();

        plugin.getProxy().getPluginManager().registerListener(plugin, new ConnectionListener());
        plugin.getProxy().getPluginManager().registerListener(plugin, new PacketEventListener());

        try {
            registerPackets();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

}
