package me.flasser.naturalcoinflip.utility.commandUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SimpleCommandGroup implements CommandExecutor {

    private final List<SubCommand> subCommands = new ArrayList<>();
    private BiConsumer<CommandSender, String[]> defaultAction; // 👈 run when no args

    public void register(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public void setDefaultAction(BiConsumer<CommandSender, String[]> action) {
        this.defaultAction = action;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (defaultAction != null) {
                defaultAction.accept(sender, args);
                return true;
            }
            sender.sendMessage("§cAvailable subcommands:");
            for (SubCommand sub : subCommands) {
                sender.sendMessage("§7- §e" + sub.getUsage() + " §7: " + sub.getDescription());
            }
            return true;
        }

        for (SubCommand sub : subCommands) {
            if (sub.matches(args[0])) {
                sub.execute(sender, args);
                return true;
            }
        }

        sender.sendMessage("§cUnknown subcommand! Try /" + label);
        return true;
    }
}
