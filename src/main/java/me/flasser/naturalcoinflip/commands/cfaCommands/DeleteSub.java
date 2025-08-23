package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DeleteSub extends SubCommand {

    public DeleteSub(String name, String... aliases) {
        super("delete", "del", "rem");
        setPermission("delete");
        setCompletion(1, "subcommand");
        setCompletion(2, "player");
    }

    @Override
    public String getDescription() {
        return "Delete a player's coinflip.";
    }

    @Override
    public String getUsage() {
        return "/cfa delete <player>";
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

        if (!FlipManager.hasFlip(target.getUniqueId())) {
            player.sendMessage(FileManager.getMessage("others_flip_not_up"));
            return;
        }

        NaturalCoinFlip.getEcon().depositPlayer(target, Objects.requireNonNull(FlipManager.getFlipInfo(target.getUniqueId())).amount);
        Bukkit.getScheduler().runTaskAsynchronously(NaturalCoinFlip.getInstance(), () -> {
            FlipManager.removeFlip(target.getUniqueId());

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                player.sendMessage(FileManager.getMessage("others_flip_deleted"));
            });
        });
    }
}
