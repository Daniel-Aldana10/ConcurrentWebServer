package org.example;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utility class to fetch remote content from a URL.
 */
public class ContentFetcher {
    /**
     * Fetches the content and content type from the given URL.
     * @param urlString the URL to fetch
     * @return a ContentResult containing the content bytes and content type
     * @throws IOException if an error occurs
     */
    public static ContentResult fetch(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        String contentType = connection.getContentType();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream is = connection.getInputStream()) {
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        }
        return new ContentResult(buffer.toByteArray(), contentType);
    }

    /**
     * Simple data holder for content bytes and content type.
     */
    public static class ContentResult {
        public final byte[] content;
        public final String contentType;
        public ContentResult(byte[] content, String contentType) {
            this.content = content;
            this.contentType = contentType;
        }
    }
} 