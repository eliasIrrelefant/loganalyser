package it.eup.loganalyser.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "origin")
public class LogOrigin {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 1024, nullable = false)
    private String path;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate = new Date();

    @Column(name = "total_count")
    private Long totalCount;
    
    @Column(name = "error_count")
    private Long errorsCount;

    @Column(name = "imported_count")
    private Long successCount;

    @Column(name = "filtered_count")
    private Long filteredCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date imported) {
        this.importDate = imported;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getErrorsCount() {
        return errorsCount;
    }
    
    public Long getFilteredCount() {
        return filteredCount;
    }
    
    public Long getSuccessCount() {
        return successCount;
    }
    
    public Long getTotalCount() {
        return totalCount;
    }
    
    public void setErrorsCount(Long errors) {
        this.errorsCount = errors;
    }
    
    public void setFilteredCount(Long filtered) {
        this.filteredCount = filtered;
    }
    
    public void setSuccessCount(Long success) {
        this.successCount = success;
    }
    
    public void setTotalCount(Long total) {
        this.totalCount = total;
    }
}
