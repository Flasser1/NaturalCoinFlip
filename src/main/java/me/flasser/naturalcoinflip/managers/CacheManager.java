package me.flasser.naturalcoinflip.managers;

import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.utility.itemUtil.NamedItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

@Component
public class CacheManager {

    private @Inject NaturalCoinFlip plugin;

    @Planned(ExecutionPhase.POST_SETUP)
    public void initialize() {
        this.createCache();
        plugin.getLogger().info("NATURALCOINFLIP: SETTING UP CACHE");
    }

    @Inject
    private FileManager fileManager;

    private ItemStack YELLOW_PANE;
    private ItemStack WHITE_PANE;
    private ItemStack BACK_ARROW;
    private ItemStack FORWARD_ARROW;
    private ItemStack INFO_BOOK;
    private ItemStack UPDATE_HEAD;
    private DecimalFormat STATS_DECIMAL;

    public void createCache() {
        YELLOW_PANE = new NamedItem(
                new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), "§7"
        ).get();

        WHITE_PANE = new NamedItem(
                new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0), "§7"
        ).get();

        BACK_ARROW = new NamedItem(
                Material.ARROW, 1, fileManager.getMessage("last_page")
        ).get();

        FORWARD_ARROW = new NamedItem(
                Material.ARROW, 1, fileManager.getMessage("next_page")
        ).get();

        INFO_BOOK = new NamedItem(
                Material.BOOK_AND_QUILL, 1,
                fileManager.getMessage("info_book_name"),
                fileManager.getListMessage("info_book_lore")
        ).get();

        UPDATE_HEAD = new NamedItem(Material.SKULL_ITEM, 1, (byte) 3,
                plugin.getConfig().getString("updateValue"),
                fileManager.getMessage("update_head_name"),
                fileManager.getListMessage("update_head_lore")
        ).get();

        STATS_DECIMAL = new DecimalFormat("#.##");
    }

    public ItemStack getYellowPane() {
        return YELLOW_PANE.clone();
    }

    public ItemStack getWhitePane() {
        return WHITE_PANE.clone();
    }

    public ItemStack getBackArrow() {
        return BACK_ARROW.clone();
    }

    public ItemStack getForwardArrow() {
        return FORWARD_ARROW.clone();
    }

    public ItemStack getInfoBook() {
        return INFO_BOOK.clone();
    }

    public ItemStack getUpdateHead() {
        return UPDATE_HEAD.clone();
    }

    public DecimalFormat getStatsDecimal() {
        return (DecimalFormat) STATS_DECIMAL.clone();
    }
}
