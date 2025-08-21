package me.flasser.naturalcoinflip.utility.itemUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NamedItem {

    private final ItemStack item;

    public NamedItem(ItemStack base, String name, String... lore) {
        this.item = applyMeta(base, name, lore.length == 0 ? null : Arrays.asList(lore));
    }

    public NamedItem(Material material, int amount, String name, String... lore) {
        this.item = applyMeta(new ItemStack(material, amount), name, lore.length == 0 ? null : Arrays.asList(lore));
    }

    public NamedItem(Material material, int amount, byte bytes, String name, String... lore) {
        this.item = applyMeta(new ItemStack(material, amount, bytes), name, lore.length == 0 ? null : Arrays.asList(lore));
    }

    private ItemStack applyMeta(ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (name != null && !name.isEmpty()) {
                meta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', name));
            }
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore.stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack get() {
        return item.clone();
    }
}
