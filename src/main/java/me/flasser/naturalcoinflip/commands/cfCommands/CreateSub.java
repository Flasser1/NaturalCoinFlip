package me.flasser.naturalcoinflip.commands.cfCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSub extends SubCommand {

    public CreateSub(String name, String... aliases) {
        super("create",  "make", "put");
    }

    @Override
    public String getDescription() {
        return "Create a CoinFlip";
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
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        if (args.length < 2) {
            player.sendMessage(FileManager.getMessage("amount_not_specified"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        Integer amount = Integer.valueOf(args[1]);

        if (amount < NaturalCoinFlip.getInstance().getConfig().getInt("minFlip")) {
            player.sendMessage(FileManager.getMessage("below_min")
                    .replace("{min}", ""+NaturalCoinFlip.getInstance().getConfig().getInt("minFlip")
            ));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        if (!(NaturalCoinFlip.getEcon().getBalance(player) >= amount)) {
            player.sendMessage(FileManager.getMessage("insufficient_funds_create"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        FlipManager.updateSQLPlayer(player.getUniqueId());
        FlipManager.addFlip(player.getUniqueId(), amount);
        NaturalCoinFlip.getEcon().withdrawPlayer(player, amount);
        player.sendMessage(FileManager.getMessage("flip_created"));

    }
}
