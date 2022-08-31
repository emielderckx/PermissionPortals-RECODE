package me.ogali.permissionportals.player.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.player.Loadable;
import me.ogali.permissionportals.player.Saveable;
import me.ogali.permissionportals.utilities.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

@AllArgsConstructor
@Setter
@Getter
public class PortalPlayer implements Saveable, Loadable {

    private final Player player;
    private boolean pushBack;
    private long netherPortalTimer;
    private long endPortalTimer;
    private double netherPortalCost;
    private double endPortalCost;

    public void enterPortal(PlayerTeleportEvent event, boolean netherPortal) {
        if (netherPortal) {
            if (canNotEnterPortal(true, netherPortalCost, "permissions.nether-portal")) {
                event.setCancelled(true);
                pushBack(event);
            }
            setNetherPortalTimer(System.currentTimeMillis() + 5 * 1000);
            return;
        }
        if (canNotEnterPortal(false, endPortalCost, "permissions.end-portal")) {
            event.setCancelled(true);
            pushBack(event);
        }
        setEndPortalTimer(System.currentTimeMillis() + 5 * 1000);
    }

    private void sendDenyMessage(boolean netherPortal, boolean permissionMessage) {
        if (netherPortal) {
            if (!(System.currentTimeMillis() > netherPortalTimer)) return;
            if (permissionMessage) {
                Chat.tell(player, PermissionPortals.getInstance().getConfig().getString("messages.no-perms"));
                return;
            }
            Chat.tell(player, PermissionPortals.getInstance().getConfig().getString("messages.not-enough-money"));
            return;
        }
        if (!(System.currentTimeMillis() > endPortalTimer)) return;
        if (permissionMessage) {
            Chat.tell(player, PermissionPortals.getInstance().getConfig().getString("messages.no-perms"));
            return;
        }
        Chat.tell(player, PermissionPortals.getInstance().getConfig().getString("messages.not-enough-money"));
    }

    private void pushBack(PlayerTeleportEvent event) {
        if (!pushBack) return;
        event.setCancelled(true);
        player.setVelocity(player.getLocation().getDirection().multiply(-0.7).setY(0.5));
    }

    private boolean canNotEnterPortal(boolean netherPortal, double portalCost, String permission) {
        if (doesNotHavePermission(netherPortal, permission)) return true;
        return doesNotHavePortalCost(netherPortal, portalCost);
    }

    private boolean doesNotHavePortalCost(boolean netherPortal, double portalCost) {
        if (PermissionPortals.getInstance().getEconomy().isEmpty()) return false;
        Economy economy = PermissionPortals.getInstance().getEconomy().get().getProvider();
        if (!economy.has(player, portalCost)) {
            sendDenyMessage(netherPortal, false);
            return true;
        }
        economy.withdrawPlayer(player, portalCost);
        Chat.tell(player, Objects.requireNonNull(PermissionPortals.getInstance().getConfig().getString("messages.portal-charge"))
                .replace("$portalcost", String.valueOf(portalCost)));
        return false;
    }

    private boolean doesNotHavePermission(boolean netherPortal, String permission) {
        if (!player.hasPermission(Objects.requireNonNull(PermissionPortals.getInstance().getConfig().getString(permission)))) {
            sendDenyMessage(netherPortal, true);
            return true;
        }
        return false;
    }

    @Override
    public void save(FileConfiguration file) {
        ConfigurationSection section = file.createSection(player.getUniqueId().toString());
        section.set("pushback", pushBack);
        section.set("netherPortalCost", netherPortalCost);
        section.set("endPortalCost", endPortalCost);
        PermissionPortals.getInstance().getFileHandler().saveFile();
    }

    @Override
    public void load(FileConfiguration file) {
        ConfigurationSection section = file.getConfigurationSection(player.getUniqueId().toString());
        if (section == null) {
            PermissionPortals.getInstance().getPortalPlayerRegistry().addPortalPlayer(this);
            return;
        }

        pushBack = section.getBoolean("pushback");
        netherPortalCost = section.getDouble("netherPortalCost");
        endPortalCost = section.getDouble("endPortalCost");
        PermissionPortals.getInstance().getPortalPlayerRegistry().addPortalPlayer(this);
    }

}
