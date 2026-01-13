package me.flasser.naturalcoinflip;

import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.annotation.Scan;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;

@Scan(deep = true)
public final class NaturalCoinFlip extends OkaeriBukkitPlugin {

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
        int pluginId = 26996;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void onShutdown() {
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
