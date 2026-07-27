package com.nectavox.nxcore;

import com.nectavox.nxcore.interfaces.SchedulerAdapter;
import com.nectavox.nxcore.managers.LangManager;
import com.nectavox.nxcore.managers.MenuManager;
import com.nectavox.nxcore.providers.AdventureAudienceProvider;
import com.nectavox.nxcore.schedulers.PaperScheduler;
import com.nectavox.nxcore.schedulers.SpigotScheduler;
import com.nectavox.nxcore.utils.ConfigUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class NxPlugin extends JavaPlugin {

    private ConfigUtil configUtil;
    private LangManager langManager;
    private MenuManager menuManager;
    private SchedulerAdapter scheduler;
    private AdventureAudienceProvider audience;

    @Override
    public final void onEnable() {

        configUtil = new ConfigUtil(this);

        langManager = new LangManager(this);
        langManager.load(true);

        menuManager = new MenuManager(this);
        menuManager.loadMenus(true);

        if (isPaper()) {
            scheduler = new PaperScheduler(this);
        } else {
            scheduler = new SpigotScheduler(this);
        }

        audience = new AdventureAudienceProvider(this);

        this.enable();
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

    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
