package me.ogali.permissionportals;

import lombok.Getter;
import me.ogali.permissionportals.commands.AdminSettingsCommand;
import me.ogali.permissionportals.commands.PlayerSettingsCommand;
import me.ogali.permissionportals.files.FileHandler;
import me.ogali.permissionportals.listeners.PlayerJoinListener;
import me.ogali.permissionportals.listeners.PlayerLeaveListener;
import me.ogali.permissionportals.listeners.PortalEnterListener;
import me.ogali.permissionportals.player.PortalPlayerRegistry;
import me.ogali.permissionportals.player.domain.PortalPlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.UUID;

public final class PermissionPortals extends JavaPlugin {

    @Getter
    private static PermissionPortals instance;
    @Getter
    private PortalPlayerRegistry portalPlayerRegistry;
    @Getter
    private FileHandler fileHandler;

    @Override
    public void onEnable() {
        instance = this;
        initializeListeners();
        initializeRegistries();
        initializeCommands();
    }

    @Override
    public void onDisable() {
        fileHandler.savePlayerData();
    }

    public Optional<RegisteredServiceProvider<Economy>> getEconomy() {
        return Optional.ofNullable(getServer().getServicesManager().getRegistration(Economy.class));
    }

    private void initializeListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalEnterListener(this), this);
    }

    private void initializeRegistries() {
        portalPlayerRegistry = new PortalPlayerRegistry(this);
        fileHandler = new FileHandler(this);
    }

    private void initializeCommands() {
        getCommand("pportalsadmin").setExecutor(new AdminSettingsCommand());
        getCommand("permissionportals").setExecutor(new PlayerSettingsCommand(this));
    }

}
