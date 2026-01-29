package me.flasser.naturalcoinflip.managers;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import me.flasser.naturalcoinflip.NaturalCoinFlip;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class FileManager {

    private @Inject NaturalCoinFlip plugin;

    private File messagesFile;

    private FileConfiguration messages;

    public FileConfiguration getMessages() {
        return messages;
    }

    public String getMessage(String path) {
        return messages.getString(path).replace("&", "§");
    }

    public String[] getListMessage(String path) {
        return messages.getStringList(path).stream()
                .map(s -> s.replace("&", "§"))
                .toArray(String[]::new);
    }

    public void createMessages() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(messagesFile), StandardCharsets.UTF_8)) {
            messages.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
