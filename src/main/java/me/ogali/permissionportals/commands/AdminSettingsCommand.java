package me.ogali.permissionportals.commands;

import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.utilities.Chat;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class AdminSettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("pportalsadmin")
                && !label.equalsIgnoreCase("ppa")) return false;
        if (!(sender instanceof Player player)) return false;
        if (!player.hasPermission("pp.admin")) return false;
        if (!(args.length > 0)) {
            Chat.tell(player, "&cUsage: /ppa settings [player] [pushback|nether portal cost (npc)|end portal cost (epc)] [value]");
            return false;
        }
        if (args.length >= 4) {
            if (!args[0].equalsIgnoreCase("settings")) return false;
            if (Bukkit.getPlayer(args[1]) == null) {
                Chat.tell(player, "&cInvalid or offline player.");
                return false;
            }
            PermissionPortals.getInstance().getPortalPlayerRegistry().getPortalPlayer(Objects.requireNonNull(Bukkit.getPlayer(args[1])))
                    .ifPresent(portalPlayer -> {
                        switch (args[2]) {
                            case "pushback":
                                if (args[3].equalsIgnoreCase("true")) {
                                    portalPlayer.setPushBack(true);
                                } else if (args[3].equalsIgnoreCase("false")) {
                                    portalPlayer.setPushBack(false);
                                }
                                break;
                            case "npc":
                                if (NumberUtils.isNumber(args[3])) {
                                    portalPlayer.setNetherPortalCost(Double.parseDouble(args[3]));
                                }
                                break;
                            case "epc":
                                if (NumberUtils.isNumber(args[3])) {
                                    portalPlayer.setEndPortalCost(Double.parseDouble(args[3]));
                                }
                                break;
                            default:
                                Chat.tell(player, "&cUsage: /ppa settings [player] [pushback|nether portal cost (npc)|end portal cost (epc)] [value]");
                                break;
                        }
                    });
        }
        return false;
    }

}
