package fr.badblock.bungee.api;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 * 
 * Cancelable event, to be used with other events
 * 
 * @author xMalware
 *
 */
public abstract class CancellableEvent extends Event implements Cancellable {

	// Cancel field
	private boolean cancelled = false;

	// Cancel reason field
	@Getter
	@Setter
	private String cancelReason = "";

	@Override
	/**
	 * Returns if the event is cancelled
	 */
	public boolean isCancelled() {
		// Returns cancelled field
		return cancelled;
	}

	@Override
	/**
	 * Set the event as cancelled or not?
	 */
	public void setCancelled(boolean cancelled) {
		// Set the cancelled field
		this.cancelled = cancelled;
	}

	/**
	 * Cancel the event with reason
	 * 
	 * @param reason
	 */
	public void cancel(String reason) {
		// Set cancelled
		setCancelled(true);
		// Set cancel reason
		setCancelReason(reason);
	}

}
