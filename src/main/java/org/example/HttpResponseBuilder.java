package org.example;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to build HTTP responses.
 */
public class HttpResponseBuilder {
    private final List<String> headers = new ArrayList<>();
    private byte[] body = new byte[0];

    /**
     * Adds a header to the response.
     * @param header the header line (e.g., "Content-Type: text/html")
     * @return this builder
     */
    public HttpResponseBuilder addHeader(String header) {
        headers.add(header);
        return this;
    }

    /**
     * Sets the body of the response.
     * @param body the body bytes
     * @return this builder
     */
    public HttpResponseBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }

    /**
     * Builds the full HTTP response as bytes.
     * @return the response bytes
     */
    public byte[] build() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\r\n");
        for (String header : headers) {
            sb.append(header).append("\r\n");
        }
        sb.append("\r\n");
        byte[] headerBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] response = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(body, 0, response, headerBytes.length, body.length);
        return response;
    }
} 