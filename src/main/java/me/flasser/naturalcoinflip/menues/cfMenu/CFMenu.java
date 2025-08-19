package me.flasser.naturalcoinflip.menues.cfMenu;

import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.utility.itemUtil.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class CFMenu {
    public static void openCFMenu(Player player, Integer page) {
        Inventory menu = Bukkit.createInventory(null, 54, "§e§lCOINFLIP §8- §fPAGE %page%".replace("%page%", page.toString()));

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
                    "§f§nLast Page"
            ).get();
            menu.setItem(45, backArrow);
        }
        ItemStack forwardArrow = new NamedItem(Material.ARROW, 1,
                "§f§nNext Page"
        ).get();
        menu.setItem(53, forwardArrow);

        ItemStack infoBook = new NamedItem(Material.BOOK_AND_QUILL, 1,
                "§f§nNext Page",
                "", "§fDette er en oversigt som indeholder alle", "§f§eCoinFlips §fsom lige nu er aktive på serveren.", "", "§eKommandoer:", "§7 §8- §f/cf", "§7 §8- §f/cf opret (antal)", "§7 §8- §f/cf delete"
        ).get();
        menu.setItem(49, infoBook);

        Integer wins = FlipManager.getPlayerInfo(player.getUniqueId()).wins;
        Integer loses = FlipManager.getPlayerInfo(player.getUniqueId()).loses;
        ItemStack infoHead = new NamedItem(Material.SKULL_ITEM, 1,
                "§f§lINFO",
                "", "§8┌ §7Total§8: §f" + (wins+loses), "§8├ §7Wins§8: §f" + wins, "§8└ §7Loses§8: §f" + loses, ""
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
            if (page == 0) {
                if (slot <= 36) {
                    ItemStack flipHead = new NamedItem(Material.SKULL_ITEM, 1,
                            "§c§lCOINFLIP",
                            "", "§8┌ §7This coinflip was created", "§8└ §7by §f" + offlinePlayer.getName(), "", "§8│ §7Betted amount: §f" +amount.toString(), "", "§8➥ §7Click here to accept this coinflip."
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
                    ItemStack flipHead = new NamedItem(Material.SKULL_ITEM, 1,
                            "§c§lCOINFLIP",
                            "", "§8┌ §7This coinflip was created", "§8└ §7by §f" + offlinePlayer.getName(), "", "§8│ §7Betted amount: §f" +amount.toString(), "", "§8➥ §7Click here to accept this coinflip."
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