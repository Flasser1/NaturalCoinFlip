package me.flasser.naturalcoinflip.commands;

import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.menues.CFMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static me.flasser.naturalcoinflip.utility.misc.FormatNumber.formatDou;

@Component
@Async
@Command(
        label = "cf",
        aliases = {"coinflip", "cflip", "coinf"}
)
public class CfCommands {
    private final NaturalCoinFlip plugin;

    @Inject
    public CfCommands(NaturalCoinFlip plugin) {
        this.plugin = plugin;
    }

    @Inject
    private FileManager fileManager;

    @Inject
    private CFMenu cfMenu;

    @Inject
    private FlipManager flipManager;

    /* ---------------- DEFAULT ---------------- */
    @Executor(pattern = "")
    public void defaultCommand(CommandSender sender) {
        Player player = (Player) sender;

        flipManager.updateSQLPlayer(player.getUniqueId());
        cfMenu.openCFMenu(player, 0);
    }

    /* ---------------- CREATE ---------------- */
    @Executor(pattern = "create")
    public void create(CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage(fileManager.getMessage("amount_not_specified"));
    }

    @Executor(pattern = "create")
    public void create(CommandSender sender, @Arg("amount") String raw) {
        Player player = (Player) sender;

        if (flipManager.hasFlip(player.getUniqueId())) {
            player.sendMessage(fileManager.getMessage("flip_already_up"));
            return;
        }

        Double amount = formatDou(raw);
        if (amount == null) {
            player.sendMessage(fileManager.getMessage("not_a_number"));
            return;
        }

        if (amount < plugin.getConfig().getDouble("minFlip")) {
            player.sendMessage(fileManager.getMessage("below_min")
                    .replace("{min}", String.valueOf(plugin.getConfig().getInt("minFlip"))));
            return;
        }

        if (NaturalCoinFlip.getEcon().getBalance(player) < amount) {
            player.sendMessage(fileManager.getMessage("insufficient_funds_create"));
            return;
        }

        flipManager.updateSQLPlayer(player.getUniqueId());
        flipManager.addFlip(player.getUniqueId(), amount);

        NaturalCoinFlip.getEcon().withdrawPlayer(player, amount);
        player.sendMessage(fileManager.getMessage("flip_created"));
    }

    /* ---------------- DELETE ---------------- */
    @Executor(pattern = "delete")
    public void delete(CommandSender sender) {
        Player player = (Player) sender;

        if (!flipManager.hasFlip(player.getUniqueId())) {
            player.sendMessage(fileManager.getMessage("flip_not_up"));
            return;
        }

        double amount = Objects.requireNonNull(flipManager.getFlipInfo(player.getUniqueId())).amount;
        NaturalCoinFlip.getEcon().depositPlayer(player, amount);
        flipManager.removeFlip(player.getUniqueId());

        player.sendMessage(fileManager.getMessage("flip_deleted"));
    }
}