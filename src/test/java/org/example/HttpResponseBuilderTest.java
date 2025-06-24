package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HttpResponseBuilderTest {
    @Test
    void testBuildSimpleResponse() {
        byte[] body = "Hello World".getBytes();
        HttpResponseBuilder builder = new HttpResponseBuilder()
                .addHeader("Content-Type: text/plain")
                .addHeader("Content-Length: " + body.length)
                .setBody(body);
        byte[] response = builder.build();
        String responseStr = new String(response);
        assertTrue(responseStr.contains("HTTP/1.1 200 OK"));
        assertTrue(responseStr.contains("Content-Type: text/plain"));
        assertTrue(responseStr.contains("Content-Length: " + body.length));
        assertTrue(responseStr.endsWith("Hello World"));
    }
} 