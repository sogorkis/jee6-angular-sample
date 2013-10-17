package net.ogorkis.async;



import net.ogorkis.crawler.WebPageRenderer;

import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Callable;

public class CrawlTask implements Callable<CrawlResponse> {

    private final URL url;
    private final OutputStream os;
    private final WebPageRenderer webpageRenderer;

    public CrawlTask(URL url, OutputStream os, WebPageRenderer webpageRenderer) {
        this.url = url;
        this.os = os;
        this.webpageRenderer = webpageRenderer;
    }

    @Override
    public CrawlResponse call() throws Exception {
        return webpageRenderer.crawlPage(url, os);
    }

    public URL getUrl() {
        return url;
    }

}
