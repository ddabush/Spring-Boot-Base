package com.rank.rocket.data.structures;

import java.util.*;

public class URLSToCrawlQueue {
    private final Queue<String> urlsToCrawl = new LinkedList<>();
    private Set<String> pendingURLs = new HashSet<>();
    private Set<String> removedURLs = new HashSet<>();

    public synchronized void putIfNeverUsed(String url) {
        if (!pendingURLs.contains(url) && !removedURLs.contains(url)) {
            urlsToCrawl.add(url);
            pendingURLs.add(url);
        }
    }

    public synchronized String pollURL() {
        String url = urlsToCrawl.poll();
        if (url != null) {
            pendingURLs.remove(url);
            removedURLs.add(url);
        }
        return url;
    }

}
