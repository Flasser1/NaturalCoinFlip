package me.flasser.naturalcoinflip.menues;

import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.CacheManager;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.utility.itemUtil.NamedItem;
import me.flasser.naturalcoinflip.utility.misc.FormatNumber;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

@Component
public class CFMenu {

    private @Inject NaturalCoinFlip plugin;
    private @Inject CacheManager cacheManager;
    private @Inject FileManager fileManager;
    private @Inject FlipManager flipManager;
    private @Inject FormatNumber formatNumber;

    @Async
    public void openCFMenu(Player player, int page) {
        FlipManager.PlayerInfo info = flipManager.getPlayerInfo(player.getUniqueId());
        int wins = info != null && info.wins != null ? info.wins : 0;
        int loses = info != null && info.loses != null ? info.loses : 0;
        int total = info != null ? wins+loses : 0;
        double ratio = info != null && total != 0 ? 100.0*wins/total : 0;

        List<UUID> flips = flipManager.getAllFlips();
        Map<UUID, Double> flipAmounts = new HashMap<>();
        assert flips != null;
        for (UUID uuid : flips) {
            flipAmounts.put(uuid, Objects.requireNonNull(flipManager.getFlipInfo(uuid)).amount);
        }

        int flipsPerPage = 36;
        int startIndex = page*flipsPerPage;
        int endIndex = Math.min(startIndex+flipsPerPage, flips.size());


        Inventory menu = Bukkit.createInventory(null, 54, fileManager.getMessage("inventory_name") + (page + 1));

        for (int slot = 0; slot < 9; slot++) {
            menu.setItem(slot, cacheManager.getYellowPane());
        }

        for (int slot = 45; slot < 53; slot++) {
            menu.setItem(slot, cacheManager.getWhitePane());
        }

        if (page > 0) {
            menu.setItem(45, cacheManager.getBackArrow());
        }

        menu.setItem(53, cacheManager.getForwardArrow());
        menu.setItem(48, cacheManager.getUpdateHead());
        menu.setItem(50, cacheManager.getInfoBook());

        List<String> newestLoreList = new ArrayList<>();
        String newLore;
        for (String lore : fileManager.getListMessage("stats_head_lore")) {
            newLore = lore
                    .replace("{wins}", String.valueOf(wins))
                    .replace("{loses}", String.valueOf(loses))
                    .replace("{ratio}", cacheManager.getStatsDecimal().format(ratio)+"%")
                    .replace("{total}", String.valueOf(total));

            newestLoreList.add(newLore);
        }
        String[] newestLore = newestLoreList.toArray(new String[0]);

        ItemStack infoHead = new NamedItem(Material.SKULL_ITEM, 1, (byte) 3,
                fileManager.getMessage("stats_head_name"),
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
            for (String lore : fileManager.getListMessage("flip_lore")) {
                newLore = lore
                        .replace("{player}", offlinePlayer.getName())
                        .replace("{amount}", formatNumber.format(amount));

                newestLoreList.add(newLore);
            }
            newestLore = newestLoreList.toArray(new String[0]);

            ItemStack flipHead = new NamedItem(Material.SKULL_ITEM, 1, (byte) 3,
                    fileManager.getMessage("flip_name"),
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
    }
}