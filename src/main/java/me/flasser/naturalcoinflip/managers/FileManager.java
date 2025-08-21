package me.flasser.naturalcoinflip.managers;

import me.flasser.naturalcoinflip.NaturalCoinFlip;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private static File messagesFile;
    private static FileConfiguration messages;

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static String getMessage(String path) {
        return messages.getString(path).replace("&", "§");
    }

    public static String[] getListMessage(String path) {
        return messages.getStringList(path).stream()
                .map(s -> s.replace("&", "§"))
                .toArray(String[]::new);
    }


    public static void createMessages() {
        messagesFile = new File(NaturalCoinFlip.getInstance().getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            NaturalCoinFlip.getInstance().saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(messagesFile), StandardCharsets.UTF_8)) {
            messages.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
