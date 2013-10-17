package net.ogorkis.servlet;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import net.ogorkis.async.AsyncCrawlerExecutor;
import net.ogorkis.async.CrawlResponse;
import net.ogorkis.async.CrawlTask;
import net.ogorkis.model.CrawlRequest;
import net.ogorkis.service.CrawlRequestService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@WebServlet(name = "CrawlerServlet", urlPatterns = "/crawler")
public class CrawlerServlet extends HttpServlet {

    @Inject
    private Logger logger;

    @Inject
    private AsyncCrawlerExecutor crawlerExecutor;

    @Inject
    private CrawlRequestService crawlRequestService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        URL url = extractURL(request, response);

        if (url == null) {
            return;
        }

        // start crawling url asynchronously
        Future<CrawlResponse> crawlTask = crawlerExecutor.crawlURL(url, response.getOutputStream());

        // store information in db
        CrawlRequest crawlRequest = crawlRequestService.createCrawlRequestStart(url, startTime);

        // wait for crawling response
        CrawlResponse crawlResponse = getCrawlResponse(crawlTask, response);

        // unexpected exception
        if (crawlTask == null) {
            crawlRequestService.updateCrawlRequestStatusToUnexpectedError(crawlRequest);
            return;
        }

        long endTime = System.currentTimeMillis();

        // update information in db
        crawlRequestService.updateCrawlRequest(crawlRequest, crawlResponse, startTime, endTime);
    }

    private URL extractURL(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String urlParam = request.getParameter("url");

            if (Strings.isNullOrEmpty(urlParam)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parameter 'url' required.");
                return null;
            }

            return new URL(urlParam);
        } catch (MalformedURLException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed url param passed.");
            return null;
        }
    }

    private CrawlResponse getCrawlResponse(Future<CrawlResponse> crawlTask, HttpServletResponse response) throws IOException {
        try {
            return crawlTask.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Unexpected Exception", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

}
