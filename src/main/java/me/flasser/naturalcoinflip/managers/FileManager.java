package me.flasser.naturalcoinflip.managers;

import me.flasser.naturalcoinflip.NaturalCoinFlip;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private static File messagesFile;
    private static FileConfiguration messages;

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static String[] getMessage(String path) {
        return new String[]{
                messages.getString("prefix").replace("&", "§"),
                messages.getString(path).replace("&", "§")
        };
    }

    public static void createMessages() {

        messagesFile = new File(NaturalCoinFlip.getInstance().getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            NaturalCoinFlip.getInstance().saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);
        try {
            messages.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
