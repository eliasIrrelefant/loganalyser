package it.eup.loganalyser.events;

import it.eup.loganalyser.model.StatisticsData;

public class ImportInterruptedException extends RuntimeException {

  private StatisticsData statistics;

  public ImportInterruptedException(StatisticsData statisticsData) {
    this.statistics = statisticsData;
  }

  public StatisticsData getStatistics() {
    return statistics;
  }

}
