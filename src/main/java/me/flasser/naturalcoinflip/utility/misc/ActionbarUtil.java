package me.flasser.naturalcoinflip.utility.misc;

import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.platform.core.annotation.Component;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
@Component
@Async
public class ActionbarUtil {

    public static void sendActionBar(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}