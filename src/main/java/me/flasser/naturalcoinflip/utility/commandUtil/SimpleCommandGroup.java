package me.flasser.naturalcoinflip.utility.commandUtil;

import me.flasser.naturalcoinflip.managers.FileManager;
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
                    !(sender.hasPermission("natural."+permission+".*") ||
                    sender.hasPermission("natural."+permission) ||
                    sender.hasPermission("natural.*")))
            {
                sender.sendMessage(FileManager.getMessage("insufficient_permissions").replace("{permission}", "natural."+permission));
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
                        sender.hasPermission("natural."+permission+"."+sub.getPerm()) ||
                        sender.hasPermission("natural."+permission+".*") ||
                        sender.hasPermission("natural.*"))
                {
                    sender.sendMessage("§8│ §f" + sub.getUsage() + " §8›› §7" + sub.getDescription());
                }
            }
            return true;
        }

        for (SubCommand sub : subCommands) {
            if (sub.matches(args[0])) {
                if (
                        permission == null ||
                        sender.hasPermission("natural."+permission+"."+sub.getPerm()) ||
                        sender.hasPermission("natural."+permission+".*") ||
                        sender.hasPermission("natural.*"))
                {
                    sub.execute(sender, args);
                    return true;
                } else {
                    sender.sendMessage(FileManager.getMessage("insufficient_permissions").replace("{permission}", "natural."+permission+"."+sub.getPerm()));
                    return true;
                }
            }
        }

        sender.sendMessage("§cUnknown subcommand! Try /" + label);
        return true;
    }
}
