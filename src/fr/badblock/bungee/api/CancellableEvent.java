package fr.badblock.bungee.api;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public abstract class CancellableEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    @Getter
    @Setter
    private String cancelReason = "";

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel(String reason) {
        setCancelled(true);
        setCancelReason(reason);
    }
}
