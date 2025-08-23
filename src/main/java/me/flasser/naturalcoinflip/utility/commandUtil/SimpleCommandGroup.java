package me.flasser.naturalcoinflip.utility.commandUtil;

import me.flasser.naturalcoinflip.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class SimpleCommandGroup implements CommandExecutor, TabCompleter {

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
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        String currentArg = args[args.length-1].toLowerCase();

        SubCommand sub = null;
        for (SubCommand s : subCommands) {
            if (s.getName().equalsIgnoreCase(args[0])) {
                sub = s;
                break;
            }
            for (String a : s.getAliases()) {
                if (a.equalsIgnoreCase(args[0])) {
                    sub = s;
                    break;
                }
            }
            if (sub != null) break;
        }

        if (args.length == 1 && (args[0].isEmpty() || sub == null)) {
            for (SubCommand s : subCommands) {
                completions.add(s.getName());
                completions.addAll(Arrays.asList(s.getAliases()));
            }
        } else if (sub != null) {
            String type = sub.getCompletionType(args.length-1);
            if (type != null) {
                switch (type.toLowerCase()) {
                    case "player":
                        for (Player player : Bukkit.getOnlinePlayers()) completions.add(player.getName());
                        break;
                    case "integer":
                        completions.add("10");
                        completions.add("100");
                        completions.add("1000");
                        break;
                    case "string":
                        completions.add("example");
                        break;
                    case "subcommand":
                        for (SubCommand s : subCommands) {
                            completions.add(s.getName());
                            completions.addAll(Arrays.asList(s.getAliases()));
                        }
                        break;
                }
            }
        }

        completions.removeIf(s -> !s.toLowerCase().startsWith(currentArg));
        completions.sort(String::compareToIgnoreCase);
        return completions;
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
