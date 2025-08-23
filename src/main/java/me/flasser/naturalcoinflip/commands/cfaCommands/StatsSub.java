package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.flasser.naturalcoinflip.managers.CacheManager.STATS_DECIMAL;

public class StatsSub extends SubCommand {

    public StatsSub(String name, String... aliases) {
        super("stats", "stats");
        setPermission("stats");
        setCompletion(0, "subcommand");
        setCompletion(1, "player");
    }

    @Override
    public String getDescription() {
        return "View stats of a player.";
    }

    @Override
    public String getUsage() {
        return "/cfa stats <player>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(FileManager.getMessage("player_not_specified"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(FileManager.getMessage("player_not_joined"));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(NaturalCoinFlip.getInstance(), () -> {
            FlipManager.PlayerInfo info = FlipManager.getPlayerInfo(target.getUniqueId());
            int wins = info != null && info.wins != null ? info.wins : 0;
            int loses = info != null && info.loses != null ? info.loses : 0;
            int total = info != null ? wins+loses : 0;
            double ratio = info != null && total != 0 ? 100.0*wins/total : 0;

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                List<String> newestLoreList = new ArrayList<>();
                String newLore;
                for (String lore : FileManager.getListMessage("others_flip_stats")) {
                    assert info != null;
                    newLore = lore
                            .replace("{player}", Bukkit.getOfflinePlayer(info.UUID).getName())
                            .replace("{won}", String.valueOf(info.wins))
                            .replace("{lost}", String.valueOf(info.loses))
                            .replace("{ratio}", STATS_DECIMAL.format(ratio)+"%")
                            .replace("{total}", String.valueOf(total));

                    newestLoreList.add(newLore);
                }
                String[] newestLore = newestLoreList.toArray(new String[0]);
                player.sendMessage(newestLore);
            });
        });

    }
}