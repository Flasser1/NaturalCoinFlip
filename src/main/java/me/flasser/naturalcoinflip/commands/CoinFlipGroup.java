package me.flasser.naturalcoinflip.commands;

import me.flasser.naturalcoinflip.menues.cfMenu.CFMenu;
import me.flasser.naturalcoinflip.utility.commandUtil.SimpleCommandGroup;
import org.bukkit.entity.Player;

public class CoinFlipGroup extends SimpleCommandGroup {

    public CoinFlipGroup() {
        register(new CreateSub("create", "make", "put"));
        register(new DeleteSub("delete", "remove", "del", "rem"));

        setDefaultAction((sender, args) -> {
            CFMenu.openCFMenu((Player) sender, 0);
        });
    }
}