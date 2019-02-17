package fr.badblock.bungee.packets.listener;

import java.util.Optional;

import fr.badblock.bungee.packets.event.InteractWindowEvent;
import fr.badblock.bungee.packets.event.PacketEvent;
import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.packets.protocol.game.client.SetWindowSlotPacket;
import fr.badblock.bungee.packets.packets.protocol.game.server.ClickWindowPacket;
import fr.badblock.bungee.packets.packets.protocol.game.shared.CloseWindowPacket;
import fr.badblock.bungee.packets.packets.protocol.game.shared.ConfirmTransactionPacket;
import fr.badblock.bungee.packets.window.Window;
import fr.badblock.bungee.packets.window.WindowManager;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Listener for packet related events to provide as much support to inventories as possible
 */
public class PacketEventListener implements Listener {

	@EventHandler
	public void onPacket(PacketEvent evt)
	{
		try
		{
			if(evt.getPacket() instanceof ClickWindowPacket) {
				ClickWindowPacket packet = ((ClickWindowPacket) evt.getPacket());
				Optional<Window> openWindow = WindowManager.instance.getOpenWindow(evt.getPlayer());//, packet.getWindowID());
				if(openWindow.isPresent()) {
					// Cancel event
					evt.setCancelled(true);

					Window window = openWindow.get();
					ConfirmTransactionPacket confirmPacket = new ConfirmTransactionPacket(window, window.getNextActionNumber(), false);
					SetWindowSlotPacket clearCursorPacket = new SetWindowSlotPacket((byte) -1, (short) -1, ItemStack.EMPTY());
					SetWindowSlotPacket resetWindowSlotPacket = new SetWindowSlotPacket(window, packet.getSlot(), window.get(packet.getSlot()));

					InteractWindowEvent event = new InteractWindowEvent(window, evt.getPlayer(), packet.getClickType(), packet.getSlot());
					ProxyServer.getInstance().getPluginManager().callEvent(event);

					evt.getPlayer().unsafe().sendPacket(confirmPacket);
					evt.getPlayer().unsafe().sendPacket(clearCursorPacket);
					evt.getPlayer().unsafe().sendPacket(resetWindowSlotPacket);
				}
			}

			if(evt.getPacket() instanceof ConfirmTransactionPacket) {
				if(evt.getSender() instanceof UserConnection && WindowManager.instance.getOpenWindow(evt.getPlayer()).isPresent()) {
					evt.setCancelled(true);
				}
			}

			if(evt.getPacket() instanceof CloseWindowPacket) {
				WindowManager.instance.closeWindow(evt.getPlayer().getUniqueId());
			}
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}
	}

}
