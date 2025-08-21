package me.flasser.naturalcoinflip.utility.commandUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SimpleCommandGroup implements CommandExecutor {

    private final List<SubCommand> subCommands = new ArrayList<>();
    private BiConsumer<CommandSender, String[]> defaultAction;
    private String permission;

    public void register(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public void setDefaultAction(BiConsumer<CommandSender, String[]> action) {
        this.defaultAction = action;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (
                    permission != null &&
                    !(sender.hasPermission("NaturalCoinFlip."+permission) ||
                    sender.hasPermission("NaturalCoinFlip.*")))
            {
                sender.sendMessage("§7Missing permission§8: §fNaturalCoinFlip." + permission);
                return true;
            }
            if (defaultAction != null) {
                defaultAction.accept(sender, args);
                return true;
            }
            sender.sendMessage(new String[]{"§f§lAvailable subcommands:", ""});
            for (SubCommand sub : subCommands) {
                if (
                        permission == null ||
                        sender.hasPermission("NaturalCoinFlip."+permission+"."+sub.getPerm()) ||
                        sender.hasPermission("NaturalCoinFlip."+permission+".*") ||
                        sender.hasPermission("NaturalCoinFlip.*"))
                {
                    sender.sendMessage("§8│ §f" + sub.getUsage() + " §8›› §7" + sub.getDescription());
                }
            }
            return true;
        }

        for (SubCommand sub : subCommands) {
            if (sub.matches(args[0])) {
                if (
                        permission != null &&
                        !(sender.hasPermission("NaturalCoinFlip."+permission+"."+sub.getPerm()) &&
                        sender.hasPermission("NaturalCoinFlip."+permission+".*") &&
                        sender.hasPermission("NaturalCoinFlip.*")))
                {
                    sender.sendMessage("§7Missing permission§8: §fNaturalCoinFlip."+permission+"."+sub.getPerm());
                    return true;
                } else {
                    sub.execute(sender, args);
                    return true;
                }
            }
        }

        sender.sendMessage("§cUnknown subcommand! Try /" + label);
        return true;
    }
}
