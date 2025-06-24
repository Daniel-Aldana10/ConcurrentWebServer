package org.example;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CookieManagerTest {
    @Test
    void testParseCookies() {
        String header = "userId=abc123; session=xyz; theme=dark";
        Map<String, String> cookies = CookieManager.parseCookies(header);
        assertEquals(3, cookies.size());
        assertEquals("abc123", cookies.get("userId"));
        assertEquals("xyz", cookies.get("session"));
        assertEquals("dark", cookies.get("theme"));
    }

    @Test
    void testParseCookiesEmpty() {
        Map<String, String> cookies = CookieManager.parseCookies("");
        assertTrue(cookies.isEmpty());
    }

    @Test
    void testBuildSetCookie() {
        String setCookie = CookieManager.buildSetCookie("userId", "abc123");
        assertTrue(setCookie.startsWith("userId=abc123"));
        assertTrue(setCookie.contains("Path=/"));
        assertTrue(setCookie.contains("HttpOnly"));
    }
} 