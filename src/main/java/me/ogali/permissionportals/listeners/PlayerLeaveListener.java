package me.ogali.permissionportals.listeners;

import lombok.AllArgsConstructor;
import me.ogali.permissionportals.PermissionPortals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerLeaveListener implements Listener {

    private final PermissionPortals main;

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        main.getPortalPlayerRegistry().getPortalPlayer(event.getPlayer())
                .ifPresent(portalPlayer -> {
                    portalPlayer.save(main.getFileHandler().getFile());
                    main.getPortalPlayerRegistry().removePortalPlayer(portalPlayer.getPlayer());
                });
    }

}