package net.ogorkis.test;

import net.ogorkis.data.CrawlRequestRepo;
import net.ogorkis.model.CrawlRequest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class CrawlRequestRepoTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return IntegrationTestBase.createTestArchiveWithPersistence()
                .addClasses(CrawlRequest.class, CrawlRequestRepo.class);
    }

    @Inject
    private CrawlRequestRepo crawlRequestRepo;

    @Test
    public void testSave() {
        Assert.assertNotNull(crawlRequestRepo);
        CrawlRequest request = new CrawlRequest();
        request.setProcessingTime(1500L);
        request.setReturnCode(200);
        request.setUrl("http://ogorkis.net/");
        request.setStartTimeUTC(DateTime.now());

        Assert.assertTrue(crawlRequestRepo.getAll().isEmpty());

        crawlRequestRepo.save(request);

        Assert.assertEquals(1, crawlRequestRepo.getAll().size());
    }

}
