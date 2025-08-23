package me.flasser.naturalcoinflip.menues.cfMenu;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
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

import java.util.*;

import static me.flasser.naturalcoinflip.managers.CacheManager.*;
import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.format;

public class CFMenu {
    public static void openCFMenu(Player player, int page) {
        Bukkit.getScheduler().runTaskAsynchronously(NaturalCoinFlip.getInstance(), () -> {
            FlipManager.PlayerInfo info = FlipManager.getPlayerInfo(player.getUniqueId());
            int wins = info != null && info.wins != null ? info.wins : 0;
            int loses = info != null && info.loses != null ? info.loses : 0;
            int total = info != null ? wins+loses : 0;
            double ratio = info != null && total != 0 ? 100.0*wins/total : 0;

            List<UUID> flips = FlipManager.getAllFlips();
            Map<UUID, Double> flipAmounts = new HashMap<>();
            assert flips != null;
            for (UUID uuid : flips) {
                flipAmounts.put(uuid, Objects.requireNonNull(FlipManager.getFlipInfo(uuid)).amount);
            }

            int flipsPerPage = 36;
            int startIndex = page*flipsPerPage;
            int endIndex = Math.min(startIndex+flipsPerPage, flips.size());

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {

                Inventory menu = Bukkit.createInventory(null, 54, FileManager.getMessage("inventory_name") + (page + 1));

                for (int slot = 0; slot < 9; slot++) {
                    menu.setItem(slot, YELLOW_PANE);
                }

                for (int slot = 45; slot < 53; slot++) {
                    menu.setItem(slot, WHITE_PANE);
                }

                if (page > 0) {
                    menu.setItem(45, BACK_ARROW);
                }

                menu.setItem(53, FORWARD_ARROW);
                menu.setItem(48, UPDATE_HEAD);
                menu.setItem(50, INFO_BOOK);

                List<String> newestLoreList = new ArrayList<>();
                String newLore;
                for (String lore : FileManager.getListMessage("stats_head_lore")) {
                    newLore = lore
                            .replace("{wins}", String.valueOf(wins))
                            .replace("{loses}", String.valueOf(loses))
                            .replace("{ratio}", STATS_DECIMAL.format(ratio)+"%")
                            .replace("{total}", String.valueOf(total));

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

                int slot = 8;
                for (int i = startIndex; i < endIndex; i++) {
                    UUID flipUUID = flips.get(i);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(flipUUID);
                    Double amount = flipAmounts.get(flipUUID);
                    slot++;

                    newestLoreList = new ArrayList<>();
                    for (String lore : FileManager.getListMessage("flip_lore")) {
                        newLore = lore
                                .replace("{player}", offlinePlayer.getName())
                                .replace("{amount}", format(amount));

                        newestLoreList.add(newLore);
                    }
                    newestLore = newestLoreList.toArray(new String[0]);

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

                player.openInventory(menu);

            });
        });
    }
}