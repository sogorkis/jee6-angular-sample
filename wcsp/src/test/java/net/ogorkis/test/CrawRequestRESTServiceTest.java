package net.ogorkis.test;


import net.ogorkis.data.CrawlRequestRepo;
import net.ogorkis.model.CrawlRequest;
import net.ogorkis.rest.CrawlRequestRESTService;
import net.ogorkis.rest.JaxRsActivator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class CrawRequestRESTServiceTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return IntegrationTestBase.createTestArchiveWithPersistence()
                .addClasses(CrawlRequest.class, CrawlRequestRepo.class, CrawlRequestRESTService.class, JaxRsActivator.class);
    }

    @Inject
    private CrawlRequestRESTService crawlRequestRESTService;

    @Test
    public void testGetAll() {
        crawlRequestRESTService.getRequests(null, null, null, null);
    }
}
