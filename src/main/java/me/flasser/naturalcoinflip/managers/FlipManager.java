package me.flasser.naturalcoinflip.managers;

import me.flasser.naturalcoinflip.utils.UUIDtoNameUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FlipManager {

    public static void addFlip(UUID player, Integer amount) {
        if (!SQLManager.isConnected()) {
            return;
        }

        String query = "INSERT INTO Flips (UUID, Amount, Creation) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = SQLManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.setInt(2, amount);
            ps.setLong(3, System.currentTimeMillis());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void overrideFlip(UUID player, Integer amount) {
        if (!SQLManager.isConnected()) {
            return;
        }

        removeFlip(player);
        addFlip(player, amount);
    }

    public static void removeFlip(UUID player) {
        if (!SQLManager.isConnected()) {
            return;
        }

        String query = "DELETE * FROM Flips WHERE UUID = ?";
        try (PreparedStatement ps = SQLManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasFlip(UUID player) {
        if (!SQLManager.isConnected()) {
            return false;
        }

        String query = "SELECT * FROM Flips WHERE UUID = ?";

        try (PreparedStatement ps = SQLManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static FlipInfo getFlipInfo(UUID player) {
        if (!SQLManager.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Flips WHERE UUID = ?";

        try (PreparedStatement ps = SQLManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FlipInfo flip = new FlipInfo();
                    flip.UUID = UUID.fromString(rs.getString("UUID"));
                    flip.amount = rs.getInt("Amount");
                    flip.creation = rs.getLong("creation");
                    return flip;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PlayerInfo getPlayerInfo(UUID player) {
        if (!SQLManager.isConnected()) {
            return null;
        }

        String query = "SELECT * FROM Players WHERE UUID = ?";

        try (PreparedStatement ps = SQLManager.getConnection().prepareStatement(query)) {
            ps.setString(1, player.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PlayerInfo playerInfo = new PlayerInfo();
                    playerInfo.UUID = UUID.fromString(rs.getString("UUID"));
                    playerInfo.wins = rs.getInt("wins");
                    playerInfo.loses = rs.getInt("loses");
                    return playerInfo;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<UUID> getAllFlips() {
        if (!SQLManager.isConnected()) {
            return null;
        }

        String query = "SELECT UUID FROM Flips";
        List<UUID> flipUUIDs = new ArrayList<>();

        try (PreparedStatement ps = SQLManager.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                flipUUIDs.add(UUID.fromString(rs.getString("UUID")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flipUUIDs;
    }

    public static class FlipInfo {
        public UUID UUID;
        public Integer amount;
        public Long creation;
    }

    public static class PlayerInfo {
        public UUID UUID;
        public Integer wins;
        public Integer loses;
    }
}