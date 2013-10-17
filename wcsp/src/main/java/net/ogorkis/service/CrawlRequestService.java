package net.ogorkis.service;

import net.ogorkis.async.CrawlResponse;
import net.ogorkis.data.CrawlRequestRepo;
import net.ogorkis.model.CrawlRequest;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.net.URL;

@Stateless
public class CrawlRequestService {

    @Inject
    private CrawlRequestRepo crawlRequestRepo;

    public CrawlRequest createCrawlRequestStart(URL url, long startTime) {
        CrawlRequest crawlRequest = new CrawlRequest();
        crawlRequest.setStartTimeUTC(new DateTime(startTime));
        crawlRequest.setUrl(url.toString());

        return crawlRequestRepo.save(crawlRequest);
    }

    public void updateCrawlRequestStatusToUnexpectedError(CrawlRequest request) {
        crawlRequestRepo.markRequestStatusCodeFailed(request);
    }

    public void updateCrawlRequest(CrawlRequest request, CrawlResponse response, long startTime, long endTime) {
        long processingTime = endTime - startTime;
        request.setReturnCode(response.getReturnCode());
        request.setProcessingTime(processingTime);
        request.setByteSize(response.getByteSize());

        crawlRequestRepo.updateReturnCodeAndProcessingTimeAndByteSize(request);
    }

}
