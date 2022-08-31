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
        PortalPlayer portalPlayer = new PortalPlayer(event.getPlayer(), main.getConfig().getBoolean("global.pushback"),
                0, 0, main.getConfig().getDouble("global.nether-portal-cost"), main.getConfig().getDouble("global.end-portal-cost"));
        portalPlayer.load(main.getFileHandler().getFile());
    }

}
