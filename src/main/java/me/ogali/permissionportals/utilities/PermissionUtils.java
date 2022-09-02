package me.ogali.permissionportals.utilities;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionUtils {

    public double getPortalCost(Player player, boolean netherPortal) {
        double lowestCost = -1;

        if (netherPortal) {
            for (PermissionAttachmentInfo attachmentInfo : getNetherPortalPermissions(player)) {
                String[] splitPermissionNode = getSplitPermissionNode(attachmentInfo.getPermission());
                if (splitPermissionNode == null) {
                    Chat.log("Invalid permission for player: " + player.getName() + ", node: " + attachmentInfo.getPermission());
                    return -1;
                }
                lowestCost = getLowestCost(lowestCost, Double.parseDouble(splitPermissionNode[2]));
            }
            return lowestCost;
        }
        for (PermissionAttachmentInfo attachmentInfo : getEndPortalPermissions(player)) {
            String[] splitPermissionNode = getSplitPermissionNode(attachmentInfo.getPermission());
            if (splitPermissionNode == null) {
                Chat.log("Invalid permission for player: " + player.getName() + ", node: " + attachmentInfo.getPermission());
                return -1;
            }
            lowestCost = getLowestCost(lowestCost, Double.parseDouble(splitPermissionNode[2]));
        }
        return lowestCost;
    }

    public boolean getPushBack(Player player) {
        return getPushBackPermission(player).isPresent();
    }

    private double getLowestCost(double lowestCost, double permissionCost) {
        if (lowestCost == -1) {
            return permissionCost;
        }
        return Math.min(permissionCost, lowestCost);
    }

    private String[] getSplitPermissionNode(String permissionNode) {
        String[] splitPermissionNode = permissionNode.split("\\.");
        return splitPermissionNode.length == 3 ? splitPermissionNode : null;
    }

    private Set<PermissionAttachmentInfo> getNetherPortalPermissions(Player player) {
        return getPPPermissionAttachmentInfo(player)
                .stream()
                .filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().contains("netherportal"))
                .collect(Collectors.toSet());
    }

    private Set<PermissionAttachmentInfo> getEndPortalPermissions(Player player) {
        return getPPPermissionAttachmentInfo(player)
                .stream()
                .filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().contains("endportal"))
                .collect(Collectors.toSet());
    }

    private Optional<PermissionAttachmentInfo> getPushBackPermission(Player player) {
        return getPPPermissionAttachmentInfo(player)
                .stream()
                .filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().contains("pushback"))
                .collect(Collectors.toSet())
                .stream()
                .findFirst();
    }

    private Set<PermissionAttachmentInfo> getPPPermissionAttachmentInfo(Player player) {
        return player.getEffectivePermissions()
                .stream()
                .filter(permissionAttachmentInfo -> permissionAttachmentInfo.getPermission().startsWith("permissionportals"))
                .collect(Collectors.toSet());
    }

}
