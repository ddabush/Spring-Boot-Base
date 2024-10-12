package com.rank.rocket.crawler;

import com.rank.rocket.data.structures.URLSToCrawlQueue;
import com.rank.rocket.models.PageIssue;
import com.rank.rocket.models.WebPageMetaData;
import com.rank.rocket.service.IPageIssueEnricher;
import com.rank.rocket.service.IPageIssueParser;
import com.rank.rocket.service.impl.PageIssueEnricher;
import com.rank.rocket.utils.URLUtils;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class WebCrawler {
    public static final int TIME_UNTIL_NEXT_CHECK = 5000;
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final String domain;
    private final String startURL;
    private final int THREAD_POOL_SIZE = 10;
    private final int MAX_PAGES_TO_CRAWL = 1000;
    private AtomicInteger tasksSubmittedCount = new AtomicInteger(0);
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private List<WebPageMetaData> webPagesMetaData = Collections.synchronizedList(new ArrayList<>());
    private URLSToCrawlQueue urlsToCrawlQueue = new URLSToCrawlQueue();
    private int lastTasksSubmittedCount = 0;
    boolean stopCrawling = false;
    private List<IPageIssueParser> pageIssueParsers;
    private IPageIssueEnricher issueEnricher = new PageIssueEnricher();

    public WebCrawler(String startURL, List<IPageIssueParser> pageIssueParsers) {
        this.startURL = startURL;
        this.domain = URLUtils.getDomainName(startURL);
        this.pageIssueParsers = pageIssueParsers;
    }

    private void extractInfoFromWebPage(String url) {
        visitedUrls.add(url);
        int statusCode = -1;
        boolean crawlingFailed = false;
        String failedToCrawlReason = null;
        List<PageIssue> issues = new ArrayList<>();
        try {
            Connection.Response response = Jsoup.connect(url).execute();
            statusCode = response.statusCode();
            Document doc = response.parse();
            System.out.println(webPagesMetaData.size() + ": url to investigate is " + url + " status code " + statusCode);

            for (IPageIssueParser<PageIssue> pageIssueParser : pageIssueParsers) {
                PageIssue pageIssue = pageIssueParser.parsePage(doc);
                issues.add(pageIssue);
                issueEnricher.enrich(pageIssue);
            }

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String nextUrl = link.absUrl("href");
                if (nextUrl.contains(domain) && !visitedUrls.contains(nextUrl)) {
                    urlsToCrawlQueue.putIfNeverUsed(nextUrl);
                }
            }
        } catch (HttpStatusException e) {
            crawlingFailed = true;
            statusCode = e.getStatusCode();
            failedToCrawlReason = e.getMessage();
            System.err.println("Failed to crawl: " + url + " due to " + e.getMessage());
        } catch (IOException e) {
            crawlingFailed = true;
            failedToCrawlReason = e.getMessage();
            System.err.println("Failed to crawl: " + url + " due to " + e.getMessage());
        } catch (Exception e) {
            failedToCrawlReason = e.getMessage();
            crawlingFailed = true;
        }
        WebPageMetaData webPageMetaData = new WebPageMetaData(crawlingFailed, failedToCrawlReason, url, statusCode, issues);
        webPagesMetaData.add(webPageMetaData);
    }

    public List<WebPageMetaData> crawl() {
        urlsToCrawlQueue.putIfNeverUsed(startURL);
        Thread checkIfNoTasksSubmittedRecentlyThread = new Thread(new CheckIfNoTasksSubmittedRecently());
        checkIfNoTasksSubmittedRecentlyThread.start();

        while (tasksSubmittedCount.get() < MAX_PAGES_TO_CRAWL && !stopCrawling) {
            String url = urlsToCrawlQueue.pollURL();
            if (url != null) {
                executorService.submit(() -> {
                    extractInfoFromWebPage(url);
                });
                tasksSubmittedCount.incrementAndGet();
            }
        }
        shutDownExecutor();
        return webPagesMetaData;

    }

    private void shutDownExecutor() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Tasks terminated in InterruptedException");
            throw new RuntimeException(e);
        }
    }

    class CheckIfNoTasksSubmittedRecently implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(TIME_UNTIL_NEXT_CHECK);
                    if (lastTasksSubmittedCount == tasksSubmittedCount.get()) {
                        stopCrawling = true;
                        break;
                    }
                    lastTasksSubmittedCount = tasksSubmittedCount.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

}
