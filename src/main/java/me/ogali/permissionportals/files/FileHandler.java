package me.ogali.permissionportals.files;

import me.ogali.permissionportals.PermissionPortals;
import me.ogali.permissionportals.player.domain.PortalPlayer;
import me.ogali.permissionportals.utilities.Chat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHandler {

    private final PermissionPortals main;
    private FileConfiguration playerFile = null;
    private File file = null;

    public FileHandler(PermissionPortals main) {
        this.main = main;
        saveDefaultFile();
    }

    public void reloadFile() {
        if (this.file == null) {
            this.file = new File(this.main.getDataFolder(), "playerdata.yml");
        }
        this.playerFile = YamlConfiguration.loadConfiguration(this.file);

        InputStream defaultStream = this.main.getResource("playerdata.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultFile = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.playerFile.setDefaults(defaultFile);
        }
    }

    public FileConfiguration getFile() {
        if (this.playerFile == null) {
            reloadFile();
        }
        return this.playerFile;
    }

    public void saveFile() {
        if (this.playerFile == null || this.file == null) return;

        try {
            this.getFile().save(this.file);
        } catch (IOException e) {
            Chat.log("COULD NOT SAVE " + this.file + " " + e);
        }
    }

    public void saveDefaultFile() {
        if (this.file == null) {
            this.file = new File(this.main.getDataFolder(), "playerdata.yml");
        }

        if (!this.file.exists()) {
            this.main.saveResource("playerdata.yml", false);
        }
    }

    public void savePlayerData() {
        main.getPortalPlayerRegistry().getPortalPlayerMap()
                .values()
                .forEach(portalPlayer -> portalPlayer.save(playerFile));
    }

    public void savePlayerData(PortalPlayer portalPlayer) {
        portalPlayer.save(playerFile);
    }

}
