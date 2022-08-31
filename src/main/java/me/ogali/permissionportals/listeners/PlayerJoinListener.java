package me.ogali.permissionportals.listeners;

import lombok.AllArgsConstructor;
import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.player.domain.PortalPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final PermissionPortals main;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PortalPlayer portalPlayer = new PortalPlayer(event.getPlayer(), false, 0, 0, 0, 0);
        portalPlayer.load(main.getFileHandler().getFile());
    }

}
