package me.flasser.naturalcoinflip.commands.cfCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DeleteSub extends SubCommand {

    public DeleteSub(String name, String... aliases) {
        super("delete", "remove", "del", "rem");
        setCompletion(0, "subcommand");
    }

    @Override
    public String getDescription() {
        return "Delete your coinflip.";
    }

    @Override
    public String getUsage() {
        return "/cf delete";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (!FlipManager.hasFlip(player.getUniqueId())) {
            player.sendMessage(FileManager.getMessage("flip_not_up"));
            return;
        }

        NaturalCoinFlip.getEcon().depositPlayer(player, Objects.requireNonNull(FlipManager.getFlipInfo(player.getUniqueId())).amount);
        Bukkit.getScheduler().runTaskAsynchronously(NaturalCoinFlip.getInstance(), () -> {
            FlipManager.removeFlip(player.getUniqueId());

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                player.sendMessage(FileManager.getMessage("flip_deleted"));
            });
        });
    }
}
