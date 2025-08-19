package me.flasser.naturalcoinflip.utility.commandUtil;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private final String name;
    private final String[] aliases;

    public SubCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
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