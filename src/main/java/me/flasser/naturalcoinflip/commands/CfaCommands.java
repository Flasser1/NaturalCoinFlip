package me.flasser.naturalcoinflip.commands;

import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Context;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.CacheManager;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.utility.misc.FormatNumber;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@Command(
        label = "cfa",
        aliases = {"cfadmin", "coinfa", "cflipa", "coinflipadmin"}
)
@Async
@Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa"})
public class CfaCommands implements CommandService {
    private @Inject NaturalCoinFlip plugin;
    private @Inject FileManager fileManager;
    private @Inject CacheManager cacheManager;
    private @Inject FlipManager flipManager;
    private @Inject FormatNumber formatNumber;

    /* ---------------- DEFAULT ---------------- */

    @Executor
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa"})
    public void _def(@Context Player player) {
        player.sendMessage("d");
    }

    /* ---------------- ADDLOSS ---------------- */

    @Executor(pattern = "addloss")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.addloss"})
    public void addloss(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "addloss <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.addloss"})
    public void addloss(@Context Player player, @Arg("player") Player target) {
        player.sendMessage(fileManager.getMessage("addloss_not_specified"));
    }

    @Executor(pattern = "addloss <player> <amount>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.addloss"})
    public void addloss(@Context Player player, @Arg("player") Player target, @Arg("amount") String raw) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatNumber.formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.addLost(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("addloss_added")
                .replace("{amount}", formatNumber.format(amount))
                .replace("{player}", target.getName()));
    }

    @Executor(pattern = "remloss")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.remloss"})
    public void remloss(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "remloss <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.remloss"})
    public void remloss(@Context Player player, @Arg("player") Player target) {
        player.sendMessage(fileManager.getMessage("remloss_not_specified"));
    }

    @Executor(pattern = "remloss <player> <amount>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.remloss"})
    public void remloss(@Context Player player, @Arg("player") Player target, @Arg("amount") String raw) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatNumber.formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.removeLost(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("remloss_removed")
                .replace("{amount}", formatNumber.format(amount))
                .replace("{player}", target.getName()));
    }

    /* ---------------- ADDWIN ---------------- */

    @Executor(pattern = "addwin")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.addwin"})
    public void addwin(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "addwin <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.addwin"})
    public void addwin(@Context Player player, @Arg("player") Player target) {
        player.sendMessage(fileManager.getMessage("addwin_not_specified"));
    }

    @Executor(pattern = "addwin <player> <amount>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.addwin"})
    public void addwin(@Context Player player, @Arg("player") Player target, @Arg("amount") String raw) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatNumber.formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.addWon(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("addwin_added")
                .replace("{amount}", formatNumber.format(amount))
                .replace("{player}", target.getName()));
    }

    @Executor(pattern = "remwin")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.remwin"})
    public void remwin(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "remwin <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.remwin"})
    public void remwin(@Context Player player, @Arg("player") Player target) {
        player.sendMessage(fileManager.getMessage("remwin_not_specified"));
    }

    @Executor(pattern = "remwin <player> <amount>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.remwin"})
    public void remwin(@Context Player player, @Arg("player") Player target, @Arg("amount") String raw) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatNumber.formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.removeWon(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("remwin_removed")
                .replace("{amount}", formatNumber.format(amount))
                .replace("{player}", target.getName()));
    }


    /* ---------------- DELETE ---------------- */

    @Executor(pattern = "delete")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.delete"})
    public void delete(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "delete <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.delete"})
    public void delete(@Context Player player, @Arg("player") Player target) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        if (!flipManager.hasFlip(target.getUniqueId())) {
            player.sendMessage(fileManager.getMessage("others_flip_not_up"));
            return;
        }

        double amount = Objects.requireNonNull(
                flipManager.getFlipInfo(target.getUniqueId())
        ).amount;

        NaturalCoinFlip.getEcon().depositPlayer(target, amount);
        flipManager.removeFlip(target.getUniqueId());

        player.sendMessage(fileManager.getMessage("others_flip_deleted"));
    }

    /* ---------------- INFO ---------------- */

    @Executor(pattern = "info")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.info"})
    public void info(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "info <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.info"})
    public void info(@Context Player player, @Arg("player") Player target) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        FlipManager.FlipInfo info = flipManager.getFlipInfo(target.getUniqueId());

        List<String> out = new ArrayList<>();
        for (String line : fileManager.getListMessage("others_flip_info")) {
            out.add(line
                    .replace("{player}", target.getName())
                    .replace("{date}", new Date(info.creation).toString())
                    .replace("{amount}", formatNumber.format(info.amount)));
        }

        player.sendMessage(out.toArray(new String[0]));
    }

    /* ---------------- RESET ---------------- */

    @Executor(pattern = "reset")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.reset"})
    public void reset(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "reset <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.reset"})
    public void reset(@Context Player player, @Arg("player") Player target) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        flipManager.resetPlayer(target.getUniqueId());

        player.sendMessage(fileManager.getMessage("others_reset")
                .replace("{player}", target.getName()));
    }

    /* ---------------- STATS ---------------- */

    @Executor(pattern = "stats")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.stats"})
    public void stats(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "stats <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.stats"})
    public void stats(@Context Player player, @Arg("player") Player target) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        FlipManager.PlayerInfo info = flipManager.getPlayerInfo(target.getUniqueId());

        int wins = info != null ? info.wins : 0;
        int loses = info != null ? info.loses : 0;
        int total = wins + loses;
        double ratio = total == 0 ? 0 : (100.0 * wins / total);

        List<String> out = new ArrayList<>();
        for (String line : fileManager.getListMessage("others_flip_stats")) {
            out.add(line
                    .replace("{player}", target.getName())
                    .replace("{won}", String.valueOf(wins))
                    .replace("{lost}", String.valueOf(loses))
                    .replace("{ratio}", cacheManager.getStatsDecimal().format(ratio) + "%")
                    .replace("{total}", String.valueOf(total)));
        }

        player.sendMessage(out.toArray(new String[0]));
    }

    /* ---------------- VOID ---------------- */

    @Executor(pattern = "void")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.void"})
    public void tovoid(@Context Player player) {
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "void <player>")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.void"})
    public void tovoid(@Context Player player, @Arg("player") Player target) {
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        if (!flipManager.hasFlip(target.getUniqueId())) {
            player.sendMessage(fileManager.getMessage("others_flip_not_up"));
            return;
        }

        flipManager.removeFlip(target.getUniqueId());
        player.sendMessage(fileManager.getMessage("others_flip_deleted"));
    }

    /* ---------------- RELOAD ---------------- */

    @Executor(pattern = "reload")
    @Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa.reload"})
    public void reload(@Context Player player) {
        plugin.saveDefaultConfig();
        fileManager.createMessages();
        cacheManager.createCache();
        player.sendMessage(fileManager.getMessage("reload_success"));
    }
}