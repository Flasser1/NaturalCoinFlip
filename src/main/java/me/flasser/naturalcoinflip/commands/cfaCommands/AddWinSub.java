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
import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.formatInt;

public class AddWinSub extends SubCommand {

    public AddWinSub(String name, String... aliases) {
        super("addwin", "addwon");
        setPermission("addwin");
        setCompletion(0, "subcommand");
        setCompletion(1, "player");
        setCompletion(2, "integer");
    }

    @Override
    public String getDescription() {
        return "Add wins to a player.";
    }

    @Override
    public String getUsage() {
        return "/cfa addwin <player> <amount>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(FileManager.getMessage("player_not_specified"));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(FileManager.getMessage("addwin_not_specified"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(FileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatInt(args[2]);
        if (amount == null) {
            player.sendMessage(FileManager.getMessage("not_a_number"));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(NaturalCoinFlip.getInstance(), () -> {
            FlipManager.addWon(target.getUniqueId(), amount);

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                player.sendMessage(FileManager.getMessage("addwin_added")
                        .replace("{amount}", format(amount))
                        .replace("{player}", target.getName()));
            });
        });
    }
}