package me.flasser.naturalcoinflip.managers;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.utility.itemUtil.NamedItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CacheManager {
    public static ItemStack YELLOW_PANE;
    public static ItemStack WHITE_PANE;
    public static ItemStack BACK_ARROW;
    public static ItemStack FORWARD_ARROW;
    public static ItemStack INFO_BOOK;
    public static ItemStack UPDATE_HEAD;

    public static void createCache() {
        YELLOW_PANE = new NamedItem(
                new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), "§7"
        ).get();

        WHITE_PANE = new NamedItem(
                new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0), "§7"
        ).get();

        BACK_ARROW = new NamedItem(
                Material.ARROW, 1, FileManager.getMessage("last_page")
        ).get();

        FORWARD_ARROW = new NamedItem(
                Material.ARROW, 1, FileManager.getMessage("next_page")
        ).get();

        INFO_BOOK = new NamedItem(
                Material.BOOK_AND_QUILL, 1,
                FileManager.getMessage("info_book_name"),
                FileManager.getListMessage("info_book_lore")
        ).get();

        UPDATE_HEAD = new NamedItem(Material.SKULL_ITEM, 1, (byte) 3,
                NaturalCoinFlip.getInstance().getConfig().getString("updateValue"),
                FileManager.getMessage("update_head_name"),
                FileManager.getListMessage("update_head_lore")
        ).get();
    }
}
