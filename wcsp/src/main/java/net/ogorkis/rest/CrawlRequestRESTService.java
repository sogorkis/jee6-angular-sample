package net.ogorkis.rest;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.ogorkis.data.CrawlRequestRepo;
import net.ogorkis.model.CrawlRequest;
import org.slf4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user/crawl-requests")
@RequestScoped
public class CrawlRequestRESTService {

    @Inject
    private CrawlRequestRepo crawlRequestRepo;

    @Inject
    private Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public List<CrawlRequest> getRequests(
            @QueryParam("urlPart") final String urlPart,
            @QueryParam("maxResults") Integer maxResults,
            @QueryParam("pageNum") Integer pageNum,
            @QueryParam("orderBy") String orderBy) {

        logger.info("urlPart: {}", urlPart);

        // TODO: make real queries
        List<CrawlRequest> requests = crawlRequestRepo.getAll();

        if (Strings.isNullOrEmpty(urlPart)) {
            return requests;
        }

        return Lists.newArrayList(Iterables.filter(requests, new Predicate<CrawlRequest>() {
            @Override
            public boolean apply(CrawlRequest request) {
                return request.getUrl().contains(urlPart);
            }
        }));
    }

}
