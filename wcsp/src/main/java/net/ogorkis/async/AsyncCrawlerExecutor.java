package net.ogorkis.async;


import net.ogorkis.crawler.WebPageRenderer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Startup
@Singleton
public class AsyncCrawlerExecutor {

    @Inject
    private WebPageRenderer webpageRenderer;

    private ExecutorService exec;

    @PostConstruct
    public void init() {
        exec = Executors.newCachedThreadPool();
    }

    public Future<CrawlResponse> crawlURL(URL url, OutputStream os) {
        CrawlTask crawlTask = new CrawlTask(url, os, webpageRenderer);

        return exec.submit(crawlTask);
    }

}
