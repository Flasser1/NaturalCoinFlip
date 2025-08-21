package me.flasser.naturalcoinflip.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class SQLManager {
    public static Connection con;

    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void connect() {
        if (!isConnected()) {

            File dataFolder = Bukkit.getPluginManager().getPlugin("NaturalCoinFlip").getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            try {
                Class.forName("org.sqlite.JDBC");

                File dbFile = new File(Bukkit.getPluginManager().getPlugin("NaturalCoinFlip").getDataFolder(), "database.db");
                String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
                con = DriverManager.getConnection(url);

                con.createStatement().execute("PRAGMA foreign_keys = ON;");

                console.sendMessage("NATURALCOINFLIP: DATABASE CONNECTED");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                console.sendMessage("NATURALCOINFLIP: DATABASE DISCONNECTED");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return (con != null);
    }

    public static Connection getConnection() {
        return con;
    }

    public static boolean isSetUp() {
        if (!isConnected()) {
            return false;
        }

        try {
            Statement stmt = con.createStatement();

            String[] tables = {"Flips"};
            for (String table : tables) {
                ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'");

                if (!rs.next()) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void setUp() {
        if (!isConnected()) {
            return;
        }

        try {
            con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Flips (" +
                    "UUID VARCHAR(36) PRIMARY KEY, " +
                    "Amount INT NOT NULL, " +
                    "Creation BIGINT NOT NULL" +
                    ");");

            con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Players (" +
                    "UUID VARCHAR(36) PRIMARY KEY, " +
                    "Won INT NOT NULL, " +
                    "Lost INT NOT NULL" +
                    ");");

            console.sendMessage("NATURALCOINFLIP: DATABASE TABLES SETUP COMPLETE");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
