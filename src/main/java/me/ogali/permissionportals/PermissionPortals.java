package me.ogali.permissionportals;

import lombok.Getter;
import me.ogali.permissionportals.listeners.PlayerJoinListener;
import me.ogali.permissionportals.listeners.PlayerLeaveListener;
import me.ogali.permissionportals.listeners.PortalEnterListener;
import me.ogali.permissionportals.player.PortalPlayerRegistry;
import me.ogali.permissionportals.utilities.PermissionUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class PermissionPortals extends JavaPlugin {

    @Getter
    private static PermissionPortals instance;
    @Getter
    private PortalPlayerRegistry portalPlayerRegistry;
    @Getter
    private PermissionUtils permissionUtils;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        initializeRegistries();
        initializeListeners();
    }

    public Optional<RegisteredServiceProvider<Economy>> getEconomy() {
        return Optional.ofNullable(getServer().getServicesManager().getRegistration(Economy.class));
    }

    private void initializeListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(permissionUtils), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalEnterListener(this), this);
    }

    private void initializeRegistries() {
        portalPlayerRegistry = new PortalPlayerRegistry();
        permissionUtils = new PermissionUtils();
    }

}
