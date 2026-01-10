package me.flasser.naturalcoinflip.menues;

import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
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

@Component
@Async
public class CFMenuListener implements Listener {
    private final NaturalCoinFlip plugin;

    @Inject
    public CFMenuListener(NaturalCoinFlip plugin) {
        this.plugin = plugin;
    }

    @Inject
    private FileManager fileManager;

    @Inject
    private CFMenu cfMenu;

    @Inject
    private FlipManager flipManager;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) {
            return;
        }

        if (!e.getClickedInventory().getName().contains(fileManager.getMessage("inventory_name"))) {
            return;
        }

        int page = Integer.parseInt(e.getClickedInventory().getName().replace(fileManager.getMessage("inventory_name"), ""));

        e.setCancelled(true);
        if (e.getCurrentItem().getType() == Material.ARROW) {
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);
            if (e.getRawSlot() == 53) {
                cfMenu.openCFMenu(player, page);
            } else if (e.getRawSlot() == 45) {
                cfMenu.openCFMenu(player, page-2);
            }
            return;
        }

        if (
                e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE ||
                        e.getCurrentItem().getType() == Material.BOOK_AND_QUILL ||
                        e.getCurrentItem().getItemMeta().getDisplayName().contains(fileManager.getMessage("stats_head_name")))
        {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(fileManager.getMessage("update_head_name")))) {
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);
            cfMenu.openCFMenu(player, page-1);
            return;
        }

        if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {
            player.closeInventory();
            SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
            UUID UUID = meta.getOwner() != null ? Bukkit.getOfflinePlayer(meta.getOwner()).getUniqueId() : null;

            if (!flipManager.hasFlip(UUID)) {
                player.sendMessage(fileManager.getMessage("flip_already_taken"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            if (UUID == player.getUniqueId()) {
                player.sendMessage(fileManager.getMessage("click_own_flip"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            double amount = Objects.requireNonNull(flipManager.getFlipInfo(UUID)).amount;

            if (NaturalCoinFlip.getEcon().getBalance(player) < amount) {
                player.sendMessage(fileManager.getMessage("insufficient_funds_take"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
                return;
            }

            flipManager.removeFlip(UUID);
            NaturalCoinFlip.getEcon().withdrawPlayer(player, amount);
            int timer = plugin.getConfig().getInt("time");
            for (int i = 0; i < timer; i++) {
                int secondsLeft = timer - i;

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    ActionbarUtil.sendActionBar(player, fileManager.getMessage("countdown").replace("{timer}", String.valueOf(secondsLeft)));
                    ActionbarUtil.sendActionBar(Bukkit.getPlayer(UUID), fileManager.getMessage("countdown").replace("{timer}", String.valueOf(secondsLeft)));

                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);
                    Bukkit.getPlayer(UUID).playSound(Bukkit.getPlayer(UUID).getLocation(), Sound.WOOD_CLICK, 1.0f, 1.0f);

                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                    Bukkit.getPlayer(UUID).playSound(Bukkit.getPlayer(UUID).getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                }, 20L * i);
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                List<Player> players = new ArrayList<>();
                players.add(player);
                players.add(Bukkit.getPlayer(UUID));

                Player winner = players.get(new Random().nextInt(players.size()));
                players.remove(winner);
                Player loser = players.get(0);

                NaturalCoinFlip.getEcon().depositPlayer(winner, amount*2*(1-plugin.getConfig().getLong("tax")));

                ActionbarUtil.sendActionBar(winner, fileManager.getMessage("win").replace("{amount}", format(amount*2)));
                ActionbarUtil.sendActionBar(loser, fileManager.getMessage("loss").replace("{amount}", format(amount*2)));

                winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                loser.playSound(loser.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);

                flipManager.updateSQLPlayer(winner.getUniqueId());
                flipManager.updateSQLPlayer(loser.getUniqueId());

                flipManager.addWon(winner.getUniqueId(), 1);
                flipManager.addLost(loser.getUniqueId(), 1);
            }, 20L * timer);
        }
    }
}
