package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.NaturalCoinFlip;
import me.flasser.naturalcoinflip.managers.CacheManager;
import me.flasser.naturalcoinflip.managers.FileManager;
import me.flasser.naturalcoinflip.utility.commandUtil.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadSub extends SubCommand {

    public ReloadSub(String name, String... aliases) {
        super("reload");
        setPermission("reload");
    }

    @Override
    public String getDescription() {
        return "Reload configs.";
    }

    @Override
    public String getUsage() {
        return "/cfa reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        NaturalCoinFlip.getInstance().saveDefaultConfig();
        FileManager.createMessages();
        CacheManager.createCache();

        sender.sendMessage(FileManager.getMessage("reload_success"));
    }
}