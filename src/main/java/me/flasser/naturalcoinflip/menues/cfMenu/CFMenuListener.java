package me.flasser.naturalcoinflip.menues.cfMenu;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.utility.actionbarUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CFMenuListener {
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (!e.getClickedInventory().getName().contains("§e§lCOINFLIP §8- §fPAGE")) {
            return;
        }

        Integer page = Integer.valueOf(e.getClickedInventory().getName().replace("§e§lCOINFLIP §8- §fPAGE ", ""));

        e.setCancelled(true);
        if (e.getCurrentItem().getType() == Material.ARROW) {
            if (e.getRawSlot() == 53) {
                CFMenu.openCFMenu(player, page);
            } else if (e.getRawSlot() == 45) {
                CFMenu.openCFMenu(player, page-2);
            }
        }

        if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {
            player.closeInventory();
            SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
            UUID UUID = java.util.UUID.fromString(meta.getOwner());

            if (!FlipManager.hasFlip(UUID)) {
                player.sendMessage("§7This coinflip is either already taken, or does not exist.");
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            if (UUID == player.getUniqueId()) {
                player.sendMessage("§7You can't take your own coinflip.");
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            Integer amount = FlipManager.getFlipInfo(UUID).amount;

            if (NaturalCoinFlip.getEcon().getBalance(String.valueOf(UUID)) < amount) {
                player.sendMessage("§7You do not have sufficient funds for this coinflip.");
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            FlipManager.removeFlip(UUID);
            NaturalCoinFlip.getEcon().withdrawPlayer(player, amount);
            Integer timer = NaturalCoinFlip.getInstance().getConfig().getInt("time");
            for (int i = 0; i < timer; i++) {
                int secondsLeft = timer - i;

                Bukkit.getScheduler().runTaskLater(NaturalCoinFlip.getInstance(), () -> {
                    actionbarUtil.sendActionBar(player, "§7Finding a winner in §f" + secondsLeft + "s");
                    actionbarUtil.sendActionBar(Bukkit.getPlayer(UUID), "§7Finding a winner in §f" + secondsLeft + "s");

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

                actionbarUtil.sendActionBar(winner, "§7You won this coinflip of §f$"+ amount*2);
                actionbarUtil.sendActionBar(loser, "§7You lost this coinflip of §f$"+ amount*2);

                winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                loser.playSound(loser.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }, 20L * timer);
        }
    }
}
