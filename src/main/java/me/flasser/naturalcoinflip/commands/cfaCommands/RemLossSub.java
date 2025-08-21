package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemLossSub extends SubCommand {

    public RemLossSub(String name, String... aliases) {
        super("remloss");
        setPermission("remloss");
    }

    @Override
    public String getDescription() {
        return "Removes a specified amount of losses from a player.";
    }

    @Override
    public String getUsage() {
        return "/cf remloss <player> <amount>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(FileManager.getMessage("player_not_specified"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        if (args.length < 3) {
            player.sendMessage(FileManager.getMessage("remloss_not_specified"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(FileManager.getMessage("player_not_joined"));
            return;
        }
        Integer amount = Integer.valueOf(args[2]);

        FlipManager.removeLost(target.getUniqueId(), amount);
        player.sendMessage(FileManager.getMessage("remloss_removed")
                .replace("{amount}", amount.toString())
                .replace("{player}", target.getName()));
    }
}
