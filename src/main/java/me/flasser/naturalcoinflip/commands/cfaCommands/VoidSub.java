package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoidSub extends SubCommand {

    public VoidSub(String name, String... aliases) {
        super("void", "void");
        setPermission("void");
    }

    @Override
    public String getDescription() {
        return "Delete your CoinFlip";
    }

    @Override
    public String getUsage() {
        return "/cf delete <player>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(FileManager.getMessage("player_not_specified"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(FileManager.getMessage("player_not_joined"));
            return;
        }

        if (!FlipManager.hasFlip(target.getUniqueId())) {
            player.sendMessage(FileManager.getMessage("others_flip_not_up"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        FlipManager.removeFlip(target.getUniqueId());
        player.sendMessage(FileManager.getMessage("others_flip_deleted"));

    }
}