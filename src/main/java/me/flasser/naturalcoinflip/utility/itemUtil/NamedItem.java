package me.flasser.naturalcoinflip.utility.itemUtil;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
@Async
public class NamedItem {

    private final ItemStack item;

    public NamedItem(ItemStack base, String name, String... lore) {
        this.item = applyMeta(base, null, name, lore.length == 0 ? null : Arrays.asList(lore));
    }

    public NamedItem(Material material, int amount, String name, String... lore) {
        this.item = applyMeta(new ItemStack(material, amount), null, name, lore.length == 0 ? null : Arrays.asList(lore));
    }

    public NamedItem(Material material, int amount, byte bytes, String name, String... lore) {
        this.item = applyMeta(new ItemStack(material, amount, bytes), null, name, lore.length == 0 ? null : Arrays.asList(lore));
    }

    public NamedItem(Material material, int amount, byte bytes, String url, String name, String... lore) {
        this.item = applyMeta(new ItemStack(material, amount, bytes), url, name, lore.length == 0 ? null : Arrays.asList(lore));
    }

    private ItemStack applyMeta(ItemStack item, String url, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (name != null && !name.isEmpty()) {
                meta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', name));
            }
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore.stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
            }
            if (url != null && !url.isEmpty()) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                profile.getProperties().put("textures", new Property("textures", url));

                try {
                    Field field = meta.getClass().getDeclaredField("profile");
                    field.setAccessible(true);
                    field.set(meta, profile);
                } catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack get() {
        return item.clone();
    }
}