package me.ogali.permissionportals.player.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.utilities.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

@Setter
@Getter
public class PortalPlayer {

    private final Player player;
    private boolean pushBack;
    private double netherPortalCost;
    private double endPortalCost;
    private long netherPortalTimer;
    private long endPortalTimer;

    public PortalPlayer(Player player, boolean pushBack, double netherPortalCost, double endPortalCost) {
        this.player = player;
        this.pushBack = pushBack;
        this.netherPortalCost = netherPortalCost;
        this.endPortalCost = endPortalCost;
    }

    public void enterPortal(PlayerTeleportEvent event, boolean netherPortal) {
        if (netherPortal) {
            if (canNotEnterPortal(true, getPortalPrice(true), "permissions.nether-portal")) {
                event.setCancelled(true);
                pushBack(event);
            }
            netherPortalTimer = System.currentTimeMillis() + 5 * 1000;
            return;
        }
        if (canNotEnterPortal(false, getPortalPrice(false), "permissions.end-portal")) {
            event.setCancelled(true);
            pushBack(event);
        }
        endPortalTimer = System.currentTimeMillis() + 5 * 1000;
    }

    private void sendDenyMessage(boolean netherPortal, boolean permissionMessage, double amountOfMoney) {
        if (netherPortal) {
            checkTimers(permissionMessage, netherPortalTimer, amountOfMoney, netherPortalCost);
            return;
        }
        checkTimers(permissionMessage, endPortalTimer, amountOfMoney, endPortalCost);
    }

    private void checkTimers(boolean permissionMessage, long timer, double amountOfMoney, double portalCost) {
        if (!(System.currentTimeMillis() > timer)) return;
        if (permissionMessage) {
            Chat.tell(player, PermissionPortals.getInstance().getConfig().getString("messages.no-perms"));
            return;
        }
        Chat.tell(player, Objects.requireNonNull(PermissionPortals.getInstance().getConfig().getString("messages.not-enough-money"))
                .replace("$portalcost", String.valueOf(portalCost))
                .replace("$amount-needed", String.valueOf(Math.abs(portalCost - amountOfMoney))));
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

        if (PermissionPortals.getInstance().getConfig().getBoolean("global.one-way-charge")
                && player.getWorld().getEnvironment() == World.Environment.NETHER) return false;

        if (!economy.has(player, portalCost)) {
            sendDenyMessage(netherPortal, false, economy.getBalance(player));
            return true;
        }
        if (portalCost <= 0) return false;

        economy.withdrawPlayer(player, portalCost);

        if (player.hasPermission("permissionportals.ignore-charge-message")) return false;
        Chat.tell(player, Objects.requireNonNull(PermissionPortals.getInstance().getConfig().getString("messages.portal-charge"))
                .replace("$portalcost", String.valueOf(portalCost)));
        return false;
    }

    private boolean doesNotHavePermission(boolean netherPortal, String permission) {
        if (!player.hasPermission(Objects.requireNonNull(PermissionPortals.getInstance().getConfig().getString(permission)))) {
            sendDenyMessage(netherPortal, true, 0);
            return true;
        }
        return false;
    }

    private double getPortalPrice(boolean netherPortal) {
        if (netherPortal) {
            if (netherPortalCost == -1) {
                netherPortalCost = PermissionPortals.getInstance().getConfig().getDouble("global.nether-portal-cost");
                return netherPortalCost;
            }
            return netherPortalCost;
        }
        if (endPortalCost == -1) {
            endPortalCost = PermissionPortals.getInstance().getConfig().getDouble("global.end-portal-cost");
            return endPortalCost;
        }
        return endPortalCost;
    }

}
