package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetSub extends SubCommand {

    public ResetSub(String name, String... aliases) {
        super("reset");
        setPermission("reset");
        setCompletion(0, "subcommand");
        setCompletion(1, "player");
    }

    @Override
    public String getDescription() {
        return "Reset a player's stats.";
    }

    @Override
    public String getUsage() {
        return "/cfa reset <player>";
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
            FlipManager.resetPlayer(target.getUniqueId());

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                player.sendMessage(FileManager.getMessage("others_reset")
                        .replace("{player}", target.getName()));
            });
        });
    }
}
