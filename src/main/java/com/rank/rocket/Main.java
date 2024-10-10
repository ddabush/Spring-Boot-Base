package com.rank.rocket;

import com.rank.rocket.crawler.WebCrawler;

public class Main {
    public static void main(String[] args) {
        String startUrl = "https://www.mako.co.il/";
        WebCrawler webCrawler = new WebCrawler(startUrl);
        webCrawler.crawl();


    }



}
