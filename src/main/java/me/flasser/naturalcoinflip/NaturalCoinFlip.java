package me.flasser.naturalcoinflip;

import me.flasser.naturalcoinflip.commands.cfCommands.CoinFlipGroup;
import me.flasser.naturalcoinflip.commands.cfaCommands.CoinFlipAdminGroup;
import me.flasser.naturalcoinflip.managers.CacheManager;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.SQLManager;
import me.flasser.naturalcoinflip.menues.cfMenu.CFMenuListener;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class NaturalCoinFlip extends JavaPlugin {

    private static NaturalCoinFlip instance;
    private static Economy econ = null;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        saveDefaultConfig();
        FileManager.createMessages();
        CacheManager.createCache();

        SQLManager.connect();

        if (!SQLManager.isSetUp()) {
            SQLManager.setUp();
        }

        int pluginId = 26996;
        Metrics metrics = new Metrics(this, pluginId);

        getServer().getPluginManager().registerEvents(new CFMenuListener(), this);

        getCommand("cf").setExecutor(new CoinFlipGroup());
        getCommand("cf").setTabCompleter(new CoinFlipGroup());
        getCommand("cfa").setExecutor(new CoinFlipAdminGroup());
        getCommand("cfa").setTabCompleter(new CoinFlipAdminGroup());
    }

    @Override
    public void onDisable() {
        getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        SQLManager.disconnect();
    }

    public static NaturalCoinFlip getInstance() {
        return instance;
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
