package it.eup.loganalyser.model;

import java.util.ArrayList;
import java.util.List;

public class ConfigModel {

  private String logformat;
  private List<QueryModel> queries = new ArrayList<>();

  public String getLogformat() {
    return logformat;
  }

  public List<QueryModel> getQueries() {
    return queries;
  }

  public void setLogformat(String logFormat) {
    this.logformat = logFormat;
  }

  public void setQueries(List<QueryModel> queries) {
    this.queries = queries;
  }
}
