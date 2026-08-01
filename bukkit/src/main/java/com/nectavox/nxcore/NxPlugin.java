package com.nectavox.nxcore;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nectavox.nxcore.audience.PaperAudienceProvider;
import com.nectavox.nxcore.commands.CommandManager;
import com.nectavox.nxcore.interfaces.AudienceProvider;
import com.nectavox.nxcore.interfaces.SchedulerAdapter;
import com.nectavox.nxcore.listeners.UpdateNotify;
import com.nectavox.nxcore.managers.LangManager;
import com.nectavox.nxcore.managers.MenuManager;
import com.nectavox.nxcore.audience.SpigotAudienceProvider;
import com.nectavox.nxcore.schedulers.PaperScheduler;
import com.nectavox.nxcore.schedulers.SpigotScheduler;
import com.nectavox.nxcore.utils.ConfigUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
public abstract class NxPlugin extends JavaPlugin {

    private ConfigUtil configUtil;
    private LangManager langManager;
    private MenuManager menuManager;
    private SchedulerAdapter scheduler;
    private AudienceProvider audience;
    private CommandManager commandManager;

    @Override
    public final void onEnable() {

        configUtil = new ConfigUtil(this);

        langManager = new LangManager(this);
        langManager.load(true);

        menuManager = new MenuManager(this);
        menuManager.loadMenus(true);

        commandManager = new CommandManager(this);

        if (isPaper()) {
            scheduler = new PaperScheduler(this);
            audience = new PaperAudienceProvider();
        } else {
            scheduler = new SpigotScheduler(this);
            audience = new SpigotAudienceProvider(this);
        }

        getServer().getPluginManager().registerEvents(new UpdateNotify(this), this);

        this.enable();

        if (getConfig().getBoolean("check-update")) {
            checkForUpdate();
        }
    }

    @Override
    public final void onDisable() {
        this.disable();
    }

    @Override
    public final void onLoad() {
        this.load();
    }

    public void enable() {
    }

    public void disable() {
    }

    public void load() {
    }

    public final void reloadPlugin() {
        langManager.load(false);
        menuManager.loadMenus(false);

        reload();
    }

    protected void reload() {

    }

    public boolean isLastVersion = true;
    public String newVersion = "1.0";

    public void checkForUpdate() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://nectavox.com/api/resource/" + getDescription().getName().toLowerCase() + "/check-update"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                getLogger().warning("Failed to check update. Status: " + response.statusCode());
                return;
            }

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            if (!json.has("latest_version") || !json.has("version")) {
                return;
            }

            isLastVersion = json.get("version").getAsString().equals(getDescription().getVersion());

            if (!isLastVersion) {
                this.newVersion = json.get("latest_version").getAsString();
            }

        } catch (IOException | InterruptedException e) {
            getLogger().warning("Failed to check for update.");
        }
    }

    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
