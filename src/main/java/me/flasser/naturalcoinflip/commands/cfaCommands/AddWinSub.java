package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddWinSub extends SubCommand {

    public AddWinSub(String name, String... aliases) {
        super("addwin", "addwin");
        setPermission("addwin");
    }

    @Override
    public String getDescription() {
        return "Adds a specified amount of wins to a player.";
    }

    @Override
    public String getUsage() {
        return "/cf addwin <player> <amount>";
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
            player.sendMessage(FileManager.getMessage("addwin_not_specified"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(FileManager.getMessage("player_not_joined"));
            return;
        }
        Integer amount = Integer.valueOf(args[2]);

        FlipManager.addWon(target.getUniqueId(), amount);
        player.sendMessage(FileManager.getMessage("addwin_added")
                .replace("{amount}", amount.toString())
                .replace("{player}", target.getName()));
    }
}