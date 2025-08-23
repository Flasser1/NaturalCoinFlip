package me.flasser.naturalcoinflip.utility.commandUtil;

import org.bukkit.command.CommandSender;

import java.util.HashMap;

public abstract class SubCommand {

    private final String name;
    private String permission;
    private final String[] aliases;
    private final HashMap<Integer, String> completions = new HashMap<>();

    public SubCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String getPerm() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setCompletion(int argIndex, String type) {
        completions.put(argIndex, type.toLowerCase());
    }

    public String getCompletionType(int argIndex) {
        return completions.get(argIndex);
    }

    public boolean matches(String arg) {
        if (arg.equalsIgnoreCase(name)) return true;
        for (String alias : aliases) {
            if (arg.equalsIgnoreCase(alias)) return true;
        }
        return false;
    }

    public abstract String getDescription();

    public abstract String getUsage();

    public abstract void execute(CommandSender sender, String[] args);
}