package com.rank.rocket.utils;

import java.net.URL;

public class URLUtils {
    public static String getDomainName(String url) {
        try {
            URL u = new URL(url);
            return u.getHost();
        } catch (Exception e) {
            throw new RuntimeException("Invalid URL: " + url, e);
        }
    }
}
