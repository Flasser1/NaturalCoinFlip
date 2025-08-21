package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddLossSub extends SubCommand {

    public AddLossSub(String name, String... aliases) {
        super("addloss", "addloss");
        setPermission("addloss");
    }

    @Override
    public String getDescription() {
        return "Adds a specified amount of losses to a player.";
    }

    @Override
    public String getUsage() {
        return "/cf addloss <player> <amount>";
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
            player.sendMessage(FileManager.getMessage("addloss_not_specified"));
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore()) {
            player.sendMessage(FileManager.getMessage("player_not_joined"));
            return;
        }
        Integer amount = Integer.valueOf(args[2]);

        FlipManager.addLost(target.getUniqueId(), amount);
        player.sendMessage(FileManager.getMessage("addloss_added")
                .replace("{amount}", amount.toString())
                .replace("{player}", target.getName()));
    }
}
