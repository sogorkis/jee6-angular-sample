package net.ogorkis.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@Table(name = "crawl_requests")
@XmlRootElement
public class CrawlRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "appSeqGenerator")
   // @TableGenerator(name = "appSeqGenerator", table = "app_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "crawl_requests", initialValue = 1, allocationSize = 50)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "start_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startTimeUTC;

    @Column(name = "processing_time")
    private Long processingTime;

    @Column(name = "return_code")
    private Integer returnCode;

    @Column(name = "byte_size")
    private Long byteSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DateTime getStartTimeUTC() {
        return startTimeUTC;
    }

    public void setStartTimeUTC(DateTime startTimeUTC) {
        this.startTimeUTC = startTimeUTC;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public Long getByteSize() {
        return byteSize;
    }

    public void setByteSize(Long byteSize) {
        this.byteSize = byteSize;
    }
}
