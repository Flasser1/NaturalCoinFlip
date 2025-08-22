package me.flasser.naturalcoinflip.commands.cfaCommands;

import me.flasser.naturalcoinflip.utility.commandUtil.SimpleCommandGroup;

public class CoinFlipAdminGroup extends SimpleCommandGroup{

    public CoinFlipAdminGroup() {
        register(new DeleteSub("delete", "del", "rem"));
        register(new AddLossSub("addloss", "del", "rem"));
        register(new AddWinSub("addwin", "del", "rem"));
        register(new InfoSub("info", "del", "rem"));
        register(new RemLossSub("remloss", "del", "rem"));
        register(new RemWinSub("remwin", "del", "rem"));
        register(new StatsSub("stats", "del", "rem"));
        register(new VoidSub("void", "del", "rem"));
        register(new ResetSub("reset", "reset"));
        register(new ReloadSub("reload", "reload"));

        setPermission("coinflip");
    }
}

//Sub commands, delete, void, stats, info, addwin, addloss, remwin, remloss, reset