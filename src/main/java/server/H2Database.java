package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Database {
    static final String JDBC_URL = "jdbc:h2:./data/url_db";

    public static void init() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS urls (shortId VARCHAR PRIMARY KEY, originalUrl VARCHAR)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}