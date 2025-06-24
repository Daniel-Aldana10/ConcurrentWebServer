package org.example;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Concurrent Web Proxy Server.
 * <p>
 * Listens for HTTP POST requests, fetches external content, manages cookies, and responds with the content.
 * Uses a thread pool to handle multiple clients concurrently.
 * </p>
 */
public class Server {
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Handles a single client connection: parses the request, manages cookies, fetches remote content,
     * builds the HTTP response, and sends it back to the client.
     */
    static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 OutputStream out = clientSocket.getOutputStream()) {

                
                String requestLine = in.readLine();
                int contentLength = 0;
                String header;
                String cookieHeader = null;
                while ((header = in.readLine()) != null && !header.isEmpty()) {
                    if (header.toLowerCase().startsWith("content-length:")) {
                        contentLength = Integer.parseInt(header.split(":")[1].trim());
                    } else if (header.toLowerCase().startsWith("cookie:")) {
                        cookieHeader = header.substring(7).trim();
                    }
                }

                Map<String, String> cookies = CookieManager.parseCookies(cookieHeader);

                char[] bodyChars = new char[contentLength];
                in.read(bodyChars);
                String urlReceived = new String(bodyChars).trim();


                ContentFetcher.ContentResult result = ContentFetcher.fetch(urlReceived);


                String userId = cookies.getOrDefault("userId", String.valueOf(System.currentTimeMillis() + clientSocket.getPort()));
                String setCookieHeader = CookieManager.buildSetCookie("userId", userId);


                HttpResponseBuilder responseBuilder = new HttpResponseBuilder()
                        .addHeader("Content-Type: " + result.contentType)
                        .addHeader("Content-Length: " + result.content.length)
                        .addHeader("Access-Control-Allow-Origin: *")
                        .addHeader("Set-Cookie: " + setCookieHeader)
                        .setBody(result.content);

                out.write(responseBuilder.build());

            } catch (Exception e) {
                // Manejo de errores
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error cerrando socket: " + e.getMessage());
                }
            }
        }
    }
}