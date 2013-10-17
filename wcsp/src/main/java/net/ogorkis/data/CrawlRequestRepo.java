package net.ogorkis.data;

import net.ogorkis.model.CrawlRequest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class CrawlRequestRepo {

    private static final int FAILED_STATUS_CODE = -1;

    @Inject
    private EntityManager em;

    public List<CrawlRequest> getAll() {
        return em.createQuery("SELECT cr FROM CrawlRequest cr", CrawlRequest.class).getResultList();
    }

    public CrawlRequest save(CrawlRequest crawlRequest) {
        return em.merge(crawlRequest);
    }

    public void markRequestStatusCodeFailed(CrawlRequest crawlRequest) {
        em.createQuery("UPDATE CrawlRequest cr set cr.returnCode = :returnCode where cr.id = :id")
                .setParameter("id", crawlRequest.getId())
                .setParameter("returnCode", FAILED_STATUS_CODE)
                .executeUpdate();
    }

    public void updateReturnCodeAndProcessingTimeAndByteSize(CrawlRequest request) {
        em.createQuery("update CrawlRequest cr set cr.returnCode = :returnCode, cr.processingTime = :processingTime, cr.byteSize = :byteSize where cr.id = :id")
                .setParameter("id", request.getId())
                .setParameter("returnCode", request.getReturnCode())
                .setParameter("processingTime", request.getProcessingTime())
                .setParameter("byteSize", request.getByteSize())
                .executeUpdate();
    }
}
