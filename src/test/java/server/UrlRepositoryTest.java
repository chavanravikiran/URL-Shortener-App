package server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeAll;
public class UrlRepositoryTest {
    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static UrlRepository repo;

    @BeforeAll
    public static void setupDatabase() throws Exception {
        // Create table in in-memory test DB before initialize /create UrlRepository
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE urls (shortId VARCHAR PRIMARY KEY, originalUrl VARCHAR)");
        }

        repo = new UrlRepository(TEST_DB_URL);
    }

    @Test
    public void testSaveAndRetrieve() {
        String shortId = "test123";
        String original = "https://openai.com";

        assertTrue(repo.saveMapping(shortId, original));
        assertEquals(original, repo.getOriginalUrl(shortId));
    }

    @Test
    public void testDuplicateSave() {
        String shortId = "test456";
        String url1 = "https://example.com";
        String url2 = "https://duplicate.com";

        assertTrue(repo.saveMapping(shortId, url1));
        assertFalse(repo.saveMapping(shortId, url2)); // primary key conflict
    }
}
