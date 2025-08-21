package me.flasser.naturalcoinflip.commands.cfCommands;

import me.flasser.naturalcoinflip.managers.FlipManager;
import me.flasser.naturalcoinflip.menues.cfMenu.CFMenu;
import me.flasser.naturalcoinflip.utility.commandUtil.SimpleCommandGroup;
import org.bukkit.entity.Player;

public class CoinFlipGroup extends SimpleCommandGroup {

    public CoinFlipGroup() {
        register(new CreateSub("create", "make", "put"));
        register(new DeleteSub("delete", "remove", "del", "rem"));

        setPermission(null);

        setDefaultAction((sender, args) -> {
            Player player = (Player) sender;
            FlipManager.updateSQLPlayer(player.getUniqueId());
            CFMenu.openCFMenu(player, 0);
        });
    }
}