package server;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RedirectHandler implements HttpHandler {
    private final UrlRepository repo = new UrlRepository();
    private static final Logger logger = LoggerFactory.getLogger(RedirectHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String shortId = path.substring(path.lastIndexOf("/") + 1);

        String originalUrl = repo.getOriginalUrl(shortId);
        if (originalUrl != null) {
            exchange.getResponseHeaders().add("Location", originalUrl);
            exchange.sendResponseHeaders(302, -1);
            logger.info("Redirecting /r/{} to {}", shortId, originalUrl);
        } else {
            String response = "URL Not Found";
            exchange.sendResponseHeaders(404, response.length());
            exchange.getResponseBody().write(response.getBytes());
            logger.warn("Short ID not found: {}", shortId);
        }
        exchange.close();
    }
}