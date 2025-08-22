package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.format;

public class AddLossSub extends SubCommand {

    public AddLossSub(String name, String... aliases) {
        super("addloss", "addloss");
        setPermission("addloss");
    }

    @Override
    public String getDescription() {
        return "Add losses to a player.";
    }

    @Override
    public String getUsage() {
        return "/cfa addloss <player> <amount>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(FileManager.getMessage("player_not_specified"));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(FileManager.getMessage("addloss_not_specified"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(FileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount;
        try {
            amount = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(FileManager.getMessage("not_a_number"));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(NaturalCoinFlip.getInstance(), () -> {
            FlipManager.addLost(target.getUniqueId(), amount);

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                player.sendMessage(FileManager.getMessage("addloss_added")
                        .replace("{amount}", format(amount))
                        .replace("{player}", target.getName()));
            });
        });
    }
}
