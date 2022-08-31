package me.ogali.permissionportals.listeners;

import lombok.AllArgsConstructor;
import me.ogali.permissionportals.PermissionPortals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

@AllArgsConstructor
public class PortalEnterListener implements Listener {

    private final PermissionPortals main;

    @EventHandler
    public void onPortalEnter(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL &&
                event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;
        main.getPortalPlayerRegistry().getPortalPlayer(event.getPlayer()).ifPresent(portalPlayer -> {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
                portalPlayer.enterPortal(event, true);
                return;
            }
            portalPlayer.enterPortal(event, false);
        });
    }

}
