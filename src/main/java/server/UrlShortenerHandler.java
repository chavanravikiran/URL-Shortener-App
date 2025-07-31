package server;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class UrlShortenerHandler implements HttpHandler {
    private final UrlRepository repo = new UrlRepository();
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

//        if ("OPTIONS".equals(exchange.getRequestMethod())) {
//            exchange.sendResponseHeaders(204, -1);
//            return;
//        }

        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parse(query);

            String originalUrl = params.get("url");
            String shortId = generateId();
            boolean success = repo.saveMapping(shortId, originalUrl);
            logger.info("Shortening URL: {} -> {}", originalUrl, shortId);

            String shortenedUrl = "http://localhost:8080/r/" + shortId;

            String jsonResponse = String.format(
                "{\"result\": {\"full_short_link\": \"%s\", \"original_link\": \"%s\"}}",shortenedUrl, originalUrl);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            exchange.getResponseBody().write(jsonResponse.getBytes());
            exchange.close();
        }
    }

    
    private Map<String, String> parse(String body) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    private String generateId() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}