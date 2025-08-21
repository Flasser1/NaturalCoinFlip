package me.flasser.naturalcoinflip.menues.cfMenu;

import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.utility.itemUtil.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CFMenu {
    public static void openCFMenu(Player player, Integer page) {
        Inventory menu = Bukkit.createInventory(null, 54, FileManager.getMessage("inventory_name") + (page+1));

        ItemStack yellowPane = new NamedItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4),
                "§7"
        ).get();

        for (int slot=0; slot<9; slot++) {
            menu.setItem(slot, yellowPane);
        }

        ItemStack whitePane = new NamedItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0),
                "§7"
        ).get();

        for (int slot=45; slot<53; slot++) {
            menu.setItem(slot, whitePane);
        }

        if (page > 0) {
            ItemStack backArrow = new NamedItem(Material.ARROW, 1,
                    FileManager.getMessage("last_page")
            ).get();
            menu.setItem(45, backArrow);
        }
        ItemStack forwardArrow = new NamedItem(Material.ARROW, 1,
                FileManager.getMessage("next_page")
        ).get();
        menu.setItem(53, forwardArrow);

        ItemStack infoBook = new NamedItem(Material.BOOK_AND_QUILL, 1,
                FileManager.getMessage("info_book_name"),
                FileManager.getListMessage("info_book_lore")
        ).get();
        menu.setItem(49, infoBook);

        FlipManager.PlayerInfo info = FlipManager.getPlayerInfo(player.getUniqueId());

        int wins = 0;
        int loses = 0;

        if (info != null) {
            if (info.wins != null) wins = info.wins;
            if (info.loses != null) loses = info.loses;
        }

        List<String> newestLoreList = new ArrayList<>();
        String newLore;
        for (String lore : FileManager.getListMessage("stats_head_lore")) {
            newLore = lore
                    .replace("{wins}", String.valueOf(wins))
                    .replace("{loses}", String.valueOf(loses))
                    .replace("{total}", String.valueOf(wins+loses));

            newestLoreList.add(newLore);
        }
        String[] newestLore = newestLoreList.toArray(new String[0]);

        ItemStack infoHead = new NamedItem(Material.SKULL_ITEM, 1, (byte) 3,
                FileManager.getMessage("stats_head_name"),
                newestLore
        ).get();

        SkullMeta meta = (SkullMeta) infoHead.getItemMeta();

        if (meta != null) {
            meta.setOwner(player.getName());
            infoHead.setItemMeta(meta);
        }
        menu.setItem(4, infoHead);

        Integer slot = 8;
        List<UUID> flips = FlipManager.getAllFlips();
        for (UUID UUID : flips) {
            slot++;
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID);
            Integer amount = FlipManager.getFlipInfo(UUID).amount;

            newestLoreList = new ArrayList<>();
            for (String lore : FileManager.getListMessage("flip_lore")) {
                newLore = lore
                        .replace("{player}", offlinePlayer.getName())
                        .replace("{amount}", amount.toString());

                newestLoreList.add(newLore);
            }
            newestLore = newestLoreList.toArray(new String[0]);

            if (page == 0) {
                if (slot <= 36) {
                    ItemStack flipHead = new NamedItem(Material.SKULL_ITEM, 1, (byte) 3,
                            FileManager.getMessage("flip_name"),
                            newestLore
                    ).get();

                    meta = (SkullMeta) flipHead.getItemMeta();

                    if (meta != null) {
                        meta.setOwner(offlinePlayer.getName());
                        flipHead.setItemMeta(meta);
                    }
                    menu.setItem(slot, flipHead);
                }
            }
            else {
                if (slot >= 36*page && slot < 36*page+36) {
                    ItemStack flipHead = new NamedItem(Material.SKULL_ITEM, 1, (byte) 3,
                            FileManager.getMessage("flip_name"),
                            newestLore
                    ).get();

                    meta = (SkullMeta) flipHead.getItemMeta();

                    if (meta != null) {
                        meta.setOwner(offlinePlayer.getName());
                        flipHead.setItemMeta(meta);
                    }
                    menu.setItem(slot-36*slot+9, flipHead);
                }
            }
        }

        player.openInventory(menu);

    }
}