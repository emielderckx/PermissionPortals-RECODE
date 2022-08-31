package me.ogali.permissionportals.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.player.domain.PortalPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PortalPlayerRegistry {

    private final PermissionPortals main;

    @Getter
    private final Map<UUID, PortalPlayer> portalPlayerMap = new HashMap<>();

    public void addPortalPlayer(PortalPlayer portalPlayer) {
        portalPlayerMap.put(portalPlayer.getPlayer().getUniqueId(), portalPlayer);
    }

    public void removePortalPlayer(Player player) {
        portalPlayerMap.remove(player.getUniqueId());
    }

    public Optional<PortalPlayer> getPortalPlayer(UUID uuid) {
        return Optional.ofNullable(portalPlayerMap.get(uuid));
    }

    public Optional<PortalPlayer> getPortalPlayer(Player player) {
        return getPortalPlayer(player.getUniqueId());
    }

}
