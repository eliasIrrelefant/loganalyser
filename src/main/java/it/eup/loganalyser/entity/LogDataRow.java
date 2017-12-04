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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

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

}
