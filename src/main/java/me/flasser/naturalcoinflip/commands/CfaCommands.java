package me.flasser.naturalcoinflip.commands;

import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.CacheManager;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.FlipManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.format;
import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.formatInt;

@Component
@Async
@Command(
        label = "cfa",
        aliases = {"cfadmin", "coinfa", "cflipa", "coinflipadmin"}
)
@Permission({"naturalstuff.*", "naturalcoinflip.*", "naturalcoinflip.cfa.*", "naturalcoinflip.cfa"})
public class CfaCommands {
    private final NaturalCoinFlip plugin;

    @Inject
    public CfaCommands(NaturalCoinFlip plugin) {
        this.plugin = plugin;
    }

    @Inject
    private FileManager fileManager;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private FlipManager flipManager;

    /* ---------------- DEFAULT ---------------- */

    @Executor(pattern = "")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void defaultCommand(CommandSender sender) {
        sender.sendMessage("d");
    }

    /* ---------------- ADDLOSS ---------------- */

    @Executor(pattern = "addloss")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void addloss(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "addloss")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void addloss(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("addloss_not_specified"));
    }

    @Executor(pattern = "addloss")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void addloss(CommandSender sender, @Arg("player") Player target, @Arg("amount") String raw) {
        Player player = (Player) sender;

        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.addLost(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("addloss_added")
                .replace("{amount}", format(amount))
                .replace("{player}", target.getName()));
    }

    @Executor(pattern = "remloss")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void remloss(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "remloss")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void remloss(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("remloss_not_specified"));
    }

    @Executor(pattern = "remloss")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void remloss(CommandSender sender, @Arg("player") Player target, @Arg("amount") String raw) {
        Player player = (Player) sender;

        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.removeLost(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("remloss_removed")
                .replace("{amount}", format(amount))
                .replace("{player}", target.getName()));
    }

    /* ---------------- ADDWIN ---------------- */

    @Executor(pattern = "addwin")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void addwin(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "addwin")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void addwin(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("addwin_not_specified"));
    }

    @Executor(pattern = "addwin")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void addwin(CommandSender sender, @Arg("player") Player target, @Arg("amount") String raw) {
        Player player = (Player) sender;
        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.addWon(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("addwin_added")
                .replace("{amount}", format(amount))
                .replace("{player}", target.getName()));
    }

    @Executor(pattern = "remwin")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void remwin(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "remwin")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void remwin(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("remwin_not_specified"));
    }

    @Executor(pattern = "remwin")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void remwin(CommandSender sender, @Arg("player") Player target, @Arg("amount") String raw) {
        Player player = (Player) sender;

        if (!target.hasPlayedBefore()) {
            player.sendMessage(fileManager.getMessage("player_not_joined"));
            return;
        }

        Integer amount = formatInt(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        flipManager.removeWon(target.getUniqueId(), amount);

        player.sendMessage(fileManager.getMessage("remwin_removed")
                .replace("{amount}", format(amount))
                .replace("{player}", target.getName()));
    }


    /* ---------------- DELETE ---------------- */

    @Executor(pattern = "delete")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void delete(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "delete")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void delete(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;

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
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void info(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "info")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void info(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;

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
                    .replace("{amount}", format(info.amount)));
        }

        player.sendMessage(out.toArray(new String[0]));
    }

    /* ---------------- RESET ---------------- */

    @Executor(pattern = "reset")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void reset(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "reset")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void reset(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;

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
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void stats(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "stats")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void stats(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;

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
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void tovoid(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("player_not_specified"));
    }

    @Executor(pattern = "void")
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void tovoid(CommandSender sender, @Arg("player") Player target) {
        Player player = (Player) sender;

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
    @Permission({"naturalstuff.*", "naturalcombat.*", "naturalcombat.cta.*", "naturalcombat.cta.reload"})
    public void reload(CommandSender sender) {
        plugin.saveDefaultConfig();
        fileManager.createMessages();
        cacheManager.createCache();
        sender.sendMessage(fileManager.getMessage("reload_success"));
    }
}