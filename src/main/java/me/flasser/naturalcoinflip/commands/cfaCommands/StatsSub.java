package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsSub extends SubCommand {

    public StatsSub(String name, String... aliases) {
        super("stats", "stats");
        setPermission("stats");
    }

    @Override
    public String getDescription() {
        return "View stats of a player.";
    }

    @Override
    public String getUsage() {
        return "/cf stats <player>";
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

        FlipManager.PlayerInfo info = FlipManager.getPlayerInfo(target.getUniqueId());

        List<String> newestLoreList = new ArrayList<>();
        String newLore;
        for (String lore : FileManager.getListMessage("others_flip_stats")) {
            newLore = lore
                    .replace("{player}", Bukkit.getOfflinePlayer(info.UUID).getName())
                    .replace("{won}", String.valueOf(info.wins))
                    .replace("{lost}", String.valueOf(info.loses))
                    .replace("{total}", String.valueOf(info.wins+info.loses));

            newestLoreList.add(newLore);
        }
        String[] newestLore = newestLoreList.toArray(new String[0]);
        player.sendMessage(newestLore);

    }
}