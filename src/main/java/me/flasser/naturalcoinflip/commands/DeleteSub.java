package me.flasser.naturalcoinflip.commands;

import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteSub extends SubCommand {

    public DeleteSub(String name, String... aliases) {
        super("create", "remove", "del", "rem");
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

        if (!FlipManager.hasFlip(player.getUniqueId())) {
            player.sendMessage("§7You don't have any flips up.");
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        FlipManager.removeFlip(player.getUniqueId());

    }
}
