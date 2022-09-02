package me.ogali.permissionportals.listeners;

import lombok.AllArgsConstructor;
import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.player.domain.PortalPlayer;
import me.ogali.permissionportals.utilities.PermissionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final PermissionUtils permissionUtils;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PermissionPortals.getInstance().getPortalPlayerRegistry()
                .addPortalPlayer(new PortalPlayer(player, permissionUtils.getPushBack(player),
                        permissionUtils.getPortalCost(player, true),
                        permissionUtils.getPortalCost(player, false)));
    }

}
