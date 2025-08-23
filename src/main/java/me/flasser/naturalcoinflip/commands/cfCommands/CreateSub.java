package me.flasser.naturalcoinflip.commands.cfCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.formatDou;

public class CreateSub extends SubCommand {

    public CreateSub(String name, String... aliases) {
        super("create",  "make", "put");
        setCompletion(0, "subcommand");
        setCompletion(1, "integer");
    }

    @Override
    public String getDescription() {
        return "Create a coinflip.";
    }

    @Override
    public String getUsage() {
        return "/cf create <int>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (FlipManager.hasFlip(player.getUniqueId())) {
            player.sendMessage(FileManager.getMessage("flip_already_up"));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(FileManager.getMessage("amount_not_specified"));
            return;
        }

        Double amount = formatDou(args[1]);
        if (amount == null) {
            player.sendMessage(FileManager.getMessage("not_a_number"));
            return;
        }

        if (amount < NaturalCoinFlip.getInstance().getConfig().getDouble("minFlip")) {
            player.sendMessage(FileManager.getMessage("below_min")
                    .replace("{min}", ""+NaturalCoinFlip.getInstance().getConfig().getInt("minFlip")
            ));
            return;
        }

        if (!(NaturalCoinFlip.getEcon().getBalance(player) >= (amount))) {
            player.sendMessage(FileManager.getMessage("insufficient_funds_create"));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(NaturalCoinFlip.getInstance(), () -> {
            FlipManager.updateSQLPlayer(player.getUniqueId());
            FlipManager.addFlip(player.getUniqueId(), amount);

            Bukkit.getScheduler().runTask(NaturalCoinFlip.getInstance(), () -> {
                NaturalCoinFlip.getEcon().withdrawPlayer(player, amount);
                player.sendMessage(FileManager.getMessage("flip_created"));
            });
        });
    }
}
