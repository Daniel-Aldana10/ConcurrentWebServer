package org.example;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing and building HTTP cookies.
 */
public class CookieManager {
    /**
     * Parses a Cookie header string into a map.
     * @param cookieHeader the raw Cookie header
     * @return a map of cookie names to values
     */
    public static Map<String, String> parseCookies(String cookieHeader) {
        Map<String, String> cookies = new HashMap<>();
        if (cookieHeader != null) {
            String[] cookiePairs = cookieHeader.split(";");
            for (String pair : cookiePairs) {
                String[] kv = pair.trim().split("=", 2);
                if (kv.length == 2) {
                    cookies.put(kv[0], kv[1]);
                }
            }
        }
        return cookies;
    }

    /**
     * Builds a Set-Cookie header string.
     * @param name cookie name
     * @param value cookie value
     * @return the Set-Cookie header value
     */
    public static String buildSetCookie(String name, String value) {
        return name + "=" + value + "; Path=/; HttpOnly";
    }
} 