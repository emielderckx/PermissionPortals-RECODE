package me.ogali.permissionportals.commands;

import lombok.AllArgsConstructor;
import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.player.domain.PortalPlayer;
import me.ogali.permissionportals.utilities.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PlayerSettingsCommand implements CommandExecutor {

    private final PermissionPortals main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("permissionportals")
                && !label.equalsIgnoreCase("pportals")) return false;
        if (!(sender instanceof Player player)) return false;
        if (!player.hasPermission("pp.player")) return false;
        if (!(args.length > 0)) {
            Chat.tell(player, "&cUsage: /pportals settings [pushback] [true/false]");
            return false;
        }
        if (args[0].equalsIgnoreCase("help")) {
            Chat.tell(player, "&cUsage: /pportals settings [pushback] [true/false]");
            return false;
        }
        if (!args[0].equalsIgnoreCase("settings")) {
            Chat.tell(player, "&cUsage: /pportals settings [pushback] [true/false]");
            return false;
        }
        if (args.length >= 3) {
            if (main.getPortalPlayerRegistry().getPortalPlayer(player).isEmpty()) return false;
            PortalPlayer portalPlayer = main.getPortalPlayerRegistry().getPortalPlayer(player).get();

            if (args[0].equalsIgnoreCase("settings")) {
                if (args[1].equalsIgnoreCase("pushback")) {
                    if (args[2].equalsIgnoreCase("true")) {
                        portalPlayer.setPushBack(true);
                    } else if (args[2].equalsIgnoreCase("false")) {
                        portalPlayer.setPushBack(false);
                    }
                }
            }
        }
        return false;
    }

}
