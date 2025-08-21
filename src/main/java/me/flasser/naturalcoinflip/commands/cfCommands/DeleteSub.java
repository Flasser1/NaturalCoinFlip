package me.flasser.naturalcoinflip.commands.cfCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteSub extends SubCommand {

    public DeleteSub(String name, String... aliases) {
        super("delete", "remove", "del", "rem");
    }

    @Override
    public String getDescription() {
        return "Delete your CoinFlip";
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
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        NaturalCoinFlip.getEcon().depositPlayer(player, FlipManager.getFlipInfo(player.getUniqueId()).amount);
        FlipManager.removeFlip(player.getUniqueId());
        player.sendMessage(FileManager.getMessage("flip_deleted"));

    }
}
