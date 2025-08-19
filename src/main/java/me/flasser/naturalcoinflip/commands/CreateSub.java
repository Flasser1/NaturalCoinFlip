package me.flasser.naturalcoinflip.commands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSub extends SubCommand {

    public CreateSub(String name, String... aliases) {
        super("create", "make", "put");
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
            player.sendMessage("§7You already have a coinflip up.");
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        if (args.length < 2) {
            player.sendMessage("§7You need to specify the amount you want to bet.");
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        Integer amount = Integer.valueOf(args[1]);

        if (amount < NaturalCoinFlip.getInstance().getConfig().getInt("minFlip")) {
            player.sendMessage("§7This amount is below the minimum amount of §f$" + NaturalCoinFlip.getInstance().getConfig().getInt("minFlip") + "§7.");
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        if (!(NaturalCoinFlip.getEcon().getBalance(player) >= amount)) {
            player.sendMessage("§7You do not have the sufficient funds to create this bet.");
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        FlipManager.addFlip(player.getUniqueId(), amount);

    }
}
