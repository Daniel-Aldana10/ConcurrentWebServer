package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContentFetcherTest {
    @Test
    void testFetchExampleCom() throws Exception {
        ContentFetcher.ContentResult result = ContentFetcher.fetch("https://example.com");
        assertNotNull(result.content);
        assertNotNull(result.contentType);
        assertTrue(result.content.length > 0);
        assertTrue(result.contentType.startsWith("text/html"));
    }

    @Test
    void testFetchInvalidUrl() {
        assertThrows(Exception.class, () -> ContentFetcher.fetch("http://invalid.invalid"));
    }
} 