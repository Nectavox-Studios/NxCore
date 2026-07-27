package com.nectavox.nxcore;

import com.nectavox.nxcore.managers.LangManager;
import com.nectavox.nxcore.managers.MenuManager;
import com.nectavox.nxcore.utils.ConfigUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class NxPlugin extends JavaPlugin {

    private ConfigUtil configUtil;
    private LangManager langManager;
    private MenuManager menuManager;

    @Override
    public final void onEnable() {

        configUtil = new ConfigUtil(this);

        langManager = new LangManager(this);
        langManager.load(true);

        menuManager = new MenuManager(this);
        menuManager.loadMenus(true);

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
}
