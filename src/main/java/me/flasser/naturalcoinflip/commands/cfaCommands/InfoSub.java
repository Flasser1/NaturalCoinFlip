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
import java.util.Date;
import java.util.List;

import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.format;

public class InfoSub extends SubCommand {

    public InfoSub(String name, String... aliases) {
        super("info");
        setPermission("info");
        setCompletion(0, "subcommand");
        setCompletion(1, "player");
    }

    @Override
    public String getDescription() {
        return "View info of a coinflip.";
    }

    @Override
    public String getUsage() {
        return "/cfa info <player>";
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
            FlipManager.FlipInfo info = FlipManager.getFlipInfo(target.getUniqueId());

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                List<String> newestLoreList = new ArrayList<>();
                String newLore;
                for (String lore : FileManager.getListMessage("others_flip_info")) {
                    assert info != null;
                    newLore = lore
                            .replace("{player}", Bukkit.getOfflinePlayer(info.UUID).getName())
                            .replace("{date}", String.valueOf(new Date(info.creation)))
                            .replace("{amount}", format(info.amount));

                    newestLoreList.add(newLore);
                }
                String[] newestLore = newestLoreList.toArray(new String[0]);
                player.sendMessage(newestLore);
            });
        });

    }
}