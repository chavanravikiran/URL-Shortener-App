package server;

import java.sql.*;

public class UrlRepository {
    private final String jdbcUrl;

    public UrlRepository() {
        this(H2Database.JDBC_URL); // default for app
    }

    public UrlRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public boolean saveMapping(String shortId, String originalUrl) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO urls VALUES (?, ?)")) {
            stmt.setString(1, shortId);
            stmt.setString(2, originalUrl);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // ← Add this to debug issues
            return false;
        }
    }

    public String getOriginalUrl(String shortId) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement("SELECT originalUrl FROM urls WHERE shortId = ?")) {
            stmt.setString(1, shortId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("originalUrl") : null;
        } catch (SQLException e) {
            e.printStackTrace(); // ← Add this to debug issues
            return null;
        }
    }
}
