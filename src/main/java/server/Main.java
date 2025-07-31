package server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        H2Database.init();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/shorten", new UrlShortenerHandler());
        server.createContext("/r", new RedirectHandler());
        server.setExecutor(null);
        server.start();
        logger.info("Server started on port 8080");
    }
}