package me.flasser.naturalcoinflip.menues.cfMenu;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.utility.misc.ActionbarUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.format;

public class CFMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) {
            return;
        }

        if (!e.getClickedInventory().getName().contains(FileManager.getMessage("inventory_name"))) {
            return;
        }

        int page = Integer.parseInt(e.getClickedInventory().getName().replace(FileManager.getMessage("inventory_name"), ""));

        e.setCancelled(true);
        if (e.getCurrentItem().getType() == Material.ARROW) {
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);
            if (e.getRawSlot() == 53) {
                CFMenu.openCFMenu(player, page);
            } else if (e.getRawSlot() == 45) {
                CFMenu.openCFMenu(player, page-2);
            }
            return;
        }

        if (
                e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE ||
                e.getCurrentItem().getType() == Material.BOOK_AND_QUILL ||
                e.getCurrentItem().getItemMeta().getDisplayName().contains(FileManager.getMessage("stats_head_name")))
        {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(FileManager.getMessage("update_head_name")))) {
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);
            CFMenu.openCFMenu(player, page-1);
            return;
        }

        if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {
            player.closeInventory();
            SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
            UUID UUID = meta.getOwner() != null ? Bukkit.getOfflinePlayer(meta.getOwner()).getUniqueId() : null;

            if (!FlipManager.hasFlip(UUID)) {
                player.sendMessage(FileManager.getMessage("flip_already_taken"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            if (UUID == player.getUniqueId()) {
                player.sendMessage(FileManager.getMessage("click_own_flip"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            double amount = Objects.requireNonNull(FlipManager.getFlipInfo(UUID)).amount;

            if (NaturalCoinFlip.getEcon().getBalance(player) < amount) {
                player.sendMessage(FileManager.getMessage("insufficient_funds_take"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            FlipManager.removeFlip(UUID);
            NaturalCoinFlip.getEcon().withdrawPlayer(player, amount);
            int timer = NaturalCoinFlip.getInstance().getConfig().getInt("time");
            for (int i = 0; i < timer; i++) {
                int secondsLeft = timer - i;

                Bukkit.getScheduler().runTaskLater(NaturalCoinFlip.getInstance(), () -> {
                    ActionbarUtil.sendActionBar(player, FileManager.getMessage("countdown").replace("{timer}", String.valueOf(secondsLeft)));
                    ActionbarUtil.sendActionBar(Bukkit.getPlayer(UUID), FileManager.getMessage("countdown").replace("{timer}", String.valueOf(secondsLeft)));

                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);
                    Bukkit.getPlayer(UUID).playSound(Bukkit.getPlayer(UUID).getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);

                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                    Bukkit.getPlayer(UUID).playSound(Bukkit.getPlayer(UUID).getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                }, 20L * i);
            }

            Bukkit.getScheduler().runTaskLater(NaturalCoinFlip.getInstance(), () -> {
                List<Player> players = new ArrayList<>();
                players.add(player);
                players.add(Bukkit.getPlayer(UUID));

                Player winner = players.get(new Random().nextInt(players.size()));
                players.remove(winner);
                Player loser = players.get(0);

                NaturalCoinFlip.getEcon().depositPlayer(winner, amount*2);

                ActionbarUtil.sendActionBar(winner, FileManager.getMessage("win").replace("{amount}", format(amount*2)));
                ActionbarUtil.sendActionBar(loser, FileManager.getMessage("loss").replace("{amount}", format(amount*2)));

                winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                loser.playSound(loser.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);

                FlipManager.updateSQLPlayer(winner.getUniqueId());
                FlipManager.updateSQLPlayer(loser.getUniqueId());

                FlipManager.addWon(winner.getUniqueId(), 1);
                FlipManager.addLost(loser.getUniqueId(), 1);
            }, 20L * timer);
        }
    }
}
