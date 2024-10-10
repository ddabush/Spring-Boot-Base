package com.rank.rocket.controllers;

import com.rank.rocket.crawler.WebCrawler;
import com.rank.rocket.models.WebPageMetaData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping(value = "/")
public class CrawlingController {

    @GetMapping("crawl")
    public ResponseEntity<List<WebPageMetaData>> crawl(@RequestParam("website") String website) {
        WebCrawler webCrawler = new WebCrawler(website);
        List<WebPageMetaData> webPagesMetaData = webCrawler.crawl();
        ResponseEntity<List<WebPageMetaData>> httpResponse = new ResponseEntity(webPagesMetaData, HttpStatus.OK);
        return httpResponse;
    }


}
