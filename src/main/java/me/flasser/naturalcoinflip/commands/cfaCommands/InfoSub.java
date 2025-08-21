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
import java.util.Date;
import java.util.List;

public class InfoSub extends SubCommand {

    public InfoSub(String name, String... aliases) {
        super("info");
        setPermission("info");
    }

    @Override
    public String getDescription() {
        return "View info of a coinflip.";
    }

    @Override
    public String getUsage() {
        return "/cf info <player>";
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
        FlipManager.FlipInfo info = FlipManager.getFlipInfo(target.getUniqueId());

        List<String> newestLoreList = new ArrayList<>();
        String newLore;
        for (String lore : FileManager.getListMessage("others_flip_info")) {
            newLore = lore
                    .replace("{player}", Bukkit.getOfflinePlayer(info.UUID).getName())
                    .replace("{date}", String.valueOf(new Date(info.creation)))
                    .replace("{amount}", String.valueOf(info.amount));

            newestLoreList.add(newLore);
        }
        String[] newestLore = newestLoreList.toArray(new String[0]);
        player.sendMessage(newestLore);

    }
}