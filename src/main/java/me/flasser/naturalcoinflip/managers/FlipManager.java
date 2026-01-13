package me.flasser.naturalcoinflip.managers;

import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import me.flasser.naturalcoinflip.NaturalCoinFlip;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FlipManager {

    private @Inject NaturalCoinFlip plugin;
    private @Inject SQLManager sqlManager;

    @Async
    public void addFlip(UUID player, double amount) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "INSERT INTO Flips (UUID, Amount, Creation) VALUES (?, ?, ?)";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.setDouble(2, amount);
            ps.setLong(3, System.currentTimeMillis());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void overrideFlip(UUID player, double amount) {
        if (!sqlManager.isConnected()) {
            return;
        }

        removeFlip(player);
        addFlip(player, amount);
    }

    @Async
    public void removeFlip(UUID player) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "DELETE FROM Flips WHERE UUID = ?";
        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Async
    public boolean hasFlip(UUID player) {
        if (!sqlManager.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM Flips WHERE UUID = ?";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Async
    public FlipInfo getFlipInfo(UUID player) {
        if (!sqlManager.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Flips WHERE UUID = ?";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FlipInfo flip = new FlipInfo();
                    flip.UUID = UUID.fromString(rs.getString("UUID"));
                    flip.amount = rs.getDouble("Amount");
                    flip.creation = rs.getLong("creation");
                    return flip;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Async
    public PlayerInfo getPlayerInfo(UUID player) {
        if (!sqlManager.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Players WHERE UUID = ?";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PlayerInfo playerInfo = new PlayerInfo();
                    playerInfo.UUID = UUID.fromString(rs.getString("UUID"));
                    playerInfo.wins = rs.getInt("Won");
                    playerInfo.loses = rs.getInt("Lost");
                    return playerInfo;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Async
    public List<UUID> getAllFlips() {
        if (!sqlManager.isConnected()) {
            return null;
        }

        String query = "SELECT UUID FROM Flips";
        List<UUID> flipUUIDs = new ArrayList<>();

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                flipUUIDs.add(UUID.fromString(rs.getString("UUID")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flipUUIDs;
    }

    @Async
    public void updateSQLPlayer(UUID UUID) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "INSERT OR IGNORE INTO Players (UUID, Won, Lost) VALUES (?, ?, ?)";
        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, String.valueOf(UUID));
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().info("Failed to insert/update Players: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Async
    public void resetPlayer(UUID player) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "DELETE FROM Players WHERE UUID = ?";
        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void addWon(UUID player, int amount) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "UPDATE Players SET Won = Won + "+amount+" WHERE UUID = ?";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void addLost(UUID player, int amount) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "UPDATE Players SET Lost = Lost + "+amount+" WHERE UUID = ?";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void removeWon(UUID player, int amount) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "UPDATE Players SET Won = Won - "+amount+" WHERE UUID = ?";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void removeLost(UUID player, int amount) {
        if (!sqlManager.isConnected()) {
            return;
        }

        String query = "UPDATE Players SET Lost = Lost - "+amount+" WHERE UUID = ?";

        try (PreparedStatement ps = sqlManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class FlipInfo {
        public UUID UUID;
        public double amount;
        public long creation;
    }

    public class PlayerInfo {
        public UUID UUID;
        public Integer wins;
        public Integer loses;
    }
}