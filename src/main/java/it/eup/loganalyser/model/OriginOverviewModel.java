package it.eup.loganalyser.model;

import java.util.Date;

public class OriginOverviewModel {

    private Long id;
    private String path;
    private Date importDate;

    private Long totalCount;
    private Long errorsCount;
    private Long successCount;
    private Long filteredCount;

    public OriginOverviewModel() {
    }

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

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(Long errorsCount) {
        this.errorsCount = errorsCount;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFilteredCount() {
        return filteredCount;
    }

    public void setFilteredCount(Long filteredCount) {
        this.filteredCount = filteredCount;
    }

}
