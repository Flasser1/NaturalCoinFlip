package me.flasser.naturalcoinflip;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import me.flasser.naturalcoinflip.managers.CacheManager;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.SQLManager;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class NaturalCoinFlip extends OkaeriBukkitPlugin {
    @Inject
    private FileManager fileManager;

    @Inject
    private SQLManager sqlManager;

    @Inject
    private CacheManager cacheManager;

    private static Economy econ = null;

    @Planned(ExecutionPhase.STARTUP)
    public void onStartup() {
        getLogger().info("NATURALCOINFLIP: STARTING UP");
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void afterSetup() {
        fileManager.createMessages();
        getLogger().info("NATURALCOINFLIP: MESSAGES SETUP");
        cacheManager.createCache();
        getLogger().info("NATURALCOINFLIP: CACHE SETUP");

        sqlManager.connect();

        if (!sqlManager.isSetUp()) {
            sqlManager.setUp();
        }
        getLogger().info("NATURALCOINFLIP: SQL SETUP");

        int pluginId = 26996;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void onShutdown() {
        sqlManager.disconnect();
        getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    public static Economy getEcon() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
