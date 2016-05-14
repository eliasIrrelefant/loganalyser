package it.eup.loganalyser.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "logdata")
public class LogDataRow {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 2048)
    private String path;

    @Column(length = 16)
    private String method;

    @Column(length = 2048)
    private String queryString;

    @Column(nullable = true, length = 128)
    private String sessionid;

    @Column
    private Integer statusCode;

    @Column
    private Integer responseTime;

    @Column
    private Integer transferedBytes;

    @Column(length = 1024)
    private String userAgent;

    @Column(length = 128)
    private String ip;

    @Column(length = 128)
    private String forwardedForIp;

    @Column(length = 16)
    private String httpVersion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(length = 2048)
    private String referer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LogOrigin origin;

    @Column
    private Boolean cacheAgePresent = Boolean.FALSE;

    @Column(nullable = true)
    private Integer cacheAge;

    @Column(nullable = true)
    private String cipherName;

    @Column(nullable = true)
    private String cipherVersion;

    @Column(nullable = true)
    private String cipherBits;

    @Column(nullable = true)
    private Boolean sslFrontend;

    @Column(nullable = true, length = 1024)
    private String hostname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getTransferedBytes() {
        return transferedBytes;
    }

    public void setTransferedBytes(Integer transferedBytes) {
        this.transferedBytes = transferedBytes;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public LogOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(LogOrigin logOrigin) {
        this.origin = logOrigin;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getCacheAge() {
        return cacheAge;
    }

    public void setCacheAge(Integer cacheAge) {
        this.cacheAge = cacheAge;
    }

    public Boolean getCacheAgePresent() {
        return cacheAgePresent;
    }

    public void setCacheAgePresent(Boolean cacheAgePresent) {
        this.cacheAgePresent = cacheAgePresent;
    }

    public String getForwardedForIp() {
        return forwardedForIp;
    }

    public void setForwardedForIp(String forwardedForIp) {
        this.forwardedForIp = forwardedForIp;
    }

    public String getCipherName() {
        return cipherName;
    }

    public void setCipherName(String cipherName) {
        this.cipherName = cipherName;
    }

    public String getCipherVersion() {
        return cipherVersion;
    }

    public void setCipherVersion(String cipherVersion) {
        this.cipherVersion = cipherVersion;
    }

    public String getCipherBits() {
        return cipherBits;
    }

    public void setCipherBits(String cipherBits) {
        this.cipherBits = cipherBits;
    }

    public Boolean getSslFrontend() {
        return sslFrontend;
    }

    public void setSslFrontend(Boolean sslFrontend) {
        this.sslFrontend = sslFrontend;
    }
}
